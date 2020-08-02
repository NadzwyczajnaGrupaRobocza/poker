package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class TwoPairsCompareTest {
    @Test
    fun `Same two pairs with same kickers should be equal`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesThree,
                heartsThree,
                diamondsTen,
                spadesQueen,
                diamondsKing
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                diamondsThree,
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
    fun `Same pairs with bigger kicker should be bigger`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesThree,
                heartsThree,
                diamondsTen,
                spadesQueen,
                spadesAce
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                diamondsThree,
                clubsThree,
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
    fun `Same pairs with less second kicker should be equal`() {
        val lhs =
            createHand(
                clubsFive,
                diamondsFive,
                spadesThree,
                heartsThree,
                diamondsTen,
                spadesQueen,
                spadesAce
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                diamondsThree,
                clubsThree,
                clubsTen,
                diamondsAce,
                diamondsKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }

    @Test
    fun `Same pairs with bigger second kicker smaller then pairs should be bigger`() {
        val lhs =
            createHand(
                clubsNine,
                diamondsNine,
                spadesThree,
                heartsThree,
                spadesFour,
                clubsTwo,
                clubsEight
            )
        val rhs =
            createHand(
                spadesNine,
                heartsNine,
                diamondsThree,
                clubsThree,
                spadesTwo,
                diamondsSix,
                diamondsFive
            )

        assertHandBigger(
            lhs,
            rhs
        )
    }

    @Test
    fun `Bigger first pair should be bigger`() {
        val lhs =
            createHand(
                clubsNine,
                diamondsNine,
                spadesThree,
                heartsThree,
                spadesFour,
                clubsTwo,
                clubsEight
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                diamondsThree,
                clubsThree,
                spadesTwo,
                diamondsSix,
                diamondsAce
            )

        assertHandBigger(
            lhs,
            rhs
        )
    }

    @Test
    fun `Less second pair should be less`() {
        val lhs =
            createHand(
                clubsNine,
                diamondsNine,
                spadesThree,
                heartsThree,
                spadesFour,
                clubsTwo,
                clubsEight
            )
        val rhs =
            createHand(
                spadesFive,
                heartsFive,
                diamondsNine,
                clubsNine,
                spadesTwo,
                diamondsSix,
                diamondsAce
            )

        assertHandLess(
            lhs,
            rhs
        )
    }
}