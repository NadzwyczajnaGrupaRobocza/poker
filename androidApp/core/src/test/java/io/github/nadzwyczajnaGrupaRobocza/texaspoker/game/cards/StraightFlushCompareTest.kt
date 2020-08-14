package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class StraightFlushCompareTest {
    @Test
    fun `Same StraightFlush should be equal`() {
        val lhs =
            createHand(
                diamondsNine,
                diamondsTen,
                diamondsJack,
                diamondsQueen,
                diamondsKing,
                clubsFive,
                spadesKing
            )
        val rhs =
            createHand(
                diamondsNine,
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
    fun `Same StraightFlush with different suit should be equal`() {
        val lhs =
            createHand(
                diamondsNine,
                diamondsTen,
                diamondsJack,
                diamondsQueen,
                diamondsKing,
                clubsNine,
                spadesKing
            )
        val rhs =
            createHand(
                spadesNine,
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

    @Test
    fun `Bigger Straight should be bigger`() {
        val lhs =
            createHand(
                diamondsNine,
                diamondsTen,
                diamondsJack,
                diamondsQueen,
                diamondsKing,
                diamondsEight,
                spadesKing
            )
        val rhs =
            createHand(
                spadesNine,
                spadesTen,
                spadesJack,
                spadesQueen,
                spadesEight,
                clubsNine,
                spadesTwo
            )

        assertHandBigger(
            lhs,
            rhs
        )
    }

    @Test
    fun `Less StraightFlush should be bigger then bigger Straight`() {
        val lhs =
            createHand(
                diamondsNine,
                diamondsTen,
                spadesJack,
                diamondsQueen,
                diamondsKing,
                diamondsEight,
                spadesKing
            )
        val rhs =
            createHand(
                spadesNine,
                spadesTen,
                spadesJack,
                spadesQueen,
                spadesEight,
                clubsNine,
                spadesTwo
            )

        assertHandBigger(
            lhs,
            rhs
        )
    }
}
