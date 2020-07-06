package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

interface CommunityCards {
    val size: Int
}

class NoCommunityCards : CommunityCards {
    fun flop(flop1: Card, flop2: Card, flop3: Card): FlopCommunityCards {
        return FlopCommunityCards(this, flop1, flop2, flop3)
    }

    override val size = 0
}

class FlopCommunityCards(communityCards: NoCommunityCards, flop1: Card, flop2: Card, flop3: Card) :
    CommunityCards {
    override val size = 3
}
