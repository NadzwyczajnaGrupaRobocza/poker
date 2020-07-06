package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class HandTest {
    private fun createPocketCards(
        pocketCard1: Card,
        pocketCard2: Card
    ): PocketCards {
        val pockerCards = PocketCards(pocketCard1, pocketCard2)
        return pockerCards
    }

    private fun createRiver(
        flop1: Card,
        flop2: Card,
        flop3: Card,
        turnCard: Card,
        riverCard: Card
    ): RiverCommunityCards {
        val river = RiverCommunityCards(
            TurnCommunityCards(
                FlopCommunityCards(
                    NoCommunityCards(),
                    flop1,
                    flop2,
                    flop3
                ), turnCard
            ), riverCard
        )
        return river
    }

    @Test
    fun `Hand should be contructred from PockedCards and RiverCommunityCards`() {
        val flop1 = Card(Suit.Diamonds, Rank.King)
        val flop2 = Card(Suit.Spades, Rank.Queen)
        val flop3 = Card(Suit.Diamonds, Rank.Ten)
        val turnCard = Card(Suit.Hearts, Rank.Nine)
        val riverCard = Card(Suit.Spades, Rank.Three)
        val pocketCard1 = Card(Suit.Clubs, Rank.Four)
        val pocketCard2 = Card(Suit.Clubs, Rank.Five)

        val river = createRiver(flop1, flop2, flop3, turnCard, riverCard)
        val pockerCards = createPocketCards(pocketCard1, pocketCard2)
        val hand = Hand(river, pockerCards)

        assertThat(hand.cards, equalTo(setOf(flop1, flop2, flop3, riverCard, turnCard, pocketCard1, pocketCard2)))
    }

}
