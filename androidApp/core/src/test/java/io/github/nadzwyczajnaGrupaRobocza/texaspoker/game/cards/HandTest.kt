package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import com.natpryce.hamkrest.isIn
import org.junit.Test

class HandTest {
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
        assertThat(
            hand.importantCards,
            equalTo(listOf(diamondsKing, spadesQueen, diamondsTen, heartsNine, clubsFive))
        )
        assertThat(hand.kickers, isEmpty)
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
        assertThat(
            hand.importantCards.toSet(),
            equalTo(setOf(clubsFive, spadesFive))
        )
        assertThat(hand.kickers, equalTo(listOf(diamondsKing, spadesQueen, diamondsTen)))
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
        assertThat(hand.importantCards.size, equalTo(4))
        assertThat(hand.importantCards.take(2).toSet(), equalTo(setOf(clubsFive, spadesFive)))
        assertThat(
            hand.importantCards.takeLast(2).toSet(),
            equalTo(setOf(spadesThree, heartsThree))
        )
        assertThat(hand.kickers, equalTo(listOf(diamondsKing)))
    }

    @Test
    fun `Given three pairs should return two pairs`() {
        val hand = createHand(
            clubsFive,
            spadesThree,
            heartsThree,
            spadesFive,
            diamondsTen,
            spadesTen,
            diamondsKing
        )

        assertThat(hand.type, equalTo(HandType.TwoPairs))
        assertThat(hand.importantCards.size, equalTo(4))
        assertThat(hand.importantCards.take(2).toSet(), equalTo(setOf(spadesTen, diamondsTen)))
        assertThat(
            hand.importantCards.takeLast(2).toSet(),
            equalTo(setOf(spadesFive, clubsFive))
        )
        assertThat(hand.kickers, equalTo(listOf(diamondsKing)))
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
        assertThat(hand.importantCards.size, equalTo(3))
        assertThat(hand.importantCards.toSet(), equalTo(setOf(clubsFive, spadesFive, heartsFive)))
        assertThat(hand.kickers, equalTo(listOf(diamondsKing, spadesQueen)))
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
        assertThat(
            hand.importantCards,
            equalTo(listOf(diamondsSix, clubsFive, heartsFour, spadesThree, spadesTwo))
        )
        assertThat(hand.kickers, isEmpty)
    }

    @Test
    fun `Given five cards when two has pair should return Straight`() {
        val hand = createHand(
            clubsFive,
            spadesThree,
            heartsFour,
            diamondsFive,
            diamondsSix,
            clubsThree,
            spadesTwo
        )

        assertThat(hand.type, equalTo(HandType.Straight))
        assertThat(hand.importantCards[0], equalTo(diamondsSix))
        assertThat(hand.importantCards[1], isIn(diamondsFive, clubsFive))
        assertThat(hand.importantCards[2], equalTo(heartsFour))
        assertThat(hand.importantCards[3], isIn(spadesThree, clubsThree))
        assertThat(hand.importantCards[4], equalTo(spadesTwo))
        assertThat(hand.kickers, isEmpty)
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
        assertThat(
            hand.importantCards,
            equalTo(listOf(clubsFive, heartsFour, spadesThree, spadesTwo, diamondsAce))
        )
        assertThat(hand.kickers, isEmpty)
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
        assertThat(
            hand.importantCards,
            equalTo(listOf(diamondsAce, diamondsKing, spadesQueen, diamondsJack, diamondsTen))
        )
        assertThat(hand.kickers, isEmpty)
    }

    @Test
    fun `Given six cards in row should return biggest Straight`() {
        val hand = createHand(
            diamondsTen,
            spadesEight,
            diamondsJack,
            spadesThree,
            spadesQueen,
            diamondsNine,
            diamondsKing
        )

        assertThat(hand.type, equalTo(HandType.Straight))
        assertThat(
            hand.importantCards,
            equalTo(listOf(diamondsKing, spadesQueen, diamondsJack, diamondsTen, diamondsNine))
        )
        assertThat(hand.kickers, isEmpty)
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

    @Test
    fun `Given three same rank cards twice should return Flush`() {
        val hand = createHand(
            clubsFive,
            heartsFive,
            spadesFive,
            heartsThree,
            clubsThree,
            heartsFour,
            spadesThree
        )

        assertThat(hand.type, equalTo(HandType.Full))
    }

    @Test
    fun `Given five cards in the same suite should return Flush`() {
        val hand = createHand(
            diamondsJack,
            heartsFive,
            diamondsKing,
            diamondsTen,
            diamondsAce,
            diamondsSix,
            spadesThree
        )

        assertThat(hand.type, equalTo(HandType.Flush))
    }

    @Test
    fun `Given all cards in the same suite should return Flush`() {
        val hand = createHand(
            diamondsJack,
            diamondsEight,
            diamondsKing,
            diamondsTen,
            diamondsAce,
            diamondsSix,
            diamondsSeven
        )

        assertThat(hand.type, equalTo(HandType.Flush))
    }

    @Test
    fun `Given four cards with same rank should return Four`() {
        val hand = createHand(
            clubsFive,
            diamondsEight,
            diamondsKing,
            heartsFive,
            diamondsFive,
            diamondsSix,
            spadesFive
        )

        assertThat(hand.type, equalTo(HandType.Four))
    }

    @Test
    fun `Given four and three cards with same rank should return Four`() {
        val hand = createHand(
            clubsFive,
            diamondsEight,
            clubsEight,
            heartsFive,
            diamondsFive,
            spadesEight,
            spadesFive
        )

        assertThat(hand.type, equalTo(HandType.Four))
    }

    @Test
    fun `Given straight in same suite should return StraightFlush`() {
        val hand = createHand(
            diamondsNine,
            diamondsEight,
            diamondsTen,
            diamondsJack,
            diamondsQueen,
            spadesEight,
            spadesFive
        )

        assertThat(hand.type, equalTo(HandType.StraightFlush))
    }

    @Test
    fun `Given royal straight return RoyalFlush`() {
        val hand = createHand(
            diamondsAce,
            diamondsKing,
            diamondsTen,
            diamondsJack,
            diamondsQueen,
            spadesEight,
            spadesFive
        )

        assertThat(hand.type, equalTo(HandType.RoyalFlush))
    }
}

