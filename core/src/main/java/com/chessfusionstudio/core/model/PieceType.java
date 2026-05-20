package com.chessfusionstudio.core.model;

public enum PieceType {
    PAWN('p'),
    KNIGHT('n'),
    BISHOP('b'),
    ROOK('r'),
    QUEEN('q'),
    KING('k');

    private final char fenSymbol;

    PieceType(char fenSymbol) {
        this.fenSymbol = fenSymbol;
    }

    public char fenSymbol() {
        return fenSymbol;
    }

    public static PieceType fromFenChar(char fenChar) {
        char lower = Character.toLowerCase(fenChar);
        for (PieceType type : values()) {
            if (type.fenSymbol == lower) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid piece char: " + fenChar);
    }

    public static PieceType fromPromotionChar(char promotionChar) {
        PieceType type = fromFenChar(promotionChar);
        if (type == PAWN || type == KING) {
            throw new IllegalArgumentException("Invalid promotion piece: " + promotionChar);
        }
        return type;
    }
}
