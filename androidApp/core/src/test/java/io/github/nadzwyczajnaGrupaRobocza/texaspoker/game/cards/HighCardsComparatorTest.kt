package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class HighCardsComparatorTest {
    @Test
    fun `HighCards should be equal when only differ in suit`() {
        val lhs =
            createHand(
                clubsFive,
                clubsFour,
                spadesThree,
                heartsNine,
                diamondsTen,
                spadesQueen,
                diamondsKing
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFour,
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
    fun `HighCard should be bigger when first card is bigger`() {
        val lhs =
            createHand(
                clubsFive,
                clubsFour,
                spadesThree,
                heartsNine,
                diamondsTen,
                spadesQueen,
                diamondsAce
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFour,
                diamondsThree,
                spadesNine,
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
    fun `HighCard should be less when second card is less`() {
        val lhs =
            createHand(
                clubsFive,
                clubsFour,
                spadesThree,
                heartsNine,
                diamondsTen,
                spadesJack,
                diamondsKing
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFour,
                diamondsThree,
                spadesNine,
                clubsTen,
                diamondsQueen,
                spadesKing
            )

        assertHandLess(
            lhs,
            rhs
        )
    }

    @Test
    fun `HighCard should be bigger when fifth card is less`() {
        val lhs =
            createHand(
                diamondsSix,
                clubsFour,
                spadesThree,
                heartsNine,
                diamondsTen,
                spadesQueen,
                diamondsKing
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFour,
                diamondsThree,
                spadesNine,
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
    fun `HighCard should be equal when sixth card is bigger`() {
        val lhs =
            createHand(
                clubsFive,
                clubsFour,
                spadesThree,
                heartsNine,
                diamondsTen,
                spadesQueen,
                diamondsKing
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesTwo,
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
}