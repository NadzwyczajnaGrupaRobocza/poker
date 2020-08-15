package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

class Deck {
    val deck: Set<Card> =
        Suit.values().flatMap { suit -> Rank.values().map { rank -> Card(suit, rank) } }.toSet()
}

