# Client-Server Chess Game

## Overview

This repository contains a full-featured Java-based chess application, structured as a Maven project with three modules:

- **client**: A Swing-based GUI client built with an MVC architecture.
- **engine**: A custom game package handling all game state and logic, with Stockfish integration and support for exporting to FEN and PGN.
- **server**: A WebSocket server (GlassFish) currently exposing a ping endpoint; future support for a lobby and matchmaking is planned.

All core code (excluding third-party dependencies) is original. Assets (images, sounds, fonts) are sourced from Chess.com.

![menu](https://github.com/user-attachments/assets/e9fd6c83-a2ed-4102-beaf-36b58ff42f3b)
![wood](https://github.com/user-attachments/assets/901314c3-52d1-471f-bc16-afe17d56f145)
![settings](https://github.com/user-attachments/assets/45268e60-93a5-4b84-b28e-f8d5d51ff598)
![icy](https://github.com/user-attachments/assets/0a293072-e3c8-4b8d-a453-26d66ea47a50)
![metal](https://github.com/user-attachments/assets/b753e8a2-4253-46c0-83bd-1a3e9bdd3f06)

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Future Work](#future-work)
- [Contributing](#contributing)
- [Assets](#assets)
- [External Software](#external-software)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Features

- **Client**
    - Swing-based GUI with intuitive, responsive board interaction.
    - MVC layout for clear separation of concerns.
    - Local play against bots and a solo mode where the board flips each move.
    - Settings system (via `settings.json`) for themes, usernames, and other preferences.
    - Custom `AssetManager` centralizes loading of images, sounds, fonts, and colors.
- **Engine**
    - Full game state management and rule enforcement.
    - Integration with Stockfish for AI opponent functionality.
    - Export to FEN and PGN formats for game analysis and sharing.
    - Comprehensive unit tests using JUnit Jupiter.
- **Server**
    - GlassFish-based WebSocket endpoint (`/ping`) for connectivity checks.
    - JSON serialization/deserialization via Gson for client–server communication.
    - Future support for lobbies, matchmaking, and multi-player game sessions.

## Architecture

The project is organized into three Maven modules under a single multi-module parent:

```
Chess/
├── client/
├── engine/
└── server/
```

- **client**: Implements the GUI front end. Uses Swing and follows MVC patterns.
- **engine**: Implements chess rules, game flow, and Stockfish integration.
- **server**: Hosts WebSocket endpoints and manages online play logic.

## Installation

### Prerequisites

- Java 17 or later (JDK)

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/gmnate6/Chess.git
   cd Chess
   ```

2. Linux & Mac Only: Make wrapper and stockfish executable
   ```bash
   chmod +x ./mvnw
   find stockfish/ -type f -exec chmod +x {} \;
   ```

3. Install via Maven Wrapper:
   ```bash
   ./mvnw clean install
   ```

## Usage

### Running the Client

```bash
./mvnw -pl client exec:java
```

### Running the Server

```bash
./mvnw -pl server exec:java
```

## Configuration

Client settings are stored in `settings.json` in the working directory. You can customize:

- Username
- Avatar
- Themes
- Server URL

Example:

```json
{
  "username": "Guest",
  "avatar": "Default",
  "theme": "wood",
  "serverURL": "ws://localhost:8080"
}
```

## Running Tests

Unit tests are provided for the engine module:

```bash
./mvnw test
```

Tests for the client and server modules are planned for future releases.

## Future Work

- Implement online lobby, matchmaking, and game session management in the server.
- Add unit and integration tests for client and server modules.
- Expand theme support and UI customization options.
- Improve engine heuristics and performance tuning.

## Contributing

Contributions are welcome! Feel free to submit issues or pull requests. Please ensure any new code is covered by appropriate tests and follows existing style conventions.

## Assets

All images, sounds, and fonts are used under Chess.com’s asset guidelines and sourced directly from Chess.com.

## External Software

- This project integrates [Stockfish](https://stockfishchess.org/), a strong open-source chess engine licensed under the GPL.
- Stockfish is developed by the open-source community and is one of the strongest chess engines in the world.

## License

This project is licensed under the [MIT License](LICENSE).

## Acknowledgments

- Chess.com for providing high-quality chess assets.

