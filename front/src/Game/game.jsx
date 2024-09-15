import React, { useState, useEffect } from 'react';
import './styles.css';

function Game() {
  const [socket, setSocket] = useState(null);
  const [grid, setGrid] = useState(Array(5).fill(Array(5).fill(null)));
  const [revealed, setRevealed] = useState(Array(5).fill(Array(5).fill(false)));
  const [correctPairs, setCorrectPairs] = useState(Array(5).fill(Array(5).fill(false)));
  const [scores, setScores] = useState([0, 0]);
  const [currentPlayer, setCurrentPlayer] = useState(0);
  const [gameStarted, setGameStarted] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (socket) {
      socket.onopen = () => {
        console.log("Conexão WebSocket aberta");
      };

      socket.onmessage = (event) => {
        const gameState = JSON.parse(event.data);
        setGrid(gameState.grid);
        setRevealed(gameState.revealed);
        setScores(gameState.scores);
        setCorrectPairs(gameState.correctPairs);
        setCurrentPlayer(gameState.currentPlayer);

        if (gameState.awaitingSecondMove) {
          setMessage(`Jogador ${gameState.currentPlayer + 1}, selecione a segunda peça.`);
        } else {
          setMessage(`Jogador ${gameState.currentPlayer + 1}, é sua vez de jogar.`);
        }
      };

      socket.onclose = () => {
        console.log("Conexão WebSocket fechada");
        setSocket(null);
      };

      socket.onerror = (error) => {
        console.error("Erro na conexão WebSocket", error);
      };

      return () => {
        if (socket) {
          socket.close();
        }
      };
    }
  }, [socket]);

  const startGame = () => {
    if (!socket || socket.readyState === WebSocket.CLOSED) {
      const ws = new WebSocket('ws://localhost:8080/game');
      setSocket(ws);
      setGameStarted(true);
    }
  };

  const handleClick = (row, col) => {
    if (socket && socket.readyState === WebSocket.OPEN) {
      socket.send(JSON.stringify({ row, col }));
      console.log(row, col);
    } else {
      console.error("A conexão WebSocket não está aberta. Tentando reconectar...");
      startGame();
    }
  };

  return (
    <div>
      <button onClick={startGame} disabled={gameStarted}>Iniciar Jogo</button>
      <div className="grid">
        {grid.map((row, rowIndex) => (
          <div key={rowIndex} className="row">
            {row.map((cell, colIndex) => (
              <div
                key={colIndex}
                className={`cell ${revealed[rowIndex][colIndex] ? 'revealed' : ''} ${correctPairs[rowIndex][colIndex] ? 'correct' : ''}`}
                onClick={() => handleClick(rowIndex, colIndex)}
              >
                {revealed[rowIndex][colIndex] ? cell : "?"}
              </div>
            ))}
          </div>
        ))}
      </div>
      <div>Jogador atual: Jogador {currentPlayer + 1}</div>
      <div>{message}</div>
      <div>Pontuações: Jogador 1 - {scores[0]}, Jogador 2 - {scores[1]}</div>
    </div>
  );
}

export default Game;
