package javafxmlapplication;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author alexey
 */

public class Circulo extends Circle {
    private final boolean rojo;
    private int tamaño_celda = 80;

    public Circulo(boolean rojo) {
        super(80 / 2);
        this.rojo = rojo;

        setCenterX(tamaño_celda / 2);
        setCenterY(tamaño_celda / 2);

        if (rojo) {
            setFill(Color.RED);
        } else {
            setFill(Color.YELLOW);
        }
    }

    public boolean esRoja() {
        return rojo;
    }
}
