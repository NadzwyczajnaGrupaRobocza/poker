package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

open class CommunityCardsTest {
    val flop1 = Card(Suit.Clubs, Rank.Ten)
    val flop2 = Card(Suit.Spades, Rank.Ten)
    val flop3 = Card(Suit.Clubs, Rank.King)

    @Test
    fun `Community cards should being with no cards`() {
        val noCards = 0
        assertThat(NoCommunityCards().size, equalTo(noCards))
    }

    @Test
    fun `After flop community cards shuold have size of flop`() {
        val cardsAfterFlop = 3

        assertThat(NoCommunityCards().flop(flop1, flop2, flop3).size, equalTo(cardsAfterFlop))
    }
}

class FlopCommunityCardsTest : CommunityCardsTest() {
    val flop = NoCommunityCards().flop(flop1, flop2, flop3)

    @Test
    fun `After turn community cards should have size four`() {
        val cardsAfterTurn = 4
        val turn = Card(Suit.Clubs, Rank.King)

        assertThat(flop.turn(turn).size, equalTo(cardsAfterTurn))
    }
}
