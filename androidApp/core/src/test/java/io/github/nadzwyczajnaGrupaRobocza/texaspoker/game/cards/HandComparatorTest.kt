package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.lessThan
import compareTo
import org.junit.Test

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

class HandComparatorTest {
    @Test
    fun `HighCard should be less then Pair`() {
        val lhs = createHand(
            clubsFive,
            clubsFour,
            clubsThree,
            heartsNine,
            diamondsTen,
            spadesQueen,
            diamondsKing
        )
        val rhs = createHand(
            spadesThree,
            spadesFour,
            diamondsThree,
            spadesNine,
            clubsTen,
            diamondsQueen,
            spadesKing
        )

        assertHandLess(lhs, rhs)
    }
}

class HighCardsComparatorTest {
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
    fun `HighCard should be less when second card is less`() {
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

    @Test
    fun `HighCard should be bigger when fifth card is less`() {
        val lhs = createHand(
            diamondsSix,
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

        assertHandBigger(lhs, rhs)
    }


    @Test
    fun `HighCard should be qual when sixth card is bigger`() {
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
            spadesTwo,
            diamondsThree,
            spadesNine,
            clubsTen,
            diamondsQueen,
            spadesKing
        )

        assertHandsEqual(lhs, rhs)
    }
}

class PairCompareTest {
    @Test
    fun `Same pairs with same kickers should be equal`() {
        val lhs = createHand(
            clubsFive,
            diamondsFive,
            spadesThree,
            heartsNine,
            diamondsTen,
            spadesQueen,
            diamondsKing
        )
        val rhs = createHand(
            spadesFive,
            heartsFive,
            diamondsThree,
            spadesNine,
            clubsTen,
            diamondsQueen,
            spadesKing
        )

        assertHandsEqual(lhs, rhs)
    }

    @Test
    fun `Same pairs with bigger first kicker should be bigger`() {
        val lhs = createHand(
            clubsFive,
            diamondsFive,
            spadesThree,
            heartsNine,
            diamondsTen,
            spadesQueen,
            spadesAce
        )
        val rhs = createHand(
            spadesFive,
            heartsFive,
            diamondsThree,
            spadesNine,
            clubsTen,
            diamondsQueen,
            diamondsKing
        )

        assertHandBigger(lhs, rhs)
    }

    @Test
    fun `Less pair with bigger first kicker should be less`() {
        val lhs = createHand(
            clubsFive,
            diamondsThree,
            spadesThree,
            heartsNine,
            diamondsTen,
            spadesQueen,
            spadesAce
        )
        val rhs = createHand(
            spadesFive,
            heartsFive,
            spadesTwo,
            spadesNine,
            clubsTen,
            diamondsQueen,
            diamondsKing
        )

        assertHandLess(lhs, rhs)
    }
}
