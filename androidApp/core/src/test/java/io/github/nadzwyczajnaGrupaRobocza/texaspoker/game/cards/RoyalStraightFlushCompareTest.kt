package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class RoyalStraightFlushCompareTest {
    @Test
    fun `Same RoyalStraightFlush should be equal`() {
        val lhs =
            createHand(
                diamondsAce,
                diamondsTen,
                diamondsJack,
                diamondsQueen,
                diamondsKing,
                clubsFive,
                spadesKing
            )
        val rhs =
            createHand(
                diamondsAce,
                diamondsTen,
                diamondsJack,
                diamondsQueen,
                diamondsKing,
                clubsNine,
                spadesKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }

    @Test
    fun `RoyalStraightFlushes in different colors should be equal`() {
        val lhs =
            createHand(
                diamondsAce,
                diamondsTen,
                diamondsJack,
                diamondsQueen,
                diamondsKing,
                clubsNine,
                spadesKing
            )
        val rhs =
            createHand(
                spadesAce,
                spadesTen,
                spadesJack,
                spadesQueen,
                spadesKing,
                clubsNine,
                spadesKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }
}
