import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class PointUtils{
	/**
	 * Returns the point with the largest Y value. If there are multiple such
	 * points, just chooses one arbitrarily.
	 */



	public static Point highestPoint(List<Point> points) {

		Iterator<Point> it = points.iterator();
		int index = -1;
		Point temp = points.get(0);

		while(it.hasNext()){
			if(temp.y < points.get(index + 1).y){
				temp = points.get(index + 1);
			}
			index++;
			it.next();
		}


		return temp;

	}
	/**
	 * Returns a new point that is the centroid of all the given points. A
	 * centroid has an X value that is the average of all the given points' X
	 * values, and a Y value that is the average of all the given points' Y
	 * values.
	 */
	public static Point centroid(List<Point> points) {
		Iterator<Point> it = points.iterator();
		int index = 0;
		int xAve = 0;
		int yAve = 0;

		while(it.hasNext()){
			xAve += points.get(index).x;
			yAve += points.get(index).y;
			index++;
			it.next();
		}

		xAve /= points.size();
		yAve /= points.size();

		return new Point(xAve, yAve);
	}

	public static void main(String[] args) {
		List<Point> points = new LinkedList<>();
		points.add(new Point(1, 1));
		points.add(new Point(1, 2));
		points.add(new Point(5, 7));
		points.add(new Point(5, 5));
		points.add(new Point(5, 9));



//		/* Should be java.awt.Point[x=3,y=3] or java.awt.Point[x=1,y=3] */
		System.out.println(highestPoint(points));
//
//		 Should be java.awt.Point[x=2,y=2]
		System.out.println(centroid(points));

//		points = new LinkedList<>();
//		points.add(new Point(1, 1));
//		points.add(new Point(1, -1));
//		points.add(new Point(-2, -4));
//		points.add(new Point(5, 5));
//		points.add(new Point(-1, 1));
//		points.add(new Point(-1, -1));

		/* Should be java.awt.Point[x=1,y=1] or java.awt.Point[x=-1,y=1] */
//		System.out.println(highestPoint(points));
//
//		// Should be java.awt.Point[x=0,y=0]
//		System.out.println(centroid(points));
	}



}
