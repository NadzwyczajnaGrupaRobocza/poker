package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

typealias Deck = List<Card>

class Decks {
    companion object {
        val ordededDeck: Set<Card> =
            Suit.values().flatMap { suit -> Rank.values().map { rank -> Card(suit, rank) } }.toSet()

        fun shuffledDeck() = ordededDeck.toList().shuffled()
    }
}

