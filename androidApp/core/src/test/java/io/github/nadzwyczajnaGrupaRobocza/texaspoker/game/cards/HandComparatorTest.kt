package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class HandComparatorTest {
    @Test
    fun `HighCard should be bigger then Pair`() {
        val lhs = createHand(
            spadesThree,
            spadesFour,
            diamondsThree,
            spadesNine,
            clubsTen,
            diamondsQueen,
            spadesKing
        )
        val rhs = createHand(
            clubsFive,
            clubsFour,
            clubsThree,
            heartsNine,
            diamondsTen,
            spadesQueen,
            diamondsKing
        )

        assertHandBigger(lhs, rhs)
    }

    @Test
    fun `HighCard should be less then Pair`() {
        val lhs = createHand(
            clubsFive,
            clubsFour,
            clubsThree,
            heartsNine,
            diamondsTen,
            spadesQueen,
            diamondsKing
        )
        val rhs = createHand(
            spadesThree,
            spadesFour,
            diamondsThree,
            spadesNine,
            clubsTen,
            diamondsQueen,
            spadesKing
        )

        assertHandLess(lhs, rhs)
    }
}

