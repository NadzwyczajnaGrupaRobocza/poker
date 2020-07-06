package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Hand(river: RiverCommunityCards, pockerCards: PocketCards) {
    val cards = river.cards + pockerCards.card1 + pockerCards.card2
}
