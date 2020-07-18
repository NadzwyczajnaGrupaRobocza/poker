package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import java.lang.Integer.max

class Hand(river: RiverCommunityCards, pocketCards: PocketCards) {
    val cards = river.cards + pocketCards.card1 + pocketCards.card2

    val type: HandType = calculateHandType(cards)

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

        if (foursCount == oneElement) return HandType.Four
        if (threesCount == oneElement && pairsCount >= oneElement) return HandType.Full
        maxCardsInOneSuite?.let { if (maxCardsInOneSuite >= fiveElements) return HandType.Flush }
        if (isStraight(cards)) return HandType.Straight
        if (threesCount == oneElement) return HandType.Three
        if (pairsCount == oneElement) return HandType.Pair
        if (pairsCount == twoElements) return HandType.TwoPairs

        return HandType.HighCard
    }

    private fun isStraight(cards: Set<Card>): Boolean {
        val cardsSortedByRank = cards.sortedBy { it.rank }
        return isStraight(cards = cardsSortedByRank, previousCard = cardsSortedByRank.last())
    }

    private fun isStraight(
        cards: List<Card>,
        previousCard: Card,
        cardsInRow: Int = 0,
        maxCardsInRow: Int = 0
    ): Boolean {
        val newMax = max(maxCardsInRow, cardsInRow)
        if (cards.isEmpty())
            return newMax >= 5
        val thisCard = cards.first()
        val diff = thisCard.rank - previousCard.rank
        if (diff == 1 || diff == -12)
            return isStraight(
                cards.subList(1, cards.size),
                thisCard,
                max(cardsInRow + 1, 2),
                newMax
            )
        if (diff == 0)
            return isStraight(cards.subList(1, cards.size), thisCard, cardsInRow, newMax)
        return isStraight(
            cards = cards.subList(1, cards.size),
            previousCard = thisCard,
            maxCardsInRow = newMax
        )
    }


}

