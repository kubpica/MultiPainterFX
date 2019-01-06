package pl.jpranica.multipainterfx;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;

import java.util.LinkedList;
import java.util.ListIterator;

public class ChronologicalCanvas {
    private LinkedList<CanvasHistoricalPoint> history;
    private Canvas canvas;
    private int toFullRip;
    private int withoutFullRip;

    public ChronologicalCanvas(Canvas c){
        this(c, 10);
    }

    public ChronologicalCanvas(Canvas c, int toFullRip){
        this.canvas = c;
        this.toFullRip = toFullRip;
        this.history = new LinkedList<>();
    }

    public void paint(Brushstroke bs){
        ListIterator<CanvasHistoricalPoint> itr = history.listIterator();

        while(true) {
            if (!itr.hasNext()) {
                itr.add(bs);
                bs.recreate(canvas.getGraphicsContext2D());
                break;
            }

            CanvasHistoricalPoint elementInList = itr.next();
            if (elementInList.compareTo(bs) > 0) {
                itr.previous();
                itr.add(bs);
                while(!itr.previous().isFullRip());
                itr.next().recreate(canvas.getGraphicsContext2D());
                while(itr.hasNext()){
                    CanvasHistoricalPoint p = itr.next();
                    if(p.isFullRip()){
                        itr.set(new CanvasRip(canvas));
                    }else {
                        p.recreate(canvas.getGraphicsContext2D());
                    }
                }
                break;
            }
        }

        withoutFullRip++;
        if(withoutFullRip>=toFullRip){
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    itr.add(new CanvasRip(canvas));
                }
            });
            withoutFullRip = 0;
        }
    }
}
