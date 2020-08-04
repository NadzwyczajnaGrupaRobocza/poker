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
    @Test

    fun `Same three with bigger first kicker should be bigger`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesFive,
                heartsThree,
                diamondsTen,
                spadesQueen,
                diamondsAce
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

        assertHandBigger(
            lhs,
            rhs
        )
    }

    @Test
    fun `Same three with less second kicker should be less`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesFive,
                heartsThree,
                diamondsTen,
                spadesQueen,
                diamondsJack
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

        assertHandLess(
            lhs,
            rhs
        )
    }
}
