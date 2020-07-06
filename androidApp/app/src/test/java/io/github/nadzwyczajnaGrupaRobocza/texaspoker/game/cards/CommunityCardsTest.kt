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

open class FlopCommunityCardsTest : CommunityCardsTest() {
    val flop = NoCommunityCards().flop(flop1, flop2, flop3)
    val turnCard = Card(Suit.Clubs, Rank.King)

    @Test
    fun `After turn community cards should have size four`() {
        val cardsAfterTurn = 4

        assertThat(flop.turn(turnCard).size, equalTo(cardsAfterTurn))
    }
}

open class TurnCommunityCardsTest : FlopCommunityCardsTest() {
    val turn = flop.turn(turnCard)
    val riverCard = Card(Suit.Diamonds, Rank.Two)

    @Test
    fun `After river community cards should have size five`() {
        val cardsAfterTurn = 5

        assertThat(turn.river(riverCard).size, equalTo(cardsAfterTurn))
    }
}

open class RiverCommunityCardsTest : TurnCommunityCardsTest() {
    val river = turn.river(riverCard)

    @Test
    fun `some test`() {
    }
}
