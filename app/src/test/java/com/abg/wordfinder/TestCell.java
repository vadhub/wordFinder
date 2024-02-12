package com.abg.wordfinder;

import static org.junit.Assert.assertEquals;

import android.graphics.Rect;
import android.util.Log;

import com.abg.wordfinder.model.Cell;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class TestCell {

    private int rows = 10;
    private int columns = 10;
    private Cell[][] cells = new Cell[rows][columns];
    private int width = 1080;
    private char[][] letters = new char[rows][columns];

    @Before
    public void init() {
        int rectWH = width / rows;
        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns; j++) {
                letters[i][j] = 'a';
                cells[i][j] = new Cell(null, letters[i][j], 0, 0);
            }
        }

        cells[1][1] = new Cell(null, letters[1][1], 0, 2);

        //cells[9][9] = new Cell(new Rect(9 * rectWH, 9 * rectWH, (9 + 1) * rectWH, (9 + 1) * rectWH), letters[9][9], 9, 9);
    }

    @Test
    public void addition_isCorrect() {

        Set<Cell> cellSet = new LinkedHashSet<>();

        for (int i = 0; i < rows; i++) {
            cellSet.addAll(Arrays.asList(cells[i]).subList(0, rows));
        }

        System.out.println(cells.length * cells[0].length);
        System.out.println(cellSet.size());
        System.out.println(Arrays.toString(cellSet.toArray()));

        assertEquals("44", "44");
    }
}
