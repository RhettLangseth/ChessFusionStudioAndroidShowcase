package com.chessfusionstudio.core.showcase;

import static org.junit.Assert.assertEquals;

import com.chessfusionstudio.core.io.FenCodec;
import com.chessfusionstudio.core.model.GameState;
import java.util.List;
import org.junit.Test;

public class ShowcaseBoardFactoryTest {
    @Test
    public void samplesContainExpectedOpeningPositionsInDisplayOrder() {
        List<GameState> samples = ShowcaseBoardFactory.samples();

        assertEquals(5, samples.size());
        assertEquals(ShowcasePositions.START_POSITION, FenCodec.toFen(samples.get(0)));
        assertEquals(ShowcasePositions.PHILIDOR_DEFENSE, FenCodec.toFen(samples.get(1)));
        assertEquals(ShowcasePositions.RUY_LOPEZ, FenCodec.toFen(samples.get(2)));
        assertEquals(ShowcasePositions.QUEENS_GAMBIT, FenCodec.toFen(samples.get(3)));
        assertEquals(ShowcasePositions.SCANDINAVIAN_DEFENSE, FenCodec.toFen(samples.get(4)));
    }
}
