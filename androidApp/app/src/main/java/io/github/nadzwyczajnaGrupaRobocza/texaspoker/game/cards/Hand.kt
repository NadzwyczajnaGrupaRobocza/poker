package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Hand(river: RiverCommunityCards, pocketCards: PocketCards) {
    val cards = river.cards + pocketCards.card1 + pocketCards.card2

    val type: HandType = calculateHandType(cards)

    private fun calculateHandType(cards: Set<Card>): HandType {
        val pair = 2
        val three = 3
        val oneElement = 1
        val twoElements = 2
        val fiveElements = 5

        val cardsByRank = cards.groupBy { it.rank }
        val pairsCount = cardsByRank.count { it.value.size == pair }
        val threesCount = cardsByRank.count { it.value.size == three }

        val cardsBySuite = cards.groupBy { it.suit }
        val maxCardsInOneSuite = cardsBySuite.maxBy { it.value.size }?.value?.size

        if (threesCount == oneElement && pairsCount >= oneElement ) return HandType.Full
        if (maxCardsInOneSuite == fiveElements) return HandType.Flush
        if (isStraight(cards)) return HandType.Straight
        if (threesCount == oneElement) return HandType.Three
        if (pairsCount == oneElement) return HandType.Pair
        if (pairsCount == twoElements) return HandType.TwoPairs

        return HandType.HighCard
    }

    private fun isStraight(cards: Set<Card>): Boolean {
        val cardsSortedByRank = cards.sortedBy { it.rank }
        val cardsDiffs = cardsSortedByRank.take(cards.size - 1)
            .mapIndexed { index, card -> cardsSortedByRank.elementAt(index + 1).rank - card.rank } + (cardsSortedByRank.last().rank - cardsSortedByRank.first().rank)
        val stringDiff = cardsDiffs.fold("") {acc: String, i: Int -> "$acc$i|" }
        val fourDiffsInRow = "1|1|1|1|"
        val beginAceToFiveStraight = "1|1|1|"
        val endAceToFiveStraight = "12|"
        return  fourDiffsInRow in stringDiff  || stringDiff.startsWith(beginAceToFiveStraight) && stringDiff.endsWith(endAceToFiveStraight)
    }


}

