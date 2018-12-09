package pl.jpranica.multipainterfx;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class PaintController implements VistaContainable {
    @FXML private Canvas canvas;
    @FXML private ColorPicker colorPicker;
    @FXML private TextField brushSize;
    @FXML private CheckBox eraser;
    private VistaContainer parent;
    private Brushstroke bs;
    private GraphicsContext gc;

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public PaintController(VistaContainer parent){
        this.parent = parent;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VistaNavigator.VISTA_PAINT));
        loader.setController(this);
        try {
            parent.setVista(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.init();
    }

    @Override
    public void setParent(VistaContainer parent) {
        this.parent = parent;
    }
    @Override
    public void init() {
        gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(e -> {
            System.out.println("pressed");
            bs = new Brushstroke((int)e.getX(), (int)e.getY(), colorPicker.getValue(),Double.parseDouble(brushSize.getText()));

            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if (eraser.isSelected()) {
                gc.clearRect(x, y, size, size);
            } else {
                gc.setFill(colorPicker.getValue());
                gc.fillRect(x, y, size, size);
            }
        });

        canvas.setOnMouseDragged(e -> {
            System.out.println("test " + e.getX() + " " + e.getY());
            bs.pushPoint((int)e.getX(), (int)e.getY());

            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if (eraser.isSelected()) {
                gc.clearRect(x, y, size, size);
            } else {
                gc.setFill(colorPicker.getValue());
                gc.fillRect(x, y, size, size);
            }
        });

        canvas.setOnMouseReleased(e -> {
            System.out.println("released");
            bs.recreate(gc);
        });

        System.out.println(canvas.getWidth() + " " + canvas.getHeight());
    }

    public void onSave() {
        try {
            Image snapshot = canvas.snapshot(null, null);

            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", new File("paint.png"));
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e);
        }
    }

    public void onExit() {
        Platform.exit();
    }
}