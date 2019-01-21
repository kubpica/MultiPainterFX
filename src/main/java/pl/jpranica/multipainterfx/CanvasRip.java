package pl.jpranica.multipainterfx;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.time.Instant;

public class CanvasRip implements CanvasHistoricalPoint, Runnable {
    private Image rip;
    private Instant date;
    private Canvas canvas;

    public CanvasRip(Canvas canvas){
        this.canvas = canvas;
        this.date = Instant.now();
        Platform.runLater(this);
    }

    @Override
    public void recreate(GraphicsContext gc) {
        while(rip==null);
        gc.drawImage(rip, 0, 0);
    }

    @Override
    public boolean isFullRip() {
        return true;
    }

    @Override
    public Instant getDate() {
        return date;
    }

    @Override
    public void run(){
        this.rip = canvas.snapshot(null, null);
        canvas = null;
    }
}
