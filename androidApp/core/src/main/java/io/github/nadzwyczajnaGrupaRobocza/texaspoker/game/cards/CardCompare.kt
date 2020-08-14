package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

fun Card.hasSameRank(rhs: Card) = this.rank == rhs.rank

fun Card.hasSameSuit(rhs: Card) = this.suit == rhs.suit
