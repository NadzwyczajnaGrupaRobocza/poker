package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.lessThan
import compareTo
import org.junit.Test

class HandComparatorTest {
    @Test
    fun `HighCards should be equal when only differ in suit`() {
        val lhs = createHand(
            clubsFive,
            clubsFour,
            spadesThree,
            heartsNine,
            diamondsTen,
            spadesQueen,
            diamondsKing
        )
        val rhs = createHand(
            diamondsFive,
            spadesFour,
            diamondsThree,
            spadesNine,
            clubsTen,
            diamondsQueen,
            spadesKing
        )

        assertHandsEqual(lhs, rhs)
    }

    @Test
    fun `HighCard should be bigger when first card is bigger`() {
        val lhs = createHand(
            clubsFive,
            clubsFour,
            spadesThree,
            heartsNine,
            diamondsTen,
            spadesQueen,
            diamondsAce
        )
        val rhs = createHand(
            diamondsFive,
            spadesFour,
            diamondsThree,
            spadesNine,
            clubsTen,
            diamondsQueen,
            spadesKing
        )

        assertHandBigger(lhs, rhs)
    }

    @Test
    fun `HighCard should be lesser when second card is lesser`() {
        val lhs = createHand(
            clubsFive,
            clubsFour,
            spadesThree,
            heartsNine,
            diamondsTen,
            spadesJack,
            diamondsKing
        )
        val rhs = createHand(
            diamondsFive,
            spadesFour,
            diamondsThree,
            spadesNine,
            clubsTen,
            diamondsQueen,
            spadesKing
        )

        assertHandLess(lhs, rhs)
    }


    private fun assertHandsEqual(
        lhs: Hand,
        rhs: Hand
    ) {
        assertThat(lhs.compareTo(rhs), equalTo(0))
    }

    private fun assertHandBigger(
        lhs: Hand,
        rhs: Hand
    ) {
        assertThat(lhs.compareTo(rhs), greaterThan(0))
    }

    private fun assertHandLess(
        lhs: Hand,
        rhs: Hand
    ) {
        assertThat(lhs.compareTo(rhs), lessThan(0))
    }
}
