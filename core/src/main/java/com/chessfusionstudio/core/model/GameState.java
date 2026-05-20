package com.chessfusionstudio.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class GameState {
    private final Piece[] board;
    private final Color sideToMove;
    private final CastlingRights castlingRights;
    private final Square enPassantTarget;
    private final int halfmoveClock;
    private final int fullmoveNumber;
    private final String startFen;
    private final List<Move> moveHistory;

    private GameState(
            Piece[] board,
            Color sideToMove,
            CastlingRights castlingRights,
            Square enPassantTarget,
            int halfmoveClock,
            int fullmoveNumber,
            String startFen,
            List<Move> moveHistory
    ) {
        if (board == null || board.length != 64) {
            throw new IllegalArgumentException("Board must contain exactly 64 squares");
        }
        if (halfmoveClock < 0) {
            throw new IllegalArgumentException("Halfmove clock cannot be negative");
        }
        if (fullmoveNumber < 1) {
            throw new IllegalArgumentException("Fullmove number must be at least 1");
        }

        this.board = board.clone();
        this.sideToMove = Objects.requireNonNull(sideToMove, "sideToMove");
        this.castlingRights = Objects.requireNonNull(castlingRights, "castlingRights");
        this.enPassantTarget = enPassantTarget;
        this.halfmoveClock = halfmoveClock;
        this.fullmoveNumber = fullmoveNumber;
        this.startFen = Objects.requireNonNull(startFen, "startFen");
        this.moveHistory = Collections.unmodifiableList(new ArrayList<Move>(
                Objects.requireNonNull(moveHistory, "moveHistory")
        ));
    }

    public static GameState of(
            Piece[] board,
            Color sideToMove,
            CastlingRights castlingRights,
            Square enPassantTarget,
            int halfmoveClock,
            int fullmoveNumber,
            String startFen,
            List<Move> moveHistory
    ) {
        return new GameState(
                board,
                sideToMove,
                castlingRights,
                enPassantTarget,
                halfmoveClock,
                fullmoveNumber,
                startFen,
                moveHistory
        );
    }

    public Piece pieceAt(Square square) {
        return board[square.index()];
    }

    public Piece pieceAt(int index) {
        return board[index];
    }

    public Color sideToMove() {
        return sideToMove;
    }

    public CastlingRights castlingRights() {
        return castlingRights;
    }

    public Square enPassantTarget() {
        return enPassantTarget;
    }

    public int halfmoveClock() {
        return halfmoveClock;
    }

    public int fullmoveNumber() {
        return fullmoveNumber;
    }

    public String startFen() {
        return startFen;
    }

    public List<Move> moveHistory() {
        return moveHistory;
    }

    public String moveHistoryAsUci() {
        StringBuilder builder = new StringBuilder(moveHistory.size() * 5);
        for (int i = 0; i < moveHistory.size(); i++) {
            if (i > 0) {
                builder.append(' ');
            }
            builder.append(moveHistory.get(i).toUci());
        }
        return builder.toString();
    }

    public Piece[] copyBoard() {
        return board.clone();
    }
}
