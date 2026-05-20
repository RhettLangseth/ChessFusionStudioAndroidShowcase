package com.chessfusionstudio.core.model;

import java.util.Objects;

public final class Square {
    private static final Square[] CACHE = buildCache();
    private static final String FILES = "abcdefgh";

    private final int file;
    private final int rank;
    private final int index;

    private Square(int file, int rank) {
        this.file = file;
        this.rank = rank;
        this.index = rank * 8 + file;
    }

    private static Square[] buildCache() {
        Square[] cache = new Square[64];
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                cache[rank * 8 + file] = new Square(file, rank);
            }
        }
        return cache;
    }

    public static Square at(int file, int rank) {
        if (file < 0 || file > 7 || rank < 0 || rank > 7) {
            throw new IllegalArgumentException("Square out of bounds: file=" + file + ", rank=" + rank);
        }
        return CACHE[rank * 8 + file];
    }

    public static Square fromIndex(int index) {
        if (index < 0 || index > 63) {
            throw new IllegalArgumentException("Square index out of bounds: " + index);
        }
        return CACHE[index];
    }

    public static Square fromAlgebraic(String algebraic) {
        Objects.requireNonNull(algebraic, "algebraic");
        if (algebraic.length() != 2) {
            throw new IllegalArgumentException("Invalid square notation: " + algebraic);
        }

        int file = FILES.indexOf(Character.toLowerCase(algebraic.charAt(0)));
        int rank = algebraic.charAt(1) - '1';
        return at(file, rank);
    }

    public int file() {
        return file;
    }

    public int rank() {
        return rank;
    }

    public int index() {
        return index;
    }

    public Square offset(int fileDelta, int rankDelta) {
        int targetFile = file + fileDelta;
        int targetRank = rank + rankDelta;
        if (targetFile < 0 || targetFile > 7 || targetRank < 0 || targetRank > 7) {
            return null;
        }
        return at(targetFile, targetRank);
    }

    public String toAlgebraic() {
        return String.valueOf(FILES.charAt(file)) + (rank + 1);
    }

    @Override
    public String toString() {
        return toAlgebraic();
    }
}
