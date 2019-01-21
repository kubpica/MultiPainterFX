package pl.jpranica.multipainterfx;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.time.Instant;
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

public class Brushstroke implements Serializable, CanvasHistoricalPoint {
    private SerializableColor paint;
    private double size;
    private Position startPoint;
    private LinkedList<Position> path = new LinkedList<Position>();
    private Position endPoint;
    private Instant date;

    public Brushstroke(int x, int y, SerializableColor paint, double size){
        startPoint = new Position(x, y);
        endPoint = startPoint;
        this.paint = paint;
        this.size = size;
        this.date = Instant.now();
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public void pushPoint(int x, int y){
        int xChange = x-endPoint.getX();
        int yChange = y-endPoint.getY();
        path.add(new Position(xChange, yChange));
        endPoint = new Position(x, y);
    }

    @Override
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

    @Override
    public boolean isFullRip(){
        return false;
    }
}
