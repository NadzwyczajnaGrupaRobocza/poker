package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class StraightCompareTest {
    @Test
    fun `Same straight should be equal`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesSix,
                heartsSeven,
                clubsEight,
                diamondsNine,
                diamondsAce,
                spadesKing
            )
        val rhs =
            createHand(
                clubsFive,
                spadesSix,
                heartsSeven,
                clubsEight,
                clubsNine,
                spadesAce,
                heartsKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }

    @Test
    fun `Same straight with different other cards should be equal`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesSix,
                heartsSeven,
                clubsEight,
                diamondsNine,
                diamondsJack,
                spadesKing
            )
        val rhs =
            createHand(
                clubsFive,
                spadesSix,
                heartsSeven,
                clubsEight,
                clubsNine,
                spadesTwo,
                heartsKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }

    @Test
    fun `Same straight with different pairs should be equal`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesSix,
                heartsSeven,
                clubsEight,
                diamondsNine,
                clubsFive,
                spadesKing
            )
        val rhs =
            createHand(
                clubsFive,
                spadesSix,
                heartsSeven,
                clubsEight,
                clubsNine,
                spadesNine,
                heartsKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }

    @Test
    fun `Bigger straight should be bigger`() {
        val lhs =
            createHand(
                diamondsTen,
                spadesSix,
                heartsSeven,
                clubsEight,
                diamondsNine,
                clubsTwo,
                spadesKing
            )
        val rhs =
            createHand(
                clubsFive,
                spadesSix,
                heartsSeven,
                clubsEight,
                clubsNine,
                spadesTwo,
                heartsKing
            )

        assertHandBigger(
            lhs,
            rhs
        )
    }

    @Test
    fun `Less straight with more cards in order should be less`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesSix,
                heartsSeven,
                clubsEight,
                diamondsNine,
                clubsFour,
                diamondsJack
            )
        val rhs =
            createHand(
                clubsFive,
                spadesSix,
                heartsSeven,
                clubsEight,
                clubsNine,
                spadesFour,
                diamondsTen
            )

        assertHandLess(
            lhs,
            rhs
        )
    }
}
