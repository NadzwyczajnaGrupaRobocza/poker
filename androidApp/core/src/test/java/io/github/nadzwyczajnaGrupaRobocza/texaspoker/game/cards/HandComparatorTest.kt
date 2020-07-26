package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
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

    private fun assertHandsEqual(
        lhs: Hand,
        rhs: Hand
    ) {
        assertThat(lhs.compareTo(rhs), equalTo(0))
    }
}
