package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Card
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.PocketCards
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Rank
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Suit
import org.junit.Test

class PocketCardsTest
{
    val anyCard1 = Card(
        Suit.Clubs,
        Rank.Queen
    )
    val anyCard2 = Card(
        Suit.Hearts,
        Rank.Ace
    )

    @Test
    fun `Hand should have five cards`()
    {
        PocketCards(
            anyCard1,
            anyCard2
        )
    }

}
