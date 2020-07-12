package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Hand(river: RiverCommunityCards, pocketCards: PocketCards) {
    val cards = river.cards + pocketCards.card1 + pocketCards.card2

    val type: HandType = calculateHandType(cards)

    private fun calculateHandType(cards: Set<Card>): HandType {
        val pair = 2
        val onePair = 1
        val twoPairs = 2
        val cardsByRank = cards.groupBy { it.rank }
        val pairsCount = cardsByRank.count { it.value.size == pair }
        if (pairsCount == onePair)  return HandType.Pair
        if (pairsCount == twoPairs) return HandType.TwoPairs

        return HandType.HighCard
    }


}
