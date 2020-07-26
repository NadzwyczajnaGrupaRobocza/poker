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

    private fun calculateInternalHand(cards: Set<Card>): InternalHand {
        val oneElement = 1
        val twoElements = 2
        val fiveElements = 5

        val cardsByRank = groupCardsByRank(cards)
        val (pairs, threes, fours) = calculateMultipleRanks(cardsByRank)
        val maxCardsInOneSuite = calculateMaxCardsInOneSuit(cards)
        val straightType = calculateStraight(cards)

        if (straightType == StraightType.Royal) return InternalHand(
            HandType.RoyalFlush,
            emptyList(),
            emptyList()
        )
        if (straightType == StraightType.Flush) return InternalHand(
            HandType.StraightFlush,
            emptyList(),
            emptyList()
        )
        if (fours.size == oneElement) return InternalHand(HandType.Four, emptyList(), emptyList())
        if (threes.size == oneElement && pairs.size >= oneElement) return InternalHand(
            HandType.Full,
            emptyList(),
            emptyList()
        )
        maxCardsInOneSuite?.let {
            if (maxCardsInOneSuite >= fiveElements) return InternalHand(
                HandType.Flush,
                emptyList(),
                emptyList()
            )
        }
        if (straightType == StraightType.Normal) return InternalHand(
            HandType.Straight,
            emptyList(),
            emptyList()
        )
        if (threes.size == oneElement) return InternalHand(HandType.Three, emptyList(), emptyList())
        if (pairs.size == oneElement) return getPairInternalHand(pairs, cards)
        if (pairs.size >= twoElements) return InternalHand(
            HandType.TwoPairs,
            emptyList(),
            emptyList()
        )

        return InternalHand(
            HandType.HighCard,
            cards.sortedByDescending { it.rank }.take(5),
            emptyList()
        )
    }

    private fun getPairInternalHand(
        pairs: List<List<Card>>,
        cards: Set<Card>
    ): InternalHand {
        val pairRank = pairs.first().first().rank
        return InternalHand(
            HandType.Pair,
            cards.filter { it.rank == pairRank },
            cards.filter { it.rank != pairRank }.sortedByDescending { it.rank }.take(3)
        )
    }

    private fun calculateMaxCardsInOneSuit(cards: Set<Card>): Int? {
        val cardsBySuite = groupCardsBySuit(cards)
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

    private fun calculateStraight(cards: Set<Card>): StraightType? {
        val cardsSortedByRank = cards.sortedBy { it.rank }
        return calculateStraight(cards = cardsSortedByRank, previousCard = cardsSortedByRank.last())
    }

    private fun calculateStraight(
        cards: List<Card>,
        previousCard: Card,
        cardsInRow: Int = 0,
        maxCardsInRow: Int = 0,
        suites: List<Suit> = emptyList()
    ): StraightType? {
        val newMax = max(maxCardsInRow, cardsInRow)
        if (cards.isEmpty()) {
            return calculateStraightType(suites, newMax, previousCard)
        }
        val thisCard = cards.first()
        val diff = thisCard.rank - previousCard.rank
        return diffBasedDecision(diff, cards, thisCard, previousCard, cardsInRow, newMax, suites)
    }

    private fun calculateStraightType(
        suites: List<Suit>,
        newMax: Int,
        previousCard: Card
    ): StraightType? {
        val suiteCount = suites.groupBy { it }
        val maxSuiteCount = suiteCount.maxBy { it.value.size }?.value?.size ?: 0
        return when {
            newMax < 5 -> null
            maxSuiteCount < 5 -> StraightType.Normal
            previousCard.rank == Rank.Ace -> StraightType.Royal
            else -> StraightType.Flush
        }
    }

    private fun diffBasedDecision(
        diff: Int,
        cards: List<Card>,
        thisCard: Card,
        previousCard: Card,
        cardsInRow: Int,
        newMax: Int,
        suites: List<Suit>
    ): StraightType? {
        if (diff == 1 || diff == -12)
            return calculateStraight(
                cards.subList(1, cards.size),
                thisCard,
                max(cardsInRow + 1, 2),
                newMax,
                (suites + thisCard.suit + if (cardsInRow == 0) previousCard.suit else null).filterNotNull()
            )
        if (diff == 0)
            return calculateStraight(
                cards.subList(1, cards.size),
                thisCard,
                cardsInRow,
                newMax,
                (suites + thisCard.suit + if (cardsInRow == 0) previousCard.suit else null).filterNotNull()
            )
        return calculateStraight(
            cards = cards.subList(1, cards.size),
            previousCard = thisCard,
            maxCardsInRow = newMax,
            suites = emptyList()
        )
    }
}

