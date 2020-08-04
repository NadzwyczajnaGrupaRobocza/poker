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
}
