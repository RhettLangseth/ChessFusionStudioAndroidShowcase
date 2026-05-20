package com.chessfusionstudio.core.model;

public final class CastlingRights {
    private final boolean whiteKingSide;
    private final boolean whiteQueenSide;
    private final boolean blackKingSide;
    private final boolean blackQueenSide;

    public CastlingRights(
            boolean whiteKingSide,
            boolean whiteQueenSide,
            boolean blackKingSide,
            boolean blackQueenSide
    ) {
        this.whiteKingSide = whiteKingSide;
        this.whiteQueenSide = whiteQueenSide;
        this.blackKingSide = blackKingSide;
        this.blackQueenSide = blackQueenSide;
    }

    public static CastlingRights initial() {
        return new CastlingRights(true, true, true, true);
    }

    public boolean whiteKingSide() {
        return whiteKingSide;
    }

    public boolean whiteQueenSide() {
        return whiteQueenSide;
    }

    public boolean blackKingSide() {
        return blackKingSide;
    }

    public boolean blackQueenSide() {
        return blackQueenSide;
    }

    public boolean hasAny() {
        return whiteKingSide || whiteQueenSide || blackKingSide || blackQueenSide;
    }

    public CastlingRights revokeWhiteKingSide() {
        return new CastlingRights(false, whiteQueenSide, blackKingSide, blackQueenSide);
    }

    public CastlingRights revokeWhiteQueenSide() {
        return new CastlingRights(whiteKingSide, false, blackKingSide, blackQueenSide);
    }

    public CastlingRights revokeBlackKingSide() {
        return new CastlingRights(whiteKingSide, whiteQueenSide, false, blackQueenSide);
    }

    public CastlingRights revokeBlackQueenSide() {
        return new CastlingRights(whiteKingSide, whiteQueenSide, blackKingSide, false);
    }

    public CastlingRights revokeForColor(Color color) {
        if (color == Color.WHITE) {
            return new CastlingRights(false, false, blackKingSide, blackQueenSide);
        }
        return new CastlingRights(whiteKingSide, whiteQueenSide, false, false);
    }

    public String toFenField() {
        StringBuilder builder = new StringBuilder(4);
        if (whiteKingSide) {
            builder.append('K');
        }
        if (whiteQueenSide) {
            builder.append('Q');
        }
        if (blackKingSide) {
            builder.append('k');
        }
        if (blackQueenSide) {
            builder.append('q');
        }
        return builder.length() == 0 ? "-" : builder.toString();
    }

    public static CastlingRights fromFenField(String fenField) {
        if (fenField == null || fenField.isEmpty()) {
            throw new IllegalArgumentException("Invalid castling rights field");
        }
        if ("-".equals(fenField)) {
            return new CastlingRights(false, false, false, false);
        }

        boolean whiteKingSide = false;
        boolean whiteQueenSide = false;
        boolean blackKingSide = false;
        boolean blackQueenSide = false;

        for (int i = 0; i < fenField.length(); i++) {
            char symbol = fenField.charAt(i);
            switch (symbol) {
                case 'K':
                    whiteKingSide = true;
                    break;
                case 'Q':
                    whiteQueenSide = true;
                    break;
                case 'k':
                    blackKingSide = true;
                    break;
                case 'q':
                    blackQueenSide = true;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid castling rights symbol: " + symbol);
            }
        }

        return new CastlingRights(whiteKingSide, whiteQueenSide, blackKingSide, blackQueenSide);
    }
}
