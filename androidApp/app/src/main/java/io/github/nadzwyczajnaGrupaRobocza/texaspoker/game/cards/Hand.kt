package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Hand(river: RiverCommunityCards, pocketCards: PocketCards) {
    val cards = river.cards + pocketCards.card1 + pocketCards.card2

    val type: HandType = calculateHandType(cards)

    private fun calculateHandType(cards: Set<Card>): HandType {
        if (cards.size == cards.distinctBy { it.rank }.size + 1) return HandType.Pair

        return HandType.HighCard
    }


}
