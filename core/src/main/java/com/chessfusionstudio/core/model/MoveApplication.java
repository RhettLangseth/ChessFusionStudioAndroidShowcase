package com.chessfusionstudio.core.model;

import java.util.Objects;

public final class MoveApplication {
    private final GameState beforeState;
    private final GameState afterState;
    private final Move move;
    private final Piece movedPiece;
    private final Piece capturedPiece;

    public MoveApplication(
            GameState beforeState,
            GameState afterState,
            Move move,
            Piece movedPiece,
            Piece capturedPiece
    ) {
        this.beforeState = Objects.requireNonNull(beforeState, "beforeState");
        this.afterState = Objects.requireNonNull(afterState, "afterState");
        this.move = Objects.requireNonNull(move, "move");
        this.movedPiece = Objects.requireNonNull(movedPiece, "movedPiece");
        this.capturedPiece = capturedPiece;
    }

    public GameState beforeState() {
        return beforeState;
    }

    public GameState afterState() {
        return afterState;
    }

    public Move move() {
        return move;
    }

    public Piece movedPiece() {
        return movedPiece;
    }

    public Piece capturedPiece() {
        return capturedPiece;
    }
}
