package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class PocketCardsTest
{
    private val anyCard1 = Card(Suit.Clubs, Rank.Queen)
    private val anyCard2 = Card(Suit.Hearts, Rank.Ace)

    @Test
    fun `Hand should have five cards`()
    {
        PocketCards(anyCard1, anyCard2)
    }

}
