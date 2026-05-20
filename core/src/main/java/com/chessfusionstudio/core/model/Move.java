package com.chessfusionstudio.core.model;

import java.util.Objects;

public final class Move {
    private final Square from;
    private final Square to;
    private final PieceType promotion;

    public Move(Square from, Square to) {
        this(from, to, null);
    }

    public Move(Square from, Square to, PieceType promotion) {
        this.from = Objects.requireNonNull(from, "from");
        this.to = Objects.requireNonNull(to, "to");
        if (promotion == PieceType.PAWN || promotion == PieceType.KING) {
            throw new IllegalArgumentException("Invalid promotion piece: " + promotion);
        }
        this.promotion = promotion;
    }

    public Square from() {
        return from;
    }

    public Square to() {
        return to;
    }

    public PieceType promotion() {
        return promotion;
    }

    public boolean isPromotion() {
        return promotion != null;
    }

    public String toUci() {
        StringBuilder builder = new StringBuilder(5);
        builder.append(from.toAlgebraic()).append(to.toAlgebraic());
        if (promotion != null) {
            builder.append(promotion.fenSymbol());
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return toUci();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        Move that = (Move) other;
        return from.equals(that.from) && to.equals(that.to) && promotion == that.promotion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, promotion);
    }
}
