package pl.jpranica.multipainterfx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.time.Instant;

public class CanvasRip implements CanvasHistoricalPoint {
    private Image rip;
    private Instant date;

    public CanvasRip(Canvas canvas){
        this.rip = canvas.snapshot(null, null);
        this.date = Instant.now();
    }

    @Override
    public void recreate(GraphicsContext gc) {
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
}
