package com.wma.convexhull.jarvis;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 凸包节点的搜索和面积的计算
 * 
 * @author MaWei
 * 
 */
public class ConvexHull {
	
	/**
	 * 计算凸包围成图形的面积
	 * @param points 凸包节点(注意使用scan方法后不要打乱顺序)
	 * @return 面积
	 */
	public double getArea(List<Point> points){
		double area = 0;
		int size = points.size();
		Point p0 = points.get(0);
		for (int i = 0 ; i < size-2;i++){
			
			Point p1 = points.get(i+1);
			Point p2 = points.get(i+2);
			
			double temp = calculateArea(p0,p1,p2);
			area += temp;
		}
		return area;
	}
	
	/**
	 * 传入节点List<Point> 输出凸包节点
	 * @param points 输入节点
	 * @return 输出节点
	 */
	public List<Point> getConvexHull(List<Point> points){
		Set<Point> set = new HashSet<Point>();
		List<Point> list = new ArrayList<Point>();
		set.addAll(points);
		list.addAll(set);
		Jarvis jarvis = new Jarvis(list);
		List<Point> result = jarvis.scan();
		return result;
	}
	
	/**
	 * 计算p0,p1,p2围成的三角形面积
	 * @param p0
	 * @param p1
	 * @param p2
	 */
	private static double calculateArea(Point p0,Point p1,Point p2){
			double x1 = p1.x - p0.x;
			double y1 = p1.y - p0.y;
			double x2 = p2.x - p0.x;
			double y2 = p2.y - p0.y;
			double area = x1 * y2 - x2 * y1;
			return Math.abs(area)/2;
	}
	
	public static void main(String[] args){
		LinkedList<Point> points = new LinkedList<Point>();
		
		Point p0 = new Point(0,0);
		Point p1 = new Point(4,4);
		Point p2 = new Point(2,2);
		Point p3 = new Point(0,2);
		Point p4 = new Point(4,1);
		Point p5 = new Point(4,2);
		Point p6 = new Point(4,3);
		Point p7 = new Point(2,3);
//		Point p4 = new Point();
		
//		Point p0 = new Point(102, 30);
//		Point p1 = new Point(104, 32);
//		Point p2 = new Point(103, 33);
//		Point p3 = new Point(101, 31);
//		Point p5 = new Point(103, 30);
//		Point p6 = new Point(102.1, 30);
//		Point p7 = new Point(102.2, 30);
		
		points.addLast(p0);
		points.addLast(p1);
		points.addLast(p2);
		points.addLast(p3);
		points.addLast(p4);
		points.addLast(p5);
		points.addLast(p6);
		points.addLast(p7);
		
		Collections.shuffle(points);
		ConvexHull c = new ConvexHull();
		List<Point> list = c.getConvexHull(points);
		
		for (Point p:list){
			System.out.println(p);
		}
		
		double area = c.getArea(list);
		System.out.println(area);
	}

}
