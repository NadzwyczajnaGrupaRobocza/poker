package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class CommunityCards() {
    private constructor(flop1: Card, flop2: Card, flop3: Card) : this() {
        this.size = 3
    }
    fun flop(flop1: Card, flop2: Card, flop3: Card): CommunityCards {
        return CommunityCards(flop1, flop2, flop3)
    }

    var size = 0
}
