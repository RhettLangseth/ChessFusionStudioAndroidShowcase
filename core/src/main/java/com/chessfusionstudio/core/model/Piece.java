package com.chessfusionstudio.core.model;

import java.util.Objects;

public final class Piece {
    private final PieceType type;
    private final Color color;

    public Piece(PieceType type, Color color) {
        this.type = Objects.requireNonNull(type, "type");
        this.color = Objects.requireNonNull(color, "color");
    }

    public PieceType type() {
        return type;
    }

    public Color color() {
        return color;
    }

    public char toFenChar() {
        char symbol = type.fenSymbol();
        return color == Color.WHITE ? Character.toUpperCase(symbol) : symbol;
    }

    public static Piece fromFenChar(char fenChar) {
        PieceType type = PieceType.fromFenChar(fenChar);
        Color color = Character.isUpperCase(fenChar) ? Color.WHITE : Color.BLACK;
        return new Piece(type, color);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Piece)) {
            return false;
        }
        Piece that = (Piece) other;
        return type == that.type && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    @Override
    public String toString() {
        return String.valueOf(toFenChar());
    }
}
