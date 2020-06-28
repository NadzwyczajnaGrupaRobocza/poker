package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Card(val suit: Suit) {

    override fun toString(): String {
        return "Card: ${toUnicode(suit)}"
    }

    override fun hashCode(): Int {
        return suit.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return hashCode() == other.hashCode()
    }
}