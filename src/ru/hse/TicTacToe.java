package ru.hse;

import java.util.Scanner;

public class TicTacToe {

    private static final int BOARD_SIZE = 3;
    private static final Scanner scanner = new Scanner(System.in);

    private Symbol[][] board;
    private GameState state;
    private boolean isGoing;
    private Winner winner;

    public TicTacToe() {
        state = GameState.START;
        winner = Winner.UNDEFINED;
        isGoing = true;

        emptyBoard();
    }

    private void emptyBoard() {
        board = new Symbol[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = Symbol.EMPTY;
            }
        }
    }

    public void play() {
        while (isGoing) {
            switch (state) {
                case START -> {
                    System.out.println("Let's play!");
                    getNextState();
                }
                case TURN_X -> {
                    playerStep(Symbol.CROSS);
                }
                case TURN_O -> {
                    playerStep(Symbol.CIRCLE);
                }
                case END -> {
                    if (winner == Winner.DRAW) {
                        System.out.println("It's a draw!");
                    } else {
                        System.out.println("Congratz " + winner.name() + "!");
                    }
                    isGoing = false;
                }

            }
        }
    }

    private void getNextState() {
        if (state == GameState.START) {
            state = GameState.TURN_X;
            return;
        }

        if (winner != Winner.UNDEFINED) {
            state = GameState.END;
            return;
        }

        if (state == GameState.TURN_X) {
            state = GameState.TURN_O;
            return;
        }

        if (state == GameState.TURN_O) {
            state = GameState.TURN_X;
            return;
        }
    }

    private boolean checkDraw() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == Symbol.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private Symbol checkLineAndRow() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][0] != Symbol.EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }

            if (board[0][i] != Symbol.EMPTY && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }

        return null;
    }

    private Symbol checkDiagonals() {
        if (board[0][0] != Symbol.EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }

        if (board[0][2] != Symbol.EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }

        return null;
    }

    private Winner checkWinner() {

        Symbol winSymbol = checkLineAndRow();
        if (winSymbol != null) {
            return Winner.symbolToPlayer(winSymbol);
        }

        winSymbol = checkDiagonals();
        if (winSymbol != null) {
            return Winner.symbolToPlayer(winSymbol);
        }

        if (checkDraw()) {
            return Winner.DRAW;
        }

        return Winner.UNDEFINED;
    }

    private void playerStep(Symbol symbol) {
        System.out.println("Now it's " + symbol.name() + "'s turn");
        writeSymbol(getCoordinates(), symbol);
        printBoard();
        winner = checkWinner();
        getNextState();
    }

    private Coordinate getCoordinates() {
        System.out.println("Please input two numbers from 1 to 3 separated by space: row and column");

        int indexRow;
        int indexColumn;
        while (true) {
            indexRow = scanner.nextInt();
            indexColumn = scanner.nextInt();
            if (ifIndexOutOfBoard(indexRow) || ifIndexOutOfBoard(indexColumn)) {
                System.out.println("This place is out of board. Indexes must be from 1 to 3:");
                continue;
            }

            if (board[indexRow - 1][indexColumn - 1] != Symbol.EMPTY) {
                System.out.println("Cheater! This place has already taken. Try again");
                continue;
            }
            break;
        }

        return new Coordinate(indexRow - 1, indexColumn - 1);
    }

    private boolean ifIndexOutOfBoard(int index) {
        return index < 1 || index > BOARD_SIZE;
    }

    private void writeSymbol(Coordinate coordinate, Symbol symbol) {
        board[coordinate.x][coordinate.y] = symbol;
    }

    private void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j].symbolImg + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private enum Symbol {
        EMPTY('.'),
        CROSS('X'),
        CIRCLE('O');

        public final char symbolImg;

        Symbol(char symbolImg) {
            this.symbolImg = symbolImg;
        }
    }

    private enum GameState {
        START,
        TURN_X,
        TURN_O,
        END
    }

    private enum Winner {
        PLAYER_X,
        PLAYER_O,
        DRAW,
        UNDEFINED;

        private static Winner symbolToPlayer(Symbol symbol) {
            if (symbol == Symbol.CROSS) {
                return PLAYER_X;
            }

            if (symbol == Symbol.CIRCLE) {
                return PLAYER_O;
            }

            return null;
        }
    }

    private record Coordinate(int x, int y) {
    }
}
