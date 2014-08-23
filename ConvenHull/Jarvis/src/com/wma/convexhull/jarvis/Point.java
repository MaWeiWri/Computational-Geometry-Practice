package com.wma.convexhull.jarvis;

public class Point implements Comparable<Point> {

	/**
	 * X轴坐标
	 */
	double x;
	/**
	 * Y轴坐标
	 */
	double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}


	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("X:" + this.x + "\t"
				+ "Y:" + this.y + "\n");
		return sb.toString();
	}

	public int hashCode() {
		int hash = (int) (this.x + this.y) & 0xFFFFFF;
		return hash;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (getClass() != other.getClass()) {
			return false;
		}
		Point otherObject = (Point) other;
		if (this.x == otherObject.x
				&& this.y == otherObject.y) {
			return true;
		}
		return false;
	}

	/**
	 * 选择第一个点时，需要对坐标点进行排序，先按Y轴坐标由小到大排，再按X轴坐标由小到大排序，
	 * 此时，第一个点(X最小,Y最小)必定在凸包上，再根据Jarvis算法进行搜索
	 */
	@Override
	public int compareTo(Point o) {
		// 比较y轴，y轴坐标较小的排前面
		if (this.y < o.y) {
			return -1;
		}
		else if (this.y > o.y) {
			return 1;
		}
		// 比较x轴，x轴坐标较小的排前面
		else if (this.x < o.x) {
			return -1;
		}
		else if (this.x > o.x) {
			return 1;
		}
		// x,y相等，则返回0
		else {
			return 0;
		}
	}

}
