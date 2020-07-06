package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class PocketCardsTest
{
    val anyCard1 = Card(Suit.Clubs, Rank.Queen)
    val anyCard2 = Card(Suit.Hearts, Rank.Ace)

    @Test
    fun `Hand should have five cards`()
    {
        val hand = PocketCards(anyCard1, anyCard2)
    }

}
