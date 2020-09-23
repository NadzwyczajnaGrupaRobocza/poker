package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

data class Card(val suit: Suit, val rank: Rank) {
    override fun toString(): String {
        return "${toUnicode(suit)}${toUnicode(rank)}"
    }
}
