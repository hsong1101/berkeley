/**
 * Created by hsong1101 on 6/20/2017.
 */

public class Path {

    Point currPoint = new Point();
    Point nextPoint;

    public Path(double x, double y){
        nextPoint = new Point(x,y);
    }


    public double getCurrX(){
        return currPoint.getX();
    }

    public double getCurrY(){
        return currPoint.getY();
    }

    public double getNextX(){
        return nextPoint.getX();
    }

    public double getNextY(){
        return nextPoint.getY();
    }

    public Point currentPoint(){
        return currPoint;
    }

    public void setCurrentPoint(Point point){
        currPoint = point;
    }

    public void iterate(double dx, double dy){
        currPoint.setX(nextPoint.getX());
        currPoint.setY(nextPoint.getY());
        nextPoint.setX(nextPoint.getX() + dx);
        nextPoint.setY(nextPoint.getY() + dy);

    }
}
