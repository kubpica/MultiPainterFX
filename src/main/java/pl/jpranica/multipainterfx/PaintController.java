package pl.jpranica.multipainterfx;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PaintController implements VistaContainable {
    @FXML private Canvas canvas;
    @FXML private ColorPicker colorPicker;
    @FXML private TextField brushSize;
    @FXML private CheckBox eraser;
    private VistaContainer parent;
    private Brushstroke bs;
    private GraphicsContext gc;
    private ServerConnection connection;
    private Image prevImage;

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public PaintController(VistaContainer parent, ServerConnection connection){
        this.connection = connection;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VistaNavigator.VISTA_PAINT));
        loader.setController(this);
        try {
            parent.setVista(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.init(parent);
    }

     @Override
    public void init(VistaContainer parent) {
        this.parent = parent;

        brushSize.textProperty().addListener(new ChangeListener<String>() {
             @Override
             public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                 if (!newValue.matches("\\d*")) {
                     brushSize.setText(newValue.replaceAll("[^\\d]", ""));
                 }
             }
        });

        colorPicker.setValue(Color.RED);

        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        canvas.setOnMousePressed(e -> {
            //System.out.println("pressed");

            Color c;
            if(eraser.isSelected())
                c = Color.WHITE;
            else
                c = colorPicker.getValue();

            bs = new Brushstroke((int)e.getX(), (int)e.getY(), new SerializableColor(c), Double.parseDouble(brushSize.getText()));

            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;


            gc.setFill(colorPicker.getValue());
            gc.fillRect(x, y, size, size);
        });

        canvas.setOnMouseDragged(e -> {
            //System.out.println("test " + e.getX() + " " + e.getY());
            bs.pushPoint((int)e.getX(), (int)e.getY());

            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            Color c;
            if(eraser.isSelected())
                c = Color.WHITE;
            else
                c = colorPicker.getValue();

            gc.setFill(c);
            gc.fillRect(x, y, size, size);
        });

        canvas.setOnMouseReleased(e -> {
            //System.out.println("released");
            try {
                connection.sendBrushstroke(bs);
            } catch (IOException ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
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