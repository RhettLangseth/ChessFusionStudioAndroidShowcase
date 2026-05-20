package com.chessfusionstudio.core.io;

import static org.junit.Assert.assertEquals;

import com.chessfusionstudio.core.model.GameState;
import org.junit.Test;

public class FenCodecTest {
    @Test
    public void toFen_roundTripsClassicStart() {
        GameState state = FenCodec.parse(FenCodec.CLASSIC_START);

        String fen = FenCodec.toFen(state);

        assertEquals(FenCodec.CLASSIC_START, fen);
    }
}
