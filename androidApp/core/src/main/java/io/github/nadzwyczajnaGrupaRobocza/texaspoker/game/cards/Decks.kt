package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

typealias Deck = List<Card>

class Decks {
    companion object {
        val ordededDeck: Set<Card> =
            Suit.values().flatMap { suit -> Rank.values().map { rank -> Card(suit, rank) } }.toSet()

        fun shuffledDeck() = shuffle(ordededDeck, emptyList())

        private tailrec fun shuffle(deck: Set<Card>, cards: List<Card>): Deck = when {
            deck.isEmpty() -> cards
            else -> {
                val nextCard = deck.random()
                shuffle(deck - nextCard, cards + nextCard)
            }
        }
    }
}

