package pl.jpranica.multipainterfx;

import javafx.scene.canvas.GraphicsContext;

import java.time.Instant;

public interface CanvasHistoricalPoint extends Comparable<CanvasHistoricalPoint> {
    void recreate(GraphicsContext g);
    Instant getDate();

    boolean isFullRip();

    @Override
    default int compareTo(CanvasHistoricalPoint o) {
        return this.getDate().compareTo(o.getDate());
    }
}
