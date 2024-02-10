package com.abg.wordfinder.model;

import androidx.annotation.NonNull;

public class Word {

    private String word;
    private boolean highlighted = false;
    private int fromRow, fromColumn, toRow, toColumn;

    public Word(String word, boolean highlighted, int fromRow, int fromColumn, int toRow, int toColumn) {
        this.word = word;
        this.highlighted = highlighted;
        this.fromRow = fromRow;
        this.fromColumn = fromColumn;
        this.toRow = toRow;
        this.toColumn = toColumn;
    }

    public Word(String word, int fromRow, int fromColumn, int toRow, int toColumn) {
        this.word = word;
        this.fromRow = fromRow;
        this.fromColumn = fromColumn;
        this.toRow = toRow;
        this.toColumn = toColumn;
    }

    public int getFromRow() {
        return fromRow;
    }

    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getFromColumn() {
        return fromColumn;
    }

    public void setFromColumn(int fromColumn) {
        this.fromColumn = fromColumn;
    }

    public int getToRow() {
        return toRow;
    }

    public void setToRow(int toRow) {
        this.toRow = toRow;
    }

    public int getToColumn() {
        return toColumn;
    }

    public void setToColumn(int toColumn) {
        this.toColumn = toColumn;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @NonNull
    @Override
    public String toString() {
        return "word: '" + word + '\'' +
                " start: [" + fromRow + ", " + fromColumn +"]; "+
                "end: [" + toRow + ", " + toColumn + ']';
    }
}

