package com.abg.wordfinder.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.MotionEventCompat;

import com.abg.wordfinder.R;
import com.abg.wordfinder.SoundManager;
import com.abg.wordfinder.model.Cell;
import com.abg.wordfinder.model.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WordSearchView extends View {

    private int rows;
    private int columns;
    private int width;
    private boolean isGrid;

    private char[][] letters;
    private List<Word> words;

    private Cell[][] cells;
    private Cell cellDragFrom, cellDragTo;
    private final List<Cell> cellList = new ArrayList<>();
    private int color; //highlighter random color
    private final int colorTextMatch;
    private final int colorText;
    private final int hintColor;
    private int hintsCount = 0;

    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaintMatch = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint highlighterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint hintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint gridLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Word hintWord;

    private OnWordSearchedListener onWordSearchedListener;
    private int wordsSearched = 0;

    public WordSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        @SuppressLint("CustomViewStyleable") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomViewWordSearch, 0, 0);
        colorTextMatch = typedArray.getColor(R.styleable.CustomViewWordSearch_colorTextMatch, 0xFFFFFFFF);
        colorText = typedArray.getColor(R.styleable.CustomViewWordSearch_colorText, 0xcc000000);
        hintColor = typedArray.getColor(R.styleable.CustomViewWordSearch_hintColor, 0x220098FF);
        init();
    }

    public int getHintsCount() {
        return hintsCount;
    }

    public void setGrid(boolean grid) {
        isGrid = grid;
        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int pointerIndex = MotionEventCompat.getActionIndex(event);
        final float x = MotionEventCompat.getX(event, pointerIndex);
        final float y = MotionEventCompat.getY(event, pointerIndex);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            cellDragFrom = getCell(x, y);

            color = Color.argb(200, (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
            highlighterPaint.setColor(color);

            cellDragTo = cellDragFrom;
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            Cell cell = getCell(x, y);
            if (cell != null && isFromToValid(cellDragFrom, cell)) {
                SoundManager.playPik();
                cellDragTo = cell;
                invalidate();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            String word = getWordStr(cellDragFrom, cellDragTo);
            if (!highlightIfContain(word)) {
                for (Cell c : cellList) {
                    c.setColor(0);
                }
            }
            cellDragFrom = null;
            cellDragTo = null;
            invalidate();
            cellList.clear();
            return false;
        }
        return true;
    }

    public void refresh() {
        wordsSearched = 0;
        hintsCount = 0;
    }

    public void hint() {
        for (Word word : words) {
            if (!word.isHighlighted()) {
                highlightHint(word);
                hintsCount++;
                return;
            }
        }
    }

    public void setLetters(char[][] letters) {
        this.letters = letters;
        rows = letters.length;
        columns = letters[0].length;
        initCells();
        invalidate();
    }

    public interface OnWordSearchedListener {
        void wordFound(String word, int wordSearchCount);
    }

    public void setOnWordSearchedListener(OnWordSearchedListener onWordSearchedListener) {
        this.onWordSearchedListener = onWordSearchedListener;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    private boolean isFromToValid(Cell cellDragFrom, Cell cellDragTo) {

        if (cellDragFrom == null) {
            return false;
        }

        return (Math.abs(cellDragFrom.getRow() - cellDragTo.getRow()) == Math.abs(cellDragFrom.getColumn() - cellDragTo.getColumn()))
                || cellDragFrom.getRow() == cellDragTo.getRow() || cellDragFrom.getColumn() == cellDragTo.getColumn();
    }

    private Cell getCell(float x, float y) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].getRect().contains((int) x, (int) y)) {
                    return cells[i][j];
                }
            }
        }
        return null;
    }

    private void init() {

        textPaint.setSubpixelText(true);
        textPaint.setColor(colorText);
        textPaint.setTextSize(70);
        textPaint.setTextAlign(Paint.Align.LEFT);

        textPaintMatch.setSubpixelText(true);
        textPaintMatch.setColor(colorTextMatch);
        textPaintMatch.setTextSize(70);
        textPaintMatch.setTextAlign(Paint.Align.LEFT);

        highlighterPaint.setStyle(Paint.Style.STROKE);
        highlighterPaint.setStrokeWidth(110);
        highlighterPaint.setStrokeCap(Paint.Cap.ROUND);

        hintPaint.setStyle(Paint.Style.STROKE);
        hintPaint.setStrokeWidth(110);
        hintPaint.setStrokeCap(Paint.Cap.ROUND);
        hintPaint.setColor(hintColor);

        gridLinePaint.setStyle(Paint.Style.STROKE);
        gridLinePaint.setStrokeWidth(4);
        gridLinePaint.setStrokeCap(Paint.Cap.SQUARE);
        gridLinePaint.setColor(0x11000000);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (rows <= 0 || columns <= 0) {
            return;
        }

        // draw grid
        if (isGrid) {
            for (int i = 0; i < rows - 1; i++) {
                canvas.drawLine(0, cells[i][0].getRect().bottom, width, cells[i][0].getRect().bottom, gridLinePaint);
            }
            for (int i = 0; i < columns - 1; i++) {
                canvas.drawLine(cells[0][i].getRect().right, cells[0][0].getRect().top, cells[0][i].getRect().right, cells[columns - 1][0].getRect().bottom, gridLinePaint);
            }
        }

        // draw highlighter
        if (isFromToValid(cellDragFrom, cellDragTo)) {
            canvas.drawLine(cellDragFrom.getRect().centerX(), cellDragFrom.getRect().centerY(),
                    cellDragTo.getRect().centerX() + 1, cellDragTo.getRect().centerY(), highlighterPaint);
        }

        for (Word word : words) {
            if (word.isHighlighted()) {
                highlighterPaint.setColor(word.getColor());
                Rect from = cells[word.getFromRow()][word.getFromColumn()].getRect();
                Rect to = cells[word.getToRow()][word.getToColumn()].getRect();
                canvas.drawLine(from.centerX(), from.centerY(), to.centerX() + 1, to.centerY(), highlighterPaint);
                highlighterPaint.setColor(color);
            }
        }

        // draw letters
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                Cell temp = cells[i][j];

                String letter = String.valueOf(temp.getLetter());
                @SuppressLint("DrawAllocation") Rect textBounds = new Rect();
                textPaint.setColor(colorText);
                if (temp.getColorLetter() != 0) {
                    textPaint.setColor(temp.getColorLetter());
                }

                textPaint.getTextBounds(letter, 0, 1, textBounds);
                canvas.drawText(letter, temp.getRect().centerX() - (textPaint.measureText(letter) / 2),
                        temp.getRect().centerY() + ((float) textBounds.height() / 2), textPaint);
            }
        }


        if (hintWord != null) {
            Rect from1 = cells[hintWord.getFromRow()][hintWord.getFromColumn()].getRect();
            Rect to2 = cells[hintWord.getToRow()][hintWord.getToColumn()].getRect();
            canvas.drawLine(from1.centerX(), from1.centerY(), to2.centerX() + 1, to2.centerY(), hintPaint);
            hintWord = null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);
        int d = Math.min(measuredWidth, measuredHeight);
        setMeasuredDimension(d, d);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        initCells();
    }

    private void initCells() {
        if (rows <= 0 || columns <= 0) {
            return;
        }
        cells = new Cell[rows][columns];
        int rectWH = width / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new Cell(new Rect(j * rectWH, i * rectWH, (j + 1) * rectWH, (i + 1) * rectWH), letters[i][j], i, j);
            }
        }
    }

    private int measure(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            result = 100;
        } else {
            result = specSize;
        }
        return result;
    }

    private String getWordStr(Cell from, Cell to) {
        StringBuilder word = new StringBuilder();
        Cell cellTemp;
        boolean reverse = false;

        if (from.getRow() == to.getRow()) {
            int c = Math.min(from.getColumn(), to.getColumn());
            for (int i = 0; i < Math.abs(from.getColumn() - to.getColumn()) + 1; i++) {
                cellTemp = cells[from.getRow()][i + c];
                letterMatch(cellTemp);
                word.append(cellTemp.getLetter());
            }
        } else if (from.getColumn() == to.getColumn()) {
            int r = Math.min(from.getRow(), to.getRow());
            for (int i = 0; i < Math.abs(from.getRow() - to.getRow()) + 1; i++) {
                cellTemp = cells[i + r][to.getColumn()];
                letterMatch(cellTemp);
                word.append(cellTemp.getLetter());
            }
        } else {

            if (from.getRow() > to.getRow()) {
                Cell cell = from;
                from = to;
                to = cell;

                if (from.getColumn() > to.getColumn()) {
                 /* s - start, e - end touch
                  ------
                  ----e-
                  ---2--
                  --s---
                 */
                    reverse = true;
                }

                if (from.getColumn() < to.getColumn()) {
                    /* s - start, e - end touch
                    ------
                    --e---
                    ---2--
                    ----s-
                 */
                    reverse = true;
                }
            }

            for (int i = 0; i < Math.abs(from.getRow() - to.getRow()) + 1; i++) {
                int foo = from.getColumn() < to.getColumn() ? i : -i;
                cellTemp = cells[from.getRow() + i][from.getColumn() + foo];
                letterMatch(cellTemp);
                word.append(cellTemp.getLetter());
            }
        }
        return reverse ? word.reverse().toString() : word.toString();
    }

    private boolean highlightIfContain(String str) {
        for (Word word : words) {
            if (word.getWord().equals(str)) {
                cellList.clear();
                if (onWordSearchedListener != null) {
                    if (!word.isHighlighted()) {
                        wordsSearched++;
                        onWordSearchedListener.wordFound(str, wordsSearched);
                    }
                }
                word.setHighlighted(true);
                word.setColor(color);
                return true;
            }
        }
        return false;
    }

    private void highlightHint(Word word) {
        hintWord = word;
        invalidate();
    }

    private void letterMatch(Cell cell) {
        if (cell.getColorLetter() == 0) {
            cell.setColor(colorTextMatch);
            cellList.add(cell);
        }
    }
}

