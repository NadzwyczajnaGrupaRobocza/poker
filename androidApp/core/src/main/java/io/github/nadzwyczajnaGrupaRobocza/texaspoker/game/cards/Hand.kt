package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import java.lang.Integer.max

class Hand(river: RiverCommunityCards, pocketCards: PocketCards) {
    val cards = river.cards + pocketCards.card1 + pocketCards.card2

    private data class InternalHand(
        val type: HandType,
        val handTypeCards: List<Card>,
        val kickers: List<Card>
    )

    private val internalHand = calculateInternalHand(cards)

    val type: HandType
        get() = internalHand.type
    val importantCards
        get() = internalHand.handTypeCards
    val kickers
        get() = internalHand.kickers

    private enum class StraightType {
        Normal,
        Flush,
        Royal
    }

    private class Straight(val type: StraightType, val highCard: Card) {
    }

    private fun calculateInternalHand(cards: Set<Card>): InternalHand {
        val oneElement = 1
        val twoElements = 2
        val fiveElements = handCardsCount

        val cardsByRank = groupCardsByRank(cards)
        val (pairs, threes, fours) = calculateMultipleRanks(cardsByRank)
        val cardsBySuite = groupCardsBySuit(cards)
        val maxCardsInOneSuite = calculateMaxCardsInOneSuit(cardsBySuite)
        val straight = calculateStraight(cards)

        if (straight?.type == StraightType.Royal) return InternalHand(
            HandType.RoyalFlush,
            emptyList(),
            emptyList()
        )
        if (straight?.type == StraightType.Flush) return InternalHand(
            HandType.StraightFlush,
            emptyList(),
            emptyList()
        )
        if (fours.size == oneElement) return InternalHand(HandType.Four, emptyList(), emptyList())
        if (threes.size == twoElements || threes.size == oneElement && pairs.size >= oneElement) return InternalHand(
            HandType.Full,
            emptyList(),
            emptyList()
        )
        maxCardsInOneSuite?.let {
            if (maxCardsInOneSuite >= fiveElements) return getFlushInternalHand(cardsBySuite)
        }
        if (straight?.type == StraightType.Normal) return getStraightInternalHand(
            HandType.Straight,
            straight.highCard
        )
        if (threes.size == oneElement) return getThreeInternalHand(threes)
        if (pairs.size == oneElement) return getPairInternalHand(pairs)
        if (pairs.size >= twoElements) return getTwoPairsInternalHand(pairs)

        return InternalHand(
            HandType.HighCard,
            cards.sortedByDescending { it.rank }.take(handCardsCount),
            emptyList()
        )
    }

    private fun getFlushInternalHand(cardsBySuite: Map<Suit, List<Card>>): InternalHand {
        val suit = cardsBySuite.toList().find { it.second.size >= handCardsCount }?.first
        return getInternalHand(HandType.Flush, handCardsCount) { it.suit == suit }
    }

    private fun getInternalHand(
        type: HandType,
        handCardCount: Int,
        handCardFilter: (Card) -> Boolean
    ) =
        InternalHand(
            type,
            cards.getNDescendingConformingPredicate(handCardFilter, handCardCount),
            cards.getNDescendingNotConformingPredicate(
                handCardFilter,
                handCardsCount - handCardCount
            )
        )


    private fun getPairInternalHand(pairs: List<List<Card>>): InternalHand {
        val pairRank = pairs.first().first().rank
        return getInternalHand(HandType.Pair, 2) { it.rank == pairRank }
    }

    private fun getTwoPairsInternalHand(pairs: List<List<Card>>): InternalHand {
        val topTwoRanks =
            pairs.sortedByDescending { it.first().rank }.take(2).map { it.first().rank }
        return getInternalHand(
            HandType.TwoPairs,
            4
        ) { card: Card -> topTwoRanks.find { it == card.rank } != null }
    }

    private fun getThreeInternalHand(threes: List<List<Card>>): InternalHand {
        val threeRank = threes.first().first().rank
        return getInternalHand(HandType.Three, 3) { it.rank == threeRank }
    }

    private fun getStraightInternalHand(hand: HandType, highCard: Card): InternalHand {
        return InternalHand(hand, listOf(highCard), emptyList())
    }

    private fun calculateMaxCardsInOneSuit(cardsBySuite: Map<Suit, List<Card>>): Int? {
        return cardsBySuite.maxBy { it.value.size }?.value?.size
    }

    private fun calculateMultipleRanks(cardsByRank: Map<Rank, List<Card>>): Triple<List<List<Card>>, List<List<Card>>, List<List<Card>>> {
        val pair = 2
        val three = 3
        val four = 4
        val pairs = cardsByRank.filter { it.value.size == pair }.map { it.value }
        val threes = cardsByRank.filter { it.value.size == three }.map { it.value }
        val fours = cardsByRank.filter { it.value.size == four }.map { it.value }
        return Triple(pairs, threes, fours)
    }

    private fun groupCardsBySuit(cards: Set<Card>): Map<Suit, List<Card>> {
        return cards.groupBy { it.suit }
    }

    private fun groupCardsByRank(cards: Set<Card>): Map<Rank, List<Card>> {
        return cards.groupBy { it.rank }
    }

    private fun calculateStraight(cards: Set<Card>): Straight? {
        val cardsSortedByRank = cards.sortedBy { it.rank }
        return calculateStraight(
            cards = cardsSortedByRank,
            previousCard = cardsSortedByRank.last(),
            bestStraightCard = cardsSortedByRank.first()
        )
    }

    private fun calculateStraight(
        cards: List<Card>,
        previousCard: Card,
        cardsInRow: Int = 0,
        maxCardsInRow: Int = 0,
        suites: List<Suit> = emptyList(),
        bestStraightCard: Card
    ): Straight? {
        val newMax = max(maxCardsInRow, cardsInRow)
        if (cards.isEmpty()) {
            return calculateStraightType(suites, newMax, previousCard, bestStraightCard)
        }
        val thisCard = cards.first()
        val diff = thisCard.rank - previousCard.rank
        return diffBasedDecision(
            diff,
            cards,
            thisCard,
            previousCard,
            cardsInRow,
            newMax,
            suites,
            bestStraightCard
        )
    }

    private fun calculateStraightType(
        suites: List<Suit>,
        newMax: Int,
        previousCard: Card,
        bestStraightCard: Card
    ): Straight? {
        val suiteCount = suites.groupBy { it }
        val maxSuiteCount = suiteCount.maxBy { it.value.size }?.value?.size ?: 0
        return when {
            newMax < 5 -> null
            maxSuiteCount < 5 -> Straight(StraightType.Normal, bestStraightCard)
            previousCard.rank == Rank.Ace -> Straight(StraightType.Royal, bestStraightCard)
            else -> Straight(StraightType.Flush, bestStraightCard)
        }
    }

    private fun diffBasedDecision(
        diff: Int,
        cards: List<Card>,
        thisCard: Card,
        previousCard: Card,
        cardsInRow: Int,
        newMax: Int,
        suites: List<Suit>,
        bestStraightCard: Card
    ): Straight? {
        if (diff == 1 || diff == -12)
            return calculateStraight(
                cards.subList(1, cards.size),
                thisCard,
                max(cardsInRow + 1, 2),
                newMax,
                (suites + thisCard.suit + if (cardsInRow == 0) previousCard.suit else null).filterNotNull(),
                thisCard
            )
        if (diff == 0)
            return calculateStraight(
                cards.subList(1, cards.size),
                thisCard,
                cardsInRow,
                newMax,
                (suites + thisCard.suit + if (cardsInRow == 0) previousCard.suit else null).filterNotNull(),
                thisCard
            )
        return calculateStraight(
            cards = cards.subList(1, cards.size),
            previousCard = thisCard,
            maxCardsInRow = newMax,
            suites = emptyList(),
            bestStraightCard = bestStraightCard
        )
    }

    companion object {
        private const val handCardsCount = 5
    }
}

fun Set<Card>.getNDescendingConformingPredicate(
    predicate: (Card) -> Boolean,
    numberOfCards: Int
) =
    this.filter(predicate).getNDescending(numberOfCards)

fun Set<Card>.getNDescendingNotConformingPredicate(
    predicate: (Card) -> Boolean,
    numberOfCards: Int
) =
    this.filterNot(predicate).getNDescending(numberOfCards)

fun List<Card>.getNDescending(
    numberOfCards: Int
) =
    this.sortedByDescending { it.rank }.take(numberOfCards)
