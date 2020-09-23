package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

interface CommunityCards {
    val size: Int
    val cards: List<Card>
}

class NoCommunityCards : CommunityCards {
    override val size
        get() = cards.size
    override val cards
        get() = emptyList<Card>()

    fun flop(flop1: Card, flop2: Card, flop3: Card) =
        FlopCommunityCards(flop1, flop2, flop3)
}

class FlopCommunityCards internal constructor(flop1: Card, flop2: Card, flop3: Card) :
    CommunityCards {
    override val size = 3
    override val cards = listOf(flop1, flop2, flop3)

    fun turn(turn: Card) = TurnCommunityCards(this, turn)
}

class TurnCommunityCards(flop: FlopCommunityCards, val turn: Card) : CommunityCards {
    override val size = 4
    override val cards = flop.cards + turn

    fun river(river: Card) = RiverCommunityCards(this, river)
}

class RiverCommunityCards(turn: TurnCommunityCards, val river: Card) : CommunityCards {
    override val size = 5
    override val cards = turn.cards + river
}
