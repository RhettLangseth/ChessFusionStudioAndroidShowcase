package com.chessfusionstudio.core.showcase;

import com.chessfusionstudio.core.io.FenCodec;
import com.chessfusionstudio.core.model.GameState;
import java.util.Arrays;
import java.util.List;

public final class ShowcaseBoardFactory {
    private ShowcaseBoardFactory() {
    }

    public static GameState classicStart() {
        return FenCodec.parse(ShowcasePositions.CLASSIC_START);
    }

    public static List<GameState> samples() {
        return Arrays.asList(
                FenCodec.parse(ShowcasePositions.CLASSIC_START),
                FenCodec.parse(ShowcasePositions.SICILIAN_STRUCTURE),
                FenCodec.parse(ShowcasePositions.ENDGAME_STUDY)
        );
    }
}
