package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import java.lang.Integer.max


class Hand(river: RiverCommunityCards, pocketCards: PocketCards) : Comparable<Hand> {
    val cards = (river.cards + pocketCards.card1 + pocketCards.card2).toSet()

    override fun compareTo(other: Hand) =
        when {
            this.type < other.type -> less
            this.type > other.type -> bigger
            else -> compareFirstImportantCardsThenKickers(this, other)
        }

    override fun equals(other: Any?): Boolean = when (other) {
        is Hand -> compareTo(other) == 0
        else -> false
    }


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

    private class Straight(val type: StraightType, val straightCards: List<Card>)

    private fun calculateInternalHand(cards: Set<Card>): InternalHand {
        val oneElement = 1
        val twoElements = 2
        val fiveElements = handCardsCount

        val cardsByRank = groupCardsByRank(cards)
        val (pairs, threes, fours) = calculateMultipleRanks(cardsByRank)
        val cardsBySuite = groupCardsBySuit(cards)
        val maxCardsInOneSuite = calculateMaxCardsInOneSuit(cardsBySuite)
        val straight = calculateStraight(cards)

        if (straight?.type == StraightType.Royal) return getStraightInternalHand(
            HandType.RoyalFlush,
            straight.straightCards
        )
        if (straight?.type == StraightType.Flush) return getStraightInternalHand(
            HandType.StraightFlush,
            straight.straightCards
        )
        if (fours.size == oneElement) return getFourInternalHand(fours)
        if (threes.size == twoElements || threes.size == oneElement && pairs.size >= oneElement) return getFullInternalHand(
            threes,
            pairs
        )
        maxCardsInOneSuite?.let {
            if (maxCardsInOneSuite >= fiveElements) return getFlushInternalHand(cardsBySuite)
        }
        if (straight?.type == StraightType.Normal) return getStraightInternalHand(
            HandType.Straight,
            straight.straightCards
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

    private fun getFullInternalHand(
        threes: List<List<Card>>,
        pairs: List<List<Card>>
    ): InternalHand {
        val flushRankCount = 2
        val flushRanks =
            (threes.map { it.first().rank } + pairs.map { it.first().rank }).sortedDescending()
                .take(flushRankCount)
        return getInternalHand(HandType.Full, handCardsCount) { flushRanks.contains(it.rank) }
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

    private fun getFourInternalHand(fours: List<List<Card>>): InternalHand {
        val fourRank = fours.first().first().rank
        return getInternalHand(HandType.Four, 4) { it.rank == fourRank }
    }

    private fun getThreeInternalHand(threes: List<List<Card>>): InternalHand {
        val threeRank = threes.first().first().rank
        return getInternalHand(HandType.Three, 3) { it.rank == threeRank }
    }

    private fun getStraightInternalHand(hand: HandType, cards: List<Card>): InternalHand {
        return InternalHand(hand, cards, emptyList())
    }

    private fun calculateMaxCardsInOneSuit(cardsBySuite: Map<Suit, List<Card>>): Int? {
        return cardsBySuite.maxByOrNull { it.value.size }?.value?.size
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
            straightCards = listOf(cards.last())
        )
    }

    private fun calculateStraight(
        cards: List<Card>,
        previousCard: Card,
        cardsInRow: Int = 0,
        maxCardsInRow: Int = 0,
        suites: List<Suit> = emptyList(),
        straightCards: List<Card>
    ): Straight? {
        val newMax = max(maxCardsInRow, cardsInRow)
        if (cards.isEmpty()) {
            return calculateStraightType(
                suites,
                newMax,
                previousCard,
                straightCards.reversed()
            )
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
            straightCards
        )
    }

    private fun calculateStraightType(
        suites: List<Suit>,
        newMax: Int,
        previousCard: Card,
        straightCards: List<Card>
    ): Straight? {
        val suiteCount = suites.groupBy { it }
        val maxSuitEntry = suiteCount.maxByOrNull { it.value.size }
        val maxSuiteCount = maxSuitEntry?.value?.size ?: 0
        val maxSuit = maxSuitEntry?.key
        return when {
            newMax < 5 -> null
            maxSuiteCount < 5 -> Straight(StraightType.Normal, getFiveMaxRankedCards(straightCards))
            previousCard.rank == Rank.Ace -> Straight(
                StraightType.Royal,
                getFiveMaxRankedCards(straightCards.filter { it.suit == maxSuit })
            )
            else -> Straight(
                StraightType.Flush,
                getFiveMaxRankedCards(straightCards.filter { it.suit == maxSuit })
            )
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
        straightCards: List<Card>
    ) =
        when (diff) {
            1, -12 -> calculateStraight(
                cards.subList(1, cards.size),
                thisCard,
                max(cardsInRow + 1, 2),
                newMax,
                (suites + thisCard.suit + if (cardsInRow == 0) previousCard.suit else null).filterNotNull(),
                (if (cardsInRow == 0) listOf(previousCard) else emptyList()) + straightCards + thisCard
            )
            0 -> calculateStraight(
                cards.subList(1, cards.size),
                thisCard,
                cardsInRow,
                newMax,
                (suites + thisCard.suit + if (cardsInRow == 0) previousCard.suit else null).filterNotNull(),
                (if (cardsInRow == 0) listOf(previousCard) else emptyList()) + straightCards + thisCard
            )
            else -> calculateStraight(
                cards = cards.subList(1, cards.size),
                previousCard = thisCard,
                maxCardsInRow = newMax,
                suites = emptyList(),
                straightCards = if (newMax >= handCardsCount) straightCards else emptyList()
            )
        }

    private fun getFiveMaxRankedCards(cards: List<Card>) =
        cards.filterOutDuplicatedRanks().take(handCardsCount)

    override fun hashCode(): Int {
        return cards.hashCode()
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

fun List<Card>.filterOutDuplicatedRanks() =
    when {
        isEmpty() -> this
        else -> listOf(first()) + this.takeLast(size - 1)
            .filterIndexed { index, card -> this[index].rank != card.rank }
    }
