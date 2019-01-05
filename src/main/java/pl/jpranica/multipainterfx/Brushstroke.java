package pl.jpranica.multipainterfx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.io.Serializable;
import java.util.LinkedList;

class Position implements Serializable {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

public class Brushstroke implements Serializable {
    private SerializableColor paint;
    private double size;
    private Position startPoint;
    private LinkedList<Position> path = new LinkedList<Position>();
    private Position endPoint;

    public Brushstroke(int x, int y, SerializableColor paint, double size){
        startPoint = new Position(x, y);
        endPoint = startPoint;
        this.paint = paint;
        this.size = size;
    }

    public void pushPoint(int x, int y){
        int xChange = x-endPoint.getX();
        int yChange = y-endPoint.getY();
        path.add(new Position(xChange, yChange));
        endPoint = new Position(x, y);
    }

    public void recreate(GraphicsContext g){
        g.setFill(paint.getFXColor());

        double x = startPoint.getX() - size / 2;
        double y = startPoint.getY() - size / 2;
        g.fillRect(x, y, size, size);

        for(Position p : path){
            x += p.getX();
            y += p.getY();
            g.fillRect(x, y, size, size);
        }
    }
}
