package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class FullCompareTest {
    @Test
    fun `Same full should be equal`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesFive,
                heartsFive,
                diamondsThree,
                heartsThree,
                diamondsAce,
                spadesKing
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFive,
                clubsFive,
                diamondsThree,
                clubsThree,
                diamondsAce,
                spadesKing
            )

        assertHandsEqual(
            lhs,
            rhs
        )
    }

    @Test
    fun `Bigger three cards full with rest card less should be bigger`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesFive,
                heartsFive,
                diamondsThree,
                heartsThree,
                diamondsNine,
                spadesTwo
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFive,
                spadesThree,
                diamondsThree,
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
    fun `Less three cards full with rest card less when one of flush have two threes should be less`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesFive,
                heartsFive,
                diamondsThree,
                heartsThree,
                diamondsThree,
                spadesTwo
            )
        val rhs =
            createHand(
                diamondsSix,
                spadesSix,
                heartsSix,
                diamondsThree,
                clubsThree,
                diamondsAce,
                spadesKing
            )

        assertHandLess(
            lhs,
            rhs
        )
    }

    @Test
    fun `Less three cards full with rest card less when one of flush have three twos should be less`() {
        val lhs =
            createHand(
                diamondsFive,
                spadesFive,
                heartsFive,
                diamondsThree,
                heartsThree,
                diamondsThree,
                spadesTwo
            )
        val rhs =
            createHand(
                diamondsFive,
                spadesFive,
                heartsFive,
                diamondsThree,
                clubsThree,
                diamondsFour,
                spadesFour
            )

        assertHandLess(
            lhs,
            rhs
        )
    }

    @Test
    fun `Fulls should be equal`() {
        val lhs = createHand(
            clubsFive,
            heartsFive,
            clubsQueen,
            diamondsQueen,
            spadesQueen,
            spadesAce,
            diamondsKing
        )
        val rhs = createHand(
            diamondsFive,
            spadesFive,
            clubsQueen,
            diamondsQueen,
            spadesQueen,
            spadesAce,
            diamondsKing
        )

        assertHandsEqual(lhs, rhs)
        assertThat(lhs == rhs, equalTo(true))
    }
}
