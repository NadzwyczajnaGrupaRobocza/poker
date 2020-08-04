package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class FullCompareTest {
    @Test
    fun `Same full should be equal`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesFive,
                heartsFive,
                diamondsThree,
                heartsThree,
                diamondsAce,
                spadesKing
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFive,
                clubsFive,
                diamondsThree,
                clubsThree,
                diamondsAce,
                spadesKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }
}
