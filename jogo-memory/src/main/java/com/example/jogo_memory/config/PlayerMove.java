package com.example.jogo_memory.config;

import org.springframework.context.annotation.Configuration;

public class PlayerMove {
    private int row;  // A linha da peça selecionada
    private int col;  // A coluna da peça selecionada
    private int playerId;  // O ID ou índice do jogador que fez o movimento

    // Construtor
    public PlayerMove(int row, int col, int playerId) {
        this.row = row;
        this.col = col;
        this.playerId = playerId;
    }

    // Getters e Setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    // Sobrescrever o método toString para fácil leitura (opcional)
    @Override
    public String toString() {
        return "PlayerMove{" +
                "row=" + row +
                ", col=" + col +
                ", playerId=" + playerId +
                '}';
    }
}


