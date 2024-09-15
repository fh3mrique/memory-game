package com.example.jogo_memory.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GameState {
    private int[][] grid;
    private boolean[][] revealed;
    private int[] scores;
    private int currentPlayer;
    private int[] lastMove;
    private boolean awaitingSecondMove;

    public GameState() {
        // Mocking the grid with random pairs
        grid = new int[5][5];
        revealed = new boolean[5][5];
        scores = new int[]{0, 0};
        currentPlayer = 0;
        lastMove = new int[]{-1, -1}; // To track first piece
        awaitingSecondMove = false;

        // Preencher grid com números de pares
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            numbers.add(i);
            numbers.add(i);
        }
        numbers.add(-1); // Grid ímpar, adicionar uma peça "vazia" (ou carta especial)

        Collections.shuffle(numbers);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                grid[i][j] = numbers.remove(0);
            }
        }
    }

    public void processMove(PlayerMove move) {
        int row = move.getRow();
        int col = move.getCol();

        if (revealed[row][col]) {
            return; // Ignore se a peça já estiver revelada
        }

        if (!awaitingSecondMove) {
            // Primeira peça da jogada
            revealed[row][col] = true;
            lastMove = new int[]{row, col};
            awaitingSecondMove = true;
        } else {
            // Segunda peça da jogada
            revealed[row][col] = true;
            if (grid[row][col] == grid[lastMove[0]][lastMove[1]]) {
                // Peças iguais, incrementar pontuação
                scores[currentPlayer]++;
            } else {
                // Peças diferentes, ocultar ambas após um tempo
                awaitingSecondMove = false; // Permitir nova jogada
                revealed[lastMove[0]][lastMove[1]] = false;
                revealed[row][col] = false;
            }
            awaitingSecondMove = false;
            currentPlayer = (currentPlayer + 1) % 2; // Alterna jogador
        }
    }

    // Getters e setters
    public int[][] getGrid() { return grid; }
    public boolean[][] getRevealed() { return revealed; }
    public int[] getScores() { return scores; }
    public int getCurrentPlayer() { return currentPlayer; }
}


