package frontend.view.game;

public class Square {
    // State
    private Character piece = null;
    private boolean isHinted = false;
    private boolean isHighlighted = false;
    private boolean isMarkedRed = false;

    // Getters
    public Character getPiece() { return piece; }
    public boolean isHinted() { return isHinted; }
    public boolean isHighlighted() { return isHighlighted; }
    public boolean isMarkedRed() { return isMarkedRed; }

    // Setters
    public void setPiece(Character piece) { this.piece = piece; }
    public void setHint(boolean isHinted) { this.isHinted = isHinted; }
    public void setHighLight(boolean isHighLighted) { this.isHighlighted = isHighLighted; }
    public void setMarkedRed(boolean isMarkedRed) { this.isMarkedRed = isMarkedRed; }

    public void clearOverlays() {
        isHinted = false;
        isHighlighted = false;
        isMarkedRed = false;
    }
}
