package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

enum class Suit {
    Spades,
    Hearts,
    Clubs,
    Diamonds
}

fun toUnicode(suit: Suit) = when (suit) {
    Suit.Spades -> {
        "\u2660"
    }
    Suit.Hearts -> {
        "\u2665"
    }
    Suit.Clubs -> {
        "\u2663"
    }
    Suit.Diamonds -> {
        "\u2666"
    }
}
