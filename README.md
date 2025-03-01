# Chess Engine with Command-Line Interface

Welcome to the **Chess Engine with Command-Line Interface**, an advanced chess engine built entirely in Java. This program allows users to play a competitive game of chess directly from the command line and provides advanced features for analyzing and managing gameplay.

## Features
- **Command-Line Chess Gameplay**: Play chess against another player on a text-based interface.
- **Chess Engine Logic**: Implements full chess rules, including special moves such as castling, en passant, promotion, and checkmate conditions.
- **Game AI**: Includes a `RandomAI` to generate moves for basic automated gameplay or testing.
- **Move History Tracking**: Tracks full move history throughout the game and allows undos/redos.
- **Timer Support**: Optional timer configurations for timed games.
- **FEN and PGN Utilities**: Save and load game states with the Forsyth-Edwards Notation (FEN) and Portable Game Notation (PGN).
- **Custom Piece Logic**: Full implementation of all chess pieces (Pawn, King, etc.) with their respective rules and movement capabilities.
- **Chessboard Visualization**: Displays a text-based visualization of the board for ease of play.

---

## Getting Started

### Prerequisites
- **Java Development Kit (JDK)**: This project requires JDK 21 or later.
- A command-line terminal for running the program.

---

### Build and Run
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```
2. Build the project using the Maven Wrapper:
   ```bash
   mvnw clean install
   ```
3. Run the main application:
   ```bash
   mvnw exec:java -Dexec.mainClass="engine.CommandLineGame"
   ```

---

## Usage

1. Start the game by running the main program.
2. Players take turns entering moves in standard algebraic long notation (e.g., `e2e4`).
3. View the chessboard state and get feedback for illegal moves.
4. Save or load your game using FEN or PGN as needed.


**Controls**:
- Enter a valid move using standard algebraic notation.
- Press Enter with no input to exit the game.

---

## Key Classes

### Gameplay Core
- **`Game`**: Encapsulates the chess game logic, including rules enforcement and game state management.
- **`Board`**: Represents the chessboard. Manages all pieces, castling rights, en passant, and other board states.
- **`Piece` (Abstract)**: Base class for all chess pieces, including `King`, `Queen`, `Pawn`, etc.

### Input/Output
- **`CommandLineGame`**: Manages the game loop, user input, and board display.

### Utilities
- **`FEN`**: Used for saving or restoring game states in Forsyth-Edwards Notation.
- **`PGN`**: Used to record and replay games in Portable Game Notation.
- **`MoveUtils`**: Includes functionality for move validation, algebraic notation conversion, and more.

### AI
- **`RandomAI`**: Generates random, valid moves for automated gameplay or testing.

---

## Notable Features

### Move Validation
- Ensures moves follow chess rules, including specific logic for each type of piece.
- Supports special moves like castling, en passant, and pawn promotion.

### Time Management
- **`Timer`**: Implements time tracking for both players, including optional per-move increments.

### Save and Load
- Save or restore game states using FEN or fully record games with PGN, ensuring seamless resumption or analysis.

---

## Sample Code

### Creating a Chess Game
```java
// Initialize a new game with a timer
Timer timer = new Timer(10 * 60 * 1000, 5 * 1000); // 10 minutes + 5 sec increment
Game game = new Game(timer);

// Display the game state
System.out.println(game);

// Making a move
Position start = Position.fromAlgebraic("e2");
Position end = Position.fromAlgebraic("e4");
char promotionChar = '\0'; // No Promotion
Move move = new Move(start, end, '\0');

if (game.isMoveLegal(move)) {
    game.move(move);
}
```

### Using FEN
```java
// Save game to FEN
String fen = FEN.getFEN(game);
System.out.println("FEN: " + fen);

// Load FEN into a game
Game restoredGame = FEN.getGame(fen, null);
System.out.println(restoredGame.board);
```

---

## Future Enhancements
- **Smart AI**: Upgrade the AI to implement advanced chess engines like Stockfish-like logic.
- **GUI Version**: Extend the application with a graphical chess interface.
- **Multiplayer Mode**: Support online multiplayer gameplay.

---

## Acknowledgments
This project follows standard chess rules and integrates concepts from chess notation standards like FEN and PGN to provide a rich chess-playing experience.

Happy Chess Playing! ♟️