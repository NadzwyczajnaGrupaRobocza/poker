package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class FourCompareTest {
    @Test
    fun `Same four should be equal`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesFive,
                heartsFive,
                clubsFive,
                diamondsThree,
                diamondsAce,
                spadesKing
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFive,
                clubsFive,
                diamondsThree,
                heartsFive,
                diamondsAce,
                spadesKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }

    @Test
    fun `Bigger four cards with one smaller card should be bigger`() {
        val lhs =
            createHand(
                diamondsNine,
                spadesNine,
                heartsNine,
                clubsNine,
                heartsThree,
                diamondsNine,
                spadesTwo
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFive,
                spadesThree,
                clubsFive,
                clubsThree,
                diamondsAce,
                spadesKing
            )

        assertHandBigger(
            lhs,
            rhs
        )
    }

    @Test
    fun `Same four cards with smaller card should be less`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesFive,
                heartsFive,
                clubsFive,
                heartsThree,
                diamondsThree,
                spadesTwo
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFive,
                heartsFive,
                clubsFive,
                clubsThree,
                diamondsAce,
                spadesKing
            )

        assertHandLess(
            lhs,
            rhs
        )
    }
}
