/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import javafx.scene.layout.GridPane;

/**
 *
 * @author jsole
 */
public class Utils {

    public static int columnCalc(GridPane grid, double x) {
        double offsetX = grid.getLayoutX(); // Offset inicial del GridPane
        double relativeX = x - offsetX;

        double gridWidth = grid.getWidth();
        int colCount = grid.getColumnCount();
        double celdaWidth = gridWidth / colCount;

        int columna = (int) Math.floor(relativeX / celdaWidth);
        columna = Math.max(0, Math.min(columna, colCount - 1));

        return columna;
    }



    public static int rowCalc(GridPane grid, double y) {
        double celdaHeight = (double) (grid.getHeight() / grid.getRowCount());
        return (int) Math.floor(y / celdaHeight);
    }

    public static int rowNorm(GridPane grid, int row) {
        int rowCount = grid.getRowCount();
        return (row + rowCount) % rowCount;
    }

    public static int columnNorm(GridPane grid, int col) {
        int colCount = grid.getColumnCount();
        return (col + colCount) % colCount;
    }
}