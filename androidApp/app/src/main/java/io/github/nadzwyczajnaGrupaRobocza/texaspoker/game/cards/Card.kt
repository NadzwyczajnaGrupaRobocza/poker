package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Card(private val suit: Suit, private val rank : Rank) {

    override fun toString(): String {
        return "Card: ${toUnicode(suit)}"
    }

    override fun equals(other: Any?): Boolean {
        return hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        var result = suit.hashCode()
        result = 31 * result + rank.hashCode()
        return result
    }
}