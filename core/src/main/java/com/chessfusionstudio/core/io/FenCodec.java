package com.chessfusionstudio.core.io;

import com.chessfusionstudio.core.model.CastlingRights;
import com.chessfusionstudio.core.model.Color;
import com.chessfusionstudio.core.model.GameState;
import com.chessfusionstudio.core.model.Piece;
import com.chessfusionstudio.core.model.Square;
import java.util.Collections;

public final class FenCodec {
    public static final String CLASSIC_START = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final String INITIAL_FEN = CLASSIC_START;

    private FenCodec() {
    }

    public static GameState classicInitialState() {
        return parse(CLASSIC_START);
    }

    public static GameState parse(String fen) {
        if (fen == null || fen.trim().isEmpty()) {
            throw new IllegalArgumentException("FEN cannot be empty");
        }

        String[] fields = fen.trim().split("\\s+");
        if (fields.length != 6) {
            throw new IllegalArgumentException("FEN must contain exactly 6 fields");
        }

        Piece[] board = parseBoard(fields[0]);
        Color sideToMove = Color.fromFenChar(fields[1].charAt(0));
        CastlingRights castlingRights = CastlingRights.fromFenField(fields[2]);
        Square enPassantTarget = "-".equals(fields[3]) ? null : Square.fromAlgebraic(fields[3]);

        int halfmoveClock = parseNonNegativeInt(fields[4], "halfmove clock");
        int fullmoveNumber = parsePositiveInt(fields[5], "fullmove number");

        return GameState.of(
                board,
                sideToMove,
                castlingRights,
                enPassantTarget,
                halfmoveClock,
                fullmoveNumber,
                fen.trim(),
                Collections.<com.chessfusionstudio.core.model.Move>emptyList()
        );
    }

    public static String toFen(GameState state) {
        StringBuilder fen = new StringBuilder(90);
        appendBoard(state, fen);
        fen.append(' ')
                .append(state.sideToMove().toFenChar())
                .append(' ')
                .append(state.castlingRights().toFenField())
                .append(' ')
                .append(state.enPassantTarget() == null ? "-" : state.enPassantTarget().toAlgebraic())
                .append(' ')
                .append(state.halfmoveClock())
                .append(' ')
                .append(state.fullmoveNumber());
        return fen.toString();
    }

    private static Piece[] parseBoard(String boardField) {
        Piece[] board = new Piece[64];
        String[] rankFields = boardField.split("/");
        if (rankFields.length != 8) {
            throw new IllegalArgumentException("Board FEN must contain 8 ranks");
        }

        for (int fenRank = 0; fenRank < 8; fenRank++) {
            String rankField = rankFields[fenRank];
            int boardRank = 7 - fenRank;
            int file = 0;

            for (int i = 0; i < rankField.length(); i++) {
                char token = rankField.charAt(i);
                if (Character.isDigit(token)) {
                    int emptyCount = token - '0';
                    if (emptyCount < 1 || emptyCount > 8) {
                        throw new IllegalArgumentException("Invalid empty-square count in FEN: " + token);
                    }
                    file += emptyCount;
                } else {
                    Piece piece = Piece.fromFenChar(token);
                    board[Square.at(file, boardRank).index()] = piece;
                    file++;
                }
            }

            if (file != 8) {
                throw new IllegalArgumentException("Rank does not contain 8 squares: " + rankField);
            }
        }

        return board;
    }

    private static void appendBoard(GameState state, StringBuilder builder) {
        for (int rank = 7; rank >= 0; rank--) {
            int emptyCount = 0;
            for (int file = 0; file < 8; file++) {
                Piece piece = state.pieceAt(Square.at(file, rank));
                if (piece == null) {
                    emptyCount++;
                    continue;
                }
                if (emptyCount > 0) {
                    builder.append(emptyCount);
                    emptyCount = 0;
                }
                builder.append(piece.toFenChar());
            }
            if (emptyCount > 0) {
                builder.append(emptyCount);
            }
            if (rank > 0) {
                builder.append('/');
            }
        }
    }

    private static int parseNonNegativeInt(String value, String fieldName) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < 0) {
                throw new IllegalArgumentException("Invalid " + fieldName + ": " + value);
            }
            return parsed;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid " + fieldName + ": " + value, exception);
        }
    }

    private static int parsePositiveInt(String value, String fieldName) {
        int parsed = parseNonNegativeInt(value, fieldName);
        if (parsed < 1) {
            throw new IllegalArgumentException("Invalid " + fieldName + ": " + value);
        }
        return parsed;
    }
}
