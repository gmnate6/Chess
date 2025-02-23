# Chess Engine and GUI Interface

This project is a chess engine built in Java featuring core functionalities for simulating chess games, including move validation, special rules handling (castling, en passant, etc.), and game state management via Forsyth–Edwards Notation (FEN). Additionally, it includes a basic command-line interface (CLI) for human players to interact with the engine along with a simple GUI interface.

## Features

- **Core Chess Engine**: Implements chess rules, piece movement, and game logic.
- **FEN Support**: Import and export game states using FEN strings.
- **Position Validation**: Ensures that moves are legal and blocks invalid moves.
- **Turn-Based Time Control**: A chess timer with time increments per move.
- **Special Chess Rules**:
    - Castling
    - En passant
    - Pawn promotion
- **Game State Management**: Handles draw conditions, resignation, and checkmate.
- **Command-Line Interface**: Play games interactively via the console by entering moves in algebraic notation (e.g., `e2 e4`).

## Modules and Classes

### Core Engine
- **`Game`**: Manages the overall game state, turn control, and win conditions.
- **`Board`**: Represents the 8x8 chessboard and handles piece placement, movement, and validation.
- **`Piece`**: An abstract class used to represent all chess pieces (e.g., `King`, `Queen`, `Rook`).
- **`Position`**: Represents files (columns) and ranks (rows) of the board, with utilities to convert between integer coordinates and algebraic notation (e.g., `e4`).
- **`Move`**: Represents a single move, encapsulating the initial and final positions and promotion (if applicable).

### Utilities
- **`Timer`**: Implements a turn-based timer with optional increments per move.
- **`FEN`**: Parses Forsyth–Edwards Notation strings and converts board states into FEN.
- **`PieceUtils`**: Utility for converting between characters (e.g., `K`, `q`) and piece objects, ensuring flexible piece handling.
- **`CastlingRights`**: Manages castling rights for both players and validates castling legality.

### Command-Line Interface
- **`CommandLineGame`**: A CLI-driven implementation for playing chess games. Allows users to:
    - Import a board state using FEN.
    - Play moves interactively in real-time.
    - Display the current game board and timer state.

## Getting Started

### Requirements
- **Java 21** or later.
- A terminal or IDE to run the command-line application.

### Running the Game
1. Compile the project:
    ```bash
    javac -d out src/**/*.java
    ```
2. Run the command-line application:
    ```bash
    java -cp out engine.CommandLineGame
    ```

### Usage Instructions
1. **Starting a New Game**: When prompted, choose whether to start with a new game or load an existing position using a FEN string.
2. **Playing Moves**: Enter moves in algebraic notation (e.g., `e2 e4`) during your turn. To view all possible moves for a piece, enter its position (e.g., `e2`).
3. **Ending the Session**: Press Enter without typing a move.

### Example Game
```text
Import FEN? (y, n): n

  r   .   b   q   k   b   n   r
  p   p   p   p   .   p   p   p
  .   .   .   .   .   .   .   .
  .   .   .   .   p   .   .   .
  .   .   .   .   P   .   .   .
  .   .   .   .   .   .   .   .
  P   P   P   P   .   P   P   P
  R   N   B   Q   K   B   N   R

White's Move: e2 e4
```

### Chess Notation
This implementation uses standard **algebraic chess notation** to describe board positions:
- Files (columns) are labeled `a` to `h`.
- Ranks (rows) are labeled `1` to `8`.
  For example:
- The starting position of White's king is `e1`.
- A pawn move from row 2 to row 4 in column `e` is represented as `e2 e4`.

## Project Structure

```plaintext
src/
├── engine/
│   ├── board/                 (Board and Position logic)
│   ├── pieces/                (Abstract and concrete classes for chess pieces)
│   ├── utils/                 (Utility classes like CastlingRights, FEN, Timer, etc.)
│   └── Game.java              (Core game logic)
├── tests/                     (Command-line game implementation)
├── utils/Color.java           (Enum for player colors: WHITE / BLACK)
```

## Example Code Highlights

### Position Class
Represents a square on the board. Includes conversion between coordinate-based and algebraic formats.
```java
Position a1 = new Position(0, 0); // 0th column, 0th row
System.out.println(a1.toAlgebraic()); // Output: "a1"

Position fromNotation = Position.fromAlgebraic("e4"); // "e4" -> (4, 3)
```

### FEN Utility
Provides easy board state saving and loading.
```java
FEN fen = FEN.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
```

## Future Features
- Implement GUI for easier interaction.
- Add support for PGN (Portable Game Notation) to save and replay games.
- Introduce advanced AI with selectable difficulty levels.

## License

This project is licensed under the **MIT License**. Feel free to use, modify, and distribute it for personal or educational purposes.

---

### Contributing
Contributions are welcome! If you find a bug, have suggestions, or want to improve this project, please feel free to open an issue or submit a pull request.
