package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class ThreeCompareTest {
    @Test
    fun `Same three with same kickers should be equal`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesFive,
                heartsThree,
                diamondsTen,
                spadesQueen,
                diamondsKing
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                diamondsFive,
                clubsThree,
                clubsTen,
                diamondsQueen,
                spadesKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }
}
