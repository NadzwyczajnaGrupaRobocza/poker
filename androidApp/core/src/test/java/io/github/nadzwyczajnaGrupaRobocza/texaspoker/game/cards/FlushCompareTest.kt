package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class FlushCompareTest {
    @Test
    fun `Same flush should be equal`() {
        val lhs =
            createHand(
                diamondsFive,
                diamondsJack,
                diamondsAce,
                diamondsNine,
                diamondsThree,
                diamondsAce,
                spadesKing
            )
        val rhs =
            createHand(
                diamondsNine,
                diamondsQueen,
                diamondsAce,
                diamondsTen,
                diamondsKing,
                spadesAce,
                heartsKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }
}
