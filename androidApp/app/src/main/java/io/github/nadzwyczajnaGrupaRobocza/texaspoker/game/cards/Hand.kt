package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import java.lang.Integer.max

class Hand(river: RiverCommunityCards, pocketCards: PocketCards) {
    val cards = river.cards + pocketCards.card1 + pocketCards.card2

    val type: HandType = calculateHandType(cards)

    enum class StraightType {
        Normal,
        Flush,
        Royal
    }

    private fun calculateHandType(cards: Set<Card>): HandType {
        val pair = 2
        val three = 3
        val four = 4
        val oneElement = 1
        val twoElements = 2
        val fiveElements = 5

        val cardsByRank = cards.groupBy { it.rank }
        val pairsCount = cardsByRank.count { it.value.size == pair }
        val threesCount = cardsByRank.count { it.value.size == three }
        val foursCount = cardsByRank.count { it.value.size == four }

        val cardsBySuite = cards.groupBy { it.suit }
        val maxCardsInOneSuite = cardsBySuite.maxBy { it.value.size }?.value?.size

        val straightType = isStraight(cards)

        if (straightType == StraightType.Royal) return HandType.RoyalFlush
        if (straightType == StraightType.Flush) return HandType.StraightFlush
        if (foursCount == oneElement) return HandType.Four
        if (threesCount == oneElement && pairsCount >= oneElement) return HandType.Full
        maxCardsInOneSuite?.let { if (maxCardsInOneSuite >= fiveElements) return HandType.Flush }
        if (straightType == StraightType.Normal) return HandType.Straight
        if (threesCount == oneElement) return HandType.Three
        if (pairsCount == oneElement) return HandType.Pair
        if (pairsCount == twoElements) return HandType.TwoPairs

        return HandType.HighCard
    }

    private fun isStraight(cards: Set<Card>): StraightType? {
        val cardsSortedByRank = cards.sortedBy { it.rank }
        return isStraight(cards = cardsSortedByRank, previousCard = cardsSortedByRank.last())
    }

    private fun isStraight(
        cards: List<Card>,
        previousCard: Card,
        cardsInRow: Int = 0,
        maxCardsInRow: Int = 0,
        suites: List<Suit> = emptyList()
    ): StraightType? {
        val newMax = max(maxCardsInRow, cardsInRow)
        if (cards.isEmpty()) {
            val suiteCount = suites.groupBy { it }
            val maxSuiteCount = suiteCount.maxBy { it.value.size }?.value?.size ?: 0
            return when {
                newMax < 5 -> null
                maxSuiteCount < 5 -> StraightType.Normal
                previousCard.rank == Rank.Ace -> StraightType.Royal
                else -> StraightType.Flush
            }
        }
        val thisCard = cards.first()
        val diff = thisCard.rank - previousCard.rank
        return diffBasedDecision(diff, cards, thisCard, previousCard, cardsInRow, newMax, suites)
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
            return isStraight(
                cards.subList(1, cards.size),
                thisCard,
                max(cardsInRow + 1, 2),
                newMax,
                (suites + thisCard.suit + if (cardsInRow == 0) previousCard.suit else null).filter { it != null } as List<Suit>
            )
        if (diff == 0)
            return isStraight(
                cards.subList(1, cards.size),
                thisCard,
                cardsInRow,
                newMax,
                (suites + thisCard.suit + if (cardsInRow == 0) previousCard.suit else null).filter { it != null } as List<Suit>
            )
        return isStraight(
            cards = cards.subList(1, cards.size),
            previousCard = thisCard,
            maxCardsInRow = newMax,
            suites = emptyList()
        )
    }


}

