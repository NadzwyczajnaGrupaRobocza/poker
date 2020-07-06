package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards


class NoCommunityCards {
    fun flop(flop1: Card, flop2: Card, flop3: Card): FlopCommunityCards {
        return FlopCommunityCards(this, flop1, flop2, flop3)
    }

    val size = 0


}

class FlopCommunityCards(communityCards: NoCommunityCards, flop1: Card, flop2: Card, flop3: Card) {
    val size = 3
}
