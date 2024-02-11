package com.abg.wordfinder;

import com.abg.wordfinder.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordSearchGenerator {
    private final char[][] board;
    private final List<String> words;
    private final List<Word> coordinates;
    private final List<String> wordsApply;
    private final boolean[][] occupied;
    private final Listener listener;
    private final char[] russianAlphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя".toUpperCase().toCharArray();
    private final char[] englishAlphabet = "qwertyuiopasdfghjklzxcvbnm".toUpperCase().toCharArray();

    public WordSearchGenerator(int rows, int cols, List<String> words, Listener listener) {
        this.board = new char[rows][cols];
        this.words = words;
        this.occupied = new boolean[rows][cols];
        this.coordinates = new ArrayList<>(words.size());
        this.listener = listener;
        this.wordsApply = new ArrayList<>();
    }

    public void generateBoard(String locale) {
        Random random = new Random();

        long timeStart = System.currentTimeMillis();

        for (String word : words) {
            boolean wordPlaced = false;
            while (!wordPlaced) {
                int direction = random.nextInt(4); // 0 - горизонтально, 1 - вертикально, 2 - диагонально вниз, 3 - диагонально вверх
                int startRow = random.nextInt(board.length);
                int startCol = random.nextInt(board[0].length);

                if (direction == 0) {
                    if (canPlaceWord(word, startRow, startCol, 0, 1)) {
                        placeWord(word, startRow, startCol, 0, 1);
                        wordPlaced = true;
                    }
                } else if (direction == 1) {
                    if (canPlaceWord(word, startRow, startCol, 1, 0)) {
                        placeWord(word, startRow, startCol, 1, 0);
                        wordPlaced = true;
                    }
                } else if (direction == 2) {
                    if (canPlaceWord(word, startRow, startCol, 1, 1)) {
                        placeWord(word, startRow, startCol, 1, 1);
                        wordPlaced = true;
                    }
                } else {
                    if (canPlaceWord(word, startRow, startCol, -1, 1)) {
                        placeWord(word, startRow, startCol, -1, 1);
                        wordPlaced = true;
                    }
                }

                if (System.currentTimeMillis()-timeStart > 100) {
                    break;
                }
            }
        }
        listener.generated(wordsApply);
        fillEmptyCellsRandomly(random, locale);

    }

    public List<Word> getWords() {
        return coordinates;
    }

    public char[][] getBoard() {
        return board;
    }

    public void printBoard() {
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }

    private boolean canPlaceWord(String word, int row, int col, int rowDir, int colDir) {
        for (int i = 0; i < word.length(); i++) {
            int newRow = row + i * rowDir;
            int newCol = col + i * colDir;
            if (newRow < 0 || newRow >= board.length || newCol < 0 || newCol >= board[0].length ||
                    (occupied[newRow][newCol] && board[newRow][newCol] != word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private void fillEmptyCellsRandomly(Random random, String locale) {

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (!occupied[i][j]) {
                    board[i][j] = ' ';
//                    if (locale.equals("ru")) {
//                        board[i][j] = russianAlphabet[random.nextInt(russianAlphabet.length)];
//                    } else {
//                        board[i][j] = englishAlphabet[random.nextInt(englishAlphabet.length)];
//                    }
                }
            }
        }
    }

    private void placeWord(String word, int row, int col, int rowDir, int colDir) {

       word = word.toUpperCase();

        int rowEnd = 0;
        int colEnd = 0;

        for (int i = 0; i < word.length(); i++) {

            rowEnd = row + i * rowDir;
            colEnd = col + i * colDir;

            board[rowEnd][colEnd] = word.charAt(i);
            occupied[rowEnd][colEnd] = true;
        }

        wordsApply.add(word);
        coordinates.add(new Word(word, row, col, rowEnd, colEnd));

    }

    public interface Listener {
        void generated(List<String> words);
    }
}