package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Card(val suit: Suit, val rank: Rank) {

    override fun toString(): String {
        return "${toUnicode(
            suit
        )}$rank"
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