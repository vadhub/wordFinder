package com.abg.wordfinder.model;

import android.graphics.Rect;

import java.util.Objects;

public class Cell {
    private Rect rect;
    private char letter;
    private int rowIndex, columnIndex;
    private int colorLetter; // color of letter if that highlight

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

    public void setColor(int color) {
        this.colorLetter = color;
    }

    public int getColorLetter() {
        return colorLetter;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "letter=" + letter +
                ", rowIndex=" + rowIndex +
                ", columnIndex=" + columnIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return letter == cell.letter && rowIndex == cell.rowIndex && columnIndex == cell.columnIndex && Objects.equals(rect, cell.rect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowIndex, columnIndex);
    }
}
