package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Deck {
    val deck: Set<Card> =
        Suit.values().flatMap { suit -> Rank.values().map { rank -> Card(suit, rank) } }.toSet()

    fun shuffledDeck() = shuffle(deck, emptyList())

    private fun shuffle(deck: Set<Card>, cards: List<Card>): List<Card> = when {
        deck.isEmpty() -> cards
        else -> {
            val nextCard = deck.random()
            shuffle(deck - nextCard, cards + nextCard)
        }
    }
}

