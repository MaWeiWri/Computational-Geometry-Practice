package com.wma.convexhull.jarvis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Jarvis算法搜索凸包，以X轴，Y轴最小的点开始从逆时针搜索凸包点，返回结果为链表 
 * 尽量保存搜索到的所有共线点，由于语言浮点数精度原因，不保证保留所有共线点
 * 算法复杂度O(hn)，其中h为凸包点数，n为总节点数
 * 
 * @author MaWei
 * 
 */
public class Jarvis {

	/**
	 * 逆时针链
	 */
	private LinkedList<Point> resultChain = new LinkedList<Point>();

	private List<Point> list = new ArrayList<Point>();

	/**
	 * 构造方法，传入节点列表，调用scan方法返回凸包节点 注意：传入的列表最好事先去重
	 * 
	 * @param list
	 */
	public Jarvis(List<Point> list) {
		this.resultChain.clear();
		this.list.clear();
		this.list.addAll(list);
		Collections.sort(this.list);
	}

	/**
	 * 返回凸包节点，结果为链表 起点是X轴，Y轴最小的点
	 * 
	 * @return 凸包点(链表)
	 */
	public LinkedList<Point> scan() {
		// 拿到p0点
		Point p0 = list.get(0);
		// 拿到p1点
		Point p1 = getFirstPoint(p0);
		// 结果链表中加入p0与p1
		resultChain.addLast(p0);
		resultChain.addLast(p1);

		Point temp = null;
		while (!p0.equals(temp)) {
			temp = getNext();
			resultChain.addLast(temp);
		}
		resultChain.removeLast();
		return resultChain;
	}

	/**
	 * 使用p0点找到凸包的第二个点
	 * 
	 * @param p0
	 * @param list
	 * @return
	 */
	private Point getFirstPoint(Point p0) {
		Point result = null;
		// 没有任何参考点，因此选择水平向量作为参考
		Point tempPoint = new Point(p0.x + 1, p0.y);
		double angle = Double.NEGATIVE_INFINITY;
		// 分别计算剩余点与水平向量的旋转关系
		for (Point point : list) {
			// 计算向量p0->point与p0->tempPoint的叉乘,若t为负值,则p0->point在p0->tempPoint的逆时针方向
			double t = calculateCross(p0, point, p0, tempPoint);
			// 当t<0 则表示p0->point向量在水平向量的逆时针方向
			// t==0则表示共线，此时根据余弦值判断是180°还是0°
			if (t <= 0) {
				// temp 为两个向量之间的余弦值，值越大，则角度越小
				double temp = calculateAngle(p0, tempPoint, p0, point);
				if (temp > angle) {
					angle = temp;
					result = point;
				}
			}
		}
		return result;
	}

	/**
	 * 每次从以确定为凸包的节点中取出最后一条边的向量 计算剩余点与该向量的旋转方向与余弦值 返回逆时针旋转，且余弦值最大的点
	 * 
	 * @return
	 */
	private Point getNext() {
		// 返回的结果，凸包点
		Point result = null;
		// 标示，标记此次凸包点的搜索有没有共线点的出现
		boolean flag = false;
		// 此时凸包点集合中至少存在两个点，取出最新的两个点
		Point p1 = resultChain.pollLast();
		Point p2 = resultChain.pollLast();
		// 用于保存最大的余弦值
		double cos = Double.NEGATIVE_INFINITY;
		// 用于保存最小的两点间距离，处理共线问题
		double distance = Double.POSITIVE_INFINITY;
		// 分别计算剩余点与最新生成的凸包边的旋转关系
		for (Point point : list) {
			// 计算向量p1->point与p2->p1的叉乘,若t为负值,则p1->point在p2->p1的逆时针方向
			double t = calculateCross(p1, point, p2, p1);
			// 当t<0 则表示p1->point向量在p2->p1向量的逆时针方向
			// t==0则表示共线，此时根据余弦值判断是180°还是0°
			if (t <= 0) {
				// temp 为两个向量之间的余弦值，值越大，则角度越小
				// 当两个向量共线时，tempCos=1
				double tempCos = calculateAngle(p2, p1, p1, point);
				// 当tempCos大于目前找到的最大余弦值
				// 同时tempCos < 1 即 p1->point向量与p2->p1向量不共线
				// 同时之前没有发现过共线的点
				if (tempCos > cos && tempCos < 1 && !flag) {
					cos = tempCos;
					result = point;
				}
				// 否则如果tempCos为1，即达到余弦最大值，表明 p1->point向量与p2->p1向量夹角为0°
				// 即 p1->point向量与p2->p1向量共线
				else if (tempCos == 1){
					// 出现共线点，置flag = true
					flag = true;
					// 计算point到p1点的距离
					double tempDistance = calculateDistance(p1,point);
					// 如果point到p1点的距离为已知共线点中发现的最小距离
					if (tempDistance<distance){
						// 更新最小距离
						distance = tempDistance;
						// 指定结果为此最小距离共线点
						result = point;
					}
				}
			}
		}
		resultChain.addLast(p2);
		resultChain.addLast(p1);
		return result;
	}

	/**
	 * 计算向量startPoint1->point1与向量startPoint2->point2之间的叉积
	 * (x1-x0)*(y2-y0)-(x2-x0)*(y1-y0)
	 * 
	 * @param startPoint1
	 * @param endPoint1
	 * @param startPoint2
	 * @param endPoint2
	 * @return 为正，则startPoint1->point1在startPoint2->point2的顺时针方向
	 */
	private double calculateCross(Point startPoint1, Point endPoint1,
			Point startPoint2, Point endPoint2) {
		double result = (endPoint1.x - startPoint1.x)
				* (endPoint2.y - startPoint2.y) - (endPoint2.x - startPoint2.x)
				* (endPoint1.y - startPoint1.y);
		return result;
	}

	/**
	 * 计算两个向量A,B的余弦值
	 * 
	 * @param startPoint1
	 *            向量A的起点
	 * @param endPoint1
	 *            向量A的终点
	 * @param startPoint2
	 *            向量B的起点
	 * @param endPoint2
	 *            向量B的终点
	 * @return 向量的余弦值
	 */
	private double calculateAngle(Point startPoint1, Point endPoint1,
			Point startPoint2, Point endPoint2) {
		// 计算两个向量(x,y)
		double x1 = endPoint1.x - startPoint1.x;
		double y1 = endPoint1.y - startPoint1.y;
		double x2 = endPoint2.x - startPoint2.x;
		double y2 = endPoint2.y - startPoint2.y;

		double up = x1 * x2 + y1 * y2;
		double down = Math.sqrt(x1 * x1 + y1 * y1)
				* Math.sqrt(x2 * x2 + y2 * y2);
		return up / down;
	}

	/**
	 * 计算两点之间的距离
	 * @param startPoint
	 * @param endPoint
	 * @return
	 */
	private double calculateDistance(Point startPoint, Point endPoint) {
		double x = endPoint.x - startPoint.x;
		double y = endPoint.y - startPoint.y;
		return Math.sqrt(x * x + y * y);

	}

	public static void main(String[] args) {
		List<Point> list = new ArrayList<Point>();
		Point p0 = new Point(102, 30);
		Point p1 = new Point(104, 32);
		Point p2 = new Point(103, 33);
		Point p3 = new Point(101, 31);
		list.add(p0);
		list.add(p1);
		list.add(p2);
		list.add(p3);

		Point p5 = new Point(103, 30);
		Point p6 = new Point(102.1, 30);
		Point p7 = new Point(102.2, 30);

		list.add(p5);
		list.add(p6);
		list.add(p7);

		Jarvis j = new Jarvis(list);
		LinkedList<Point> result = j.scan();

		for (Point p : result) {
			System.out.println(p);
		}
	}
}
