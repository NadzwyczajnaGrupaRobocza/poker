package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class PairCompareTest {
    @Test
    fun `Same pairs with same kickers should be equal`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesThree,
                heartsNine,
                diamondsTen,
                spadesQueen,
                diamondsKing
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                diamondsThree,
                spadesNine,
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
    fun `Same pairs with bigger first kicker should be bigger`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesThree,
                heartsNine,
                diamondsTen,
                spadesQueen,
                spadesAce
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                diamondsThree,
                spadesNine,
                clubsTen,
                diamondsQueen,
                diamondsKing
            )

        assertHandBigger(
            lhs,
            rhs
        )
    }

    @Test
    fun `Less pair with bigger first kicker should be less`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsThree,
                spadesThree,
                heartsNine,
                diamondsTen,
                spadesQueen,
                spadesAce
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                spadesTwo,
                spadesNine,
                clubsTen,
                diamondsQueen,
                diamondsKing
            )

        assertHandLess(
            lhs,
            rhs
        )
    }

    @Test
    fun `Same pair with bigger third kicker should be bigger`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesThree,
                heartsNine,
                diamondsJack,
                spadesQueen,
                spadesKing
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                spadesTwo,
                spadesNine,
                clubsTen,
                diamondsQueen,
                diamondsKing
            )

        assertHandBigger(
            lhs,
            rhs
        )
    }

    @Test
    fun `Same pair with bigger fourth kicker should be equal`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesThree,
                heartsNine,
                diamondsTen,
                spadesQueen,
                spadesKing
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                spadesTwo,
                spadesEight,
                clubsTen,
                diamondsQueen,
                diamondsKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }
}