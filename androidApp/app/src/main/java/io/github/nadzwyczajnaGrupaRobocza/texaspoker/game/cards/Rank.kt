package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

enum class Rank {
    Ace,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten,
    Jack,
    Queen,
    King
}

fun toString(rank: Rank) = when (rank) {
    Rank.Ace -> {
        "A"
    }
    Rank.Two -> {
        "2"
    }
    Rank.Three -> {
        "3"
    }
    Rank.Four -> {
        "4"
    }
    Rank.Five -> {
        "5"
    }
    Rank.Six -> {
        "6"
    }
    Rank.Seven -> {
        "7"
    }
    Rank.Eight -> {
        "8"
    }
    Rank.Nine -> {
        "9"
    }
    Rank.Ten -> {
        "10"
    }
    Rank.Jack -> {
        "J"
    }
    Rank.Queen -> {
        "Q"
    }
    Rank.King -> {
        "K"
    }
}

operator fun Rank.minus(rank: Rank) = this.ordinal - rank.ordinal
