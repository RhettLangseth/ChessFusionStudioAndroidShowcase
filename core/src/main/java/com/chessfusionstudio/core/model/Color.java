package com.chessfusionstudio.core.model;

public enum Color {
    WHITE(1),
    BLACK(-1);

    private final int pawnForwardDirection;

    Color(int pawnForwardDirection) {
        this.pawnForwardDirection = pawnForwardDirection;
    }

    public int pawnForwardDirection() {
        return pawnForwardDirection;
    }

    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }

    public char toFenChar() {
        return this == WHITE ? 'w' : 'b';
    }

    public static Color fromFenChar(char fenChar) {
        if (fenChar == 'w') {
            return WHITE;
        }
        if (fenChar == 'b') {
            return BLACK;
        }
        throw new IllegalArgumentException("Invalid side-to-move char: " + fenChar);
    }
}
