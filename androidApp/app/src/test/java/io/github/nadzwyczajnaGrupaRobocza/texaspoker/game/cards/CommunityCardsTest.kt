package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import org.junit.Test

private val flop1 = Card(Suit.Clubs, Rank.Ten)
private val flop2 = Card(Suit.Spades, Rank.Ten)
private val flop3 = Card(Suit.Clubs, Rank.King)
private val turnCard = Card(Suit.Clubs, Rank.King)
private val riverCard = Card(Suit.Diamonds, Rank.Two)

private val communityCards = NoCommunityCards()
private val flop = communityCards.flop(flop1, flop2, flop3)
private val turn = flop.turn(turnCard)
private val river = turn.river(riverCard)

open class CommunityCardsTest {

    @Test
    fun `Community cards should being with no cards`() {
        val noCards = 0
        assertThat(communityCards.size, equalTo(noCards))
    }

    @Test
    fun `After flop community cards shuold have size of flop`() {
        val cardsAfterFlop = 3

        assertThat(communityCards.flop(flop1, flop2, flop3).size, equalTo(cardsAfterFlop))
    }

    @Test
    fun `no cards should return empty cards`() {
        assertThat(communityCards.cards, isEmpty)
    }
}

open class FlopCommunityCardsTest : CommunityCardsTest() {
    @Test
    fun `After turn community cards should have size four`() {
        val cardsAfterTurn = 4

        assertThat(flop.turn(turnCard).size, equalTo(cardsAfterTurn))
    }

    @Test
    fun `Flop should return flop cards`() {
        assertThat(flop.cards, equalTo(setOf(flop1, flop2, flop3)))
    }
}

open class TurnCommunityCardsTest : FlopCommunityCardsTest() {

    @Test
    fun `After river community cards should have size five`() {
        val cardsAfterTurn = 5

        assertThat(turn.river(riverCard).size, equalTo(cardsAfterTurn))
    }
}

open class RiverCommunityCardsTest : TurnCommunityCardsTest() {

    @Test
    fun `some test`() {
    }
}
