package com.abg.wordfinder;

public class WordField {

    public static char[][] get2dWordField(int size) {
        char[][] field = new char[size][size];
        for (int r = 0; r < size; r++){
            for (int c = 0; c < size; c++){
                int number = (int) (Math.random() * 26) + 65;  //just move this line
                field[r][c] = (char) number;
                System.out.print(field[r][c] + " ");
            }//inner for loop
            System.out.println();
        }//outer for loop

        return field;
    }

//    public static char[][] placeWordInField() {
//        placeWordHorizontally();
//    }

    public static char[][] placeWordHorizontally(char[][] board, String word, int x, int y) {
        for (int i = 0; i < word.length(); i++) {
            if (y + i >= board[x].length) {
                // fail ... edge of board
            } else {
                board[x][y + i] = word.charAt(i);
            }
        }

        return board;
    }

    public static char[][] placeWordVertically(char[][] board, String word, int x, int y) {
        for (int i = 0; i < word.length(); i++) {
            if (x + i >= board[x].length) {
                // fail ... edge of board
            } else {
                board[x + i][y] = word.charAt(i);
            }
        }

        return board;
    }
}
