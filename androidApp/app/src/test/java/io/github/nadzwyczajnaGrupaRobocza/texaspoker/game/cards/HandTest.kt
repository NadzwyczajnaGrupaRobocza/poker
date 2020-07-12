package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class HandTest {
    private fun createPocketCards(
        pocketCard1: Card,
        pocketCard2: Card
    ) = PocketCards(pocketCard1, pocketCard2)

    private fun createRiver(
        flop1: Card,
        flop2: Card,
        flop3: Card,
        turnCard: Card,
        riverCard: Card
    ) =
        RiverCommunityCards(
            TurnCommunityCards(
                FlopCommunityCards(
                    NoCommunityCards(),
                    flop1,
                    flop2,
                    flop3
                ), turnCard
            ), riverCard
        )

    private fun createHand(
        card1: Card,
        card2: Card,
        card3: Card,
        card4: Card,
        card5: Card,
        card6: Card,
        card7: Card
    ) = Hand(createRiver(card1, card2, card3, card4, card5), createPocketCards(card6, card7))

    private val diamondKing = Card(Suit.Diamonds, Rank.King)
    private val spadesQueen = Card(Suit.Spades, Rank.Queen)
    private val diamondsTen = Card(Suit.Diamonds, Rank.Ten)
    private val hearsNine = Card(Suit.Hearts, Rank.Nine)
    private val spadesThree = Card(Suit.Spades, Rank.Three)
    private val hearsThree = Card(Suit.Hearts, Rank.Three)
    private val clubsFour = Card(Suit.Clubs, Rank.Four)
    private val clubsFive = Card(Suit.Clubs, Rank.Five)
    private val spadesFive = Card(Suit.Spades, Rank.Five)
    private val hearsFive = Card(Suit.Hearts, Rank.Five)

    @Test
    fun `Hand should be constructed from PockedCards and RiverCommunityCards`() {
        val river = createRiver(diamondKing, spadesQueen, diamondsTen, hearsNine, spadesThree)
        val pocketCards = createPocketCards(clubsFour, clubsFive)
        val hand = Hand(river, pocketCards)

        assertThat(
            hand.cards,
            equalTo(
                setOf(
                    diamondKing,
                    spadesQueen,
                    diamondsTen,
                    spadesThree,
                    hearsNine,
                    clubsFour,
                    clubsFive
                )
            )
        )
    }

    @Test
    fun `Given no special cards should return highest card`() {
        val hand = createHand(
            clubsFive,
            clubsFour,
            spadesThree,
            hearsNine,
            diamondsTen,
            spadesQueen,
            diamondKing
        )

        assertThat(hand.type, equalTo(HandType.HighCard))
    }

    @Test
    fun `Given pair should return pair`() {
        val hand = createHand(
            clubsFive,
            spadesThree,
            hearsNine,
            spadesFive,
            diamondsTen,
            spadesQueen,
            diamondKing
        )

        assertThat(hand.type, equalTo(HandType.Pair))
    }

    @Test
    fun `Given two pairs should return two pairs`() {
        val hand = createHand(
            clubsFive,
            spadesThree,
            hearsThree,
            spadesFive,
            diamondsTen,
            spadesQueen,
            diamondKing
        )

        assertThat(hand.type, equalTo(HandType.TwoPairs))
    }

    @Test
    fun `Given three same cards should return Three`() {
        val hand = createHand(
            clubsFive,
            spadesThree,
            hearsFive,
            spadesFive,
            diamondsTen,
            spadesQueen,
            diamondKing
        )

        assertThat(hand.type, equalTo(HandType.Three))
    }
}
