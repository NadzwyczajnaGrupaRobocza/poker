package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.junit.Test

class HandTest
{
    @Test
    fun `Hand should have two cards`()
    {
        val hand = Hand(Card(Suit.Hearts, Rank.Seven), Card(Suit.Clubs, Rank.Queen))
    }

}
