package com.chessfusionstudio.core.showcase;

import com.chessfusionstudio.core.io.FenCodec;
import com.chessfusionstudio.core.model.GameState;
import java.util.Arrays;
import java.util.List;

public final class ShowcaseBoardFactory {
    private ShowcaseBoardFactory() {
    }

    public static List<GameState> samples() {
        return Arrays.asList(
                FenCodec.parse(ShowcasePositions.START_POSITION),
                FenCodec.parse(ShowcasePositions.PHILIDOR_DEFENSE),
                FenCodec.parse(ShowcasePositions.RUY_LOPEZ),
                FenCodec.parse(ShowcasePositions.QUEENS_GAMBIT),
                FenCodec.parse(ShowcasePositions.SCANDINAVIAN_DEFENSE)
        );
    }
}
