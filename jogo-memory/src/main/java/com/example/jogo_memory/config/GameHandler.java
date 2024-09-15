package com.example.jogo_memory.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameHandler extends TextWebSocketHandler {
    private List<WebSocketSession> sessions = new ArrayList<>();
    private GameState gameState;
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        if (sessions.size() == 2) {
            gameState = new GameState();  // Inicializa o estado do jogo
            startGame();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("Conexão WebSocket fechada: " + status);
    }

    private void startGame() throws Exception {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage("O jogo começou!"));
                session.sendMessage(new TextMessage(mapper.writeValueAsString(gameState)));
            }
        }
    }



    private void sendMessageToAll(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            } else {
                System.out.println("Sessão WebSocket fechada para o jogador.");
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        PlayerMove move = mapper.readValue(message.getPayload(), PlayerMove.class);
        gameState.processMove(move);

        // Enviar estado atualizado para todos os jogadores
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage(mapper.writeValueAsString(gameState)));
        }
    }
}

