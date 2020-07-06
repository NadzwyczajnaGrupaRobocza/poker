package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

interface CommunityCards {
    val size: Int
}

class NoCommunityCards : CommunityCards {
    override val size = 0

    fun flop(flop1: Card, flop2: Card, flop3: Card) =
        FlopCommunityCards(this, flop1, flop2, flop3)
}

class FlopCommunityCards(communityCards: NoCommunityCards, flop1: Card, flop2: Card, flop3: Card) :
    CommunityCards {
    override val size = 3

    fun turn(turn: Card) = TurnCommunityCards(this, turn)
}

class TurnCommunityCards(flop: FlopCommunityCards, turn: Card) : CommunityCards {
    fun river(river: Card) = RiverCommunityCards(this, river)

    override val size = 4
}

class RiverCommunityCards(turn: TurnCommunityCards, river: Card) : CommunityCards {
    override val size = 5
}
