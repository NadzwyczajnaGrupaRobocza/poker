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


    @Test
    fun `Hand should be constructed from PockedCards and RiverCommunityCards`() {
        val river = createRiver(diamondsKing, spadesQueen, diamondsTen, heartsNine, spadesThree)
        val pocketCards = createPocketCards(clubsFour, clubsFive)
        val hand = Hand(river, pocketCards)

        assertThat(
            hand.cards,
            equalTo(
                setOf(
                    diamondsKing,
                    spadesQueen,
                    diamondsTen,
                    spadesThree,
                    heartsNine,
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
            heartsNine,
            diamondsTen,
            spadesQueen,
            diamondsKing
        )

        assertThat(hand.type, equalTo(HandType.HighCard))
    }

    @Test
    fun `Given pair should return pair`() {
        val hand = createHand(
            clubsFive,
            spadesThree,
            heartsNine,
            spadesFive,
            diamondsTen,
            spadesQueen,
            diamondsKing
        )

        assertThat(hand.type, equalTo(HandType.Pair))
    }

    @Test
    fun `Given two pairs should return two pairs`() {
        val hand = createHand(
            clubsFive,
            spadesThree,
            heartsThree,
            spadesFive,
            diamondsTen,
            spadesQueen,
            diamondsKing
        )

        assertThat(hand.type, equalTo(HandType.TwoPairs))
    }

    @Test
    fun `Given three same cards should return Three`() {
        val hand = createHand(
            clubsFive,
            spadesThree,
            heartsFive,
            spadesFive,
            diamondsTen,
            spadesQueen,
            diamondsKing
        )

        assertThat(hand.type, equalTo(HandType.Three))
    }

    @Test
    fun `Given five cards in order should return Straight`() {
        val hand = createHand(
            clubsFive,
            spadesThree,
            heartsFour,
            diamondsTen,
            diamondsSix,
            spadesQueen,
            spadesTwo
        )

        assertThat(hand.type, equalTo(HandType.Straight))
    }

    @Test
    fun `Given five cards from ace to five should return Straight`() {
        val hand = createHand(
            clubsFive,
            heartsFour,
            diamondsTen,
            spadesThree,
            spadesQueen,
            diamondsAce,
            spadesTwo
        )

        assertThat(hand.type, equalTo(HandType.Straight))
    }

    @Test
    fun `Given five cards from ten to ace should return Straight`() {
        val hand = createHand(
            diamondsTen,
            heartsFour,
            diamondsJack,
            spadesThree,
            spadesQueen,
            diamondsAce,
            diamondsKing
        )

        assertThat(hand.type, equalTo(HandType.Straight))
    }

    @Test
    fun `Given three same rank cards and dwo other same rank cards should return Flush`() {
        val hand = createHand(
            clubsFive,
            heartsFive,
            spadesFive,
            diamondsJack,
            heartsThree,
            spadesQueen,
            spadesThree
        )

        assertThat(hand.type, equalTo(HandType.Full))
    }

    @Test
    fun `Given three same rank cards and twice two other same rank cards should return Flush`() {
        val hand = createHand(
            clubsFive,
            heartsFive,
            spadesFive,
            heartsThree,
            clubsFour,
            heartsFour,
            spadesThree
        )

        assertThat(hand.type, equalTo(HandType.Full))
    }

}

