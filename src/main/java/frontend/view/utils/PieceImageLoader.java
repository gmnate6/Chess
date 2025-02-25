package frontend.view.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class PieceImageLoader {
    private static final Map<Character, BufferedImage> pieceFileMap = new HashMap<>();
    private final BufferedImage whiteImage;
    private final BufferedImage blackImage;
    private final BufferedImage moveHintImage;
    private final BufferedImage captureHintImage;
    private final BufferedImage highlightImage;
    private final BufferedImage markedRedImage;

    public PieceImageLoader() {
        // Load Piece Images
        pieceFileMap.put('P', loadPiece("white_pawn.png"));
        pieceFileMap.put('R', loadPiece("white_rook.png"));
        pieceFileMap.put('N', loadPiece("white_knight.png"));
        pieceFileMap.put('B', loadPiece("white_bishop.png"));
        pieceFileMap.put('Q', loadPiece("white_queen.png"));
        pieceFileMap.put('K', loadPiece("white_king.png"));
        pieceFileMap.put('p', loadPiece("black_pawn.png"));
        pieceFileMap.put('r', loadPiece("black_rook.png"));
        pieceFileMap.put('n', loadPiece("black_knight.png"));
        pieceFileMap.put('b', loadPiece("black_bishop.png"));
        pieceFileMap.put('q', loadPiece("black_queen.png"));
        pieceFileMap.put('k', loadPiece("black_king.png"));

        // Load Other Images
        whiteImage = ImageLoader.loadBufferedImage("board/white.png");
        blackImage = ImageLoader.loadBufferedImage("board/black.png");
        moveHintImage = ImageLoader.loadBufferedImage("board/move_hint.png");
        captureHintImage = ImageLoader.loadBufferedImage("board/capture_hint.png");
        highlightImage = ImageLoader.loadBufferedImage("board/highlight.png");
        markedRedImage = ImageLoader.loadBufferedImage("board/marked_red.png");
    }

    private BufferedImage loadPiece(String fileName) {
        String path = "pieces/" + fileName;
        return ImageLoader.loadBufferedImage(path);
    }

    // Getters
    public BufferedImage getPieceImage(char piece) { return pieceFileMap.get(piece); }
    public BufferedImage getWhiteImage() { return whiteImage; }
    public BufferedImage getBlackImage() { return blackImage; }
    public BufferedImage getMoveHintImage() { return moveHintImage; }
    public BufferedImage getCaptureHintImage() { return captureHintImage; }
    public BufferedImage getHighlightImage() { return highlightImage; }
    public BufferedImage getMarkedRedImage() { return markedRedImage; }
}
