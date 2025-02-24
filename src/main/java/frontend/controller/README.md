# Controller Proposal:

## ~ Structure ~
- controller:
  - game:
    - `GameController`
    - `BoardController`
  - menu:
    - `OnlineMenuController`
    - `OfflineMenuController`
  - `MainController`

## ~ Responsibilities ~
1) GameController
   - Holds Game and BoardController instances
   - Looks at the big picture of the `GamePanel`
   - Talks to `GameModel` for back end access
2) BoardController
   - Looks at the smaller picture of just `BoardPanel`
   - Deals with `SquareButton` interactions
3) OnlineMenuController
   - Talks between `LobbyPanel` and server backend
   - Hosts and Joins games on the server
4) OfflineMenuController
   - Handles making local offline games
5) MainController
   - Brings all the controllers together