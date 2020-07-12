package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Hand(river: RiverCommunityCards, pocketCards: PocketCards) {
    val cards = river.cards + pocketCards.card1 + pocketCards.card2

    val type: HandType = calculateHandType(cards)

    private fun calculateHandType(cards: Set<Card>): HandType {
        val pair = 2
        val three = 3
        val oneElement = 1
        val twoElements = 2

        val cardsByRank = cards.groupBy { it.rank }

        val pairsCount = cardsByRank.count { it.value.size == pair }
        val threesCount = cardsByRank.count { it.value.size == three }
        if (threesCount == oneElement) return HandType.Three
        if (pairsCount == oneElement) return HandType.Pair
        if (pairsCount == twoElements) return HandType.TwoPairs

        return HandType.HighCard
    }


}
