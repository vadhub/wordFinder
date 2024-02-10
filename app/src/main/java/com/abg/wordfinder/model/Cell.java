package com.abg.wordfinder.model;

import android.graphics.Rect;

public class Cell {
    private Rect rect;
    private char letter;
    private int rowIndex, columnIndex;

    public Cell(Rect rect, char letter, int rowIndex, int columnIndex) {
        this.rect = rect;
        this.letter = letter;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }

    public int getRow() {
        return rowIndex;
    }

    public void setRow(int row) {
        this.rowIndex = row;
    }

    public int getColumn() {
        return columnIndex;
    }

    public void setColumn(int column) {
        this.columnIndex = column;
    }
}
