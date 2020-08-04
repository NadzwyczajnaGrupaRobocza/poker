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
                diamondsKing,
                spadesKing
            )
        val rhs =
            createHand(
                diamondsFive,
                diamondsJack,
                diamondsAce,
                diamondsNine,
                diamondsThree,
                diamondsKing,
                heartsKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }

    @Test
    fun `Flush with smaller biggest card should be less`() {
        val lhs =
            createHand(
                diamondsFive,
                diamondsJack,
                diamondsQueen,
                diamondsNine,
                diamondsThree,
                diamondsKing,
                spadesKing
            )
        val rhs =
            createHand(
                diamondsFive,
                diamondsJack,
                diamondsAce,
                diamondsNine,
                diamondsThree,
                diamondsKing,
                heartsKing
            )

        assertHandLess(
            lhs,
            rhs
        )
    }

    @Test
    fun `Different suit flush with smae ranks should be equal`() {
        val lhs =
            createHand(
                clubsFive,
                clubsJack,
                clubsQueen,
                clubsNine,
                diamondsThree,
                clubsKing,
                spadesKing
            )
        val rhs =
            createHand(
                diamondsFive,
                diamondsJack,
                diamondsAce,
                diamondsNine,
                diamondsThree,
                diamondsKing,
                heartsKing
            )

        assertHandLess(
            lhs,
            rhs
        )
    }
}
