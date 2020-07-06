package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class CommunityCardsTest
{
    @Test
    fun `Community cards should being with no cards`()
    {
        val noCards = 0
        assertThat(CommunityCards().size , equalTo(noCards))
    }

    @Test
    fun `After flop community cards shuold have size of flop`()
    {
        val cardsAfterFlop = 3
        val flop1 = Card(Suit.Clubs, Rank.Ten)
        val flop2 = Card(Suit.Spades, Rank.Ten)
        val flop3 = Card(Suit.Clubs, Rank.King)

        assertThat(CommunityCards().flop(flop1, flop2, flop3).size, equalTo(cardsAfterFlop))
    }

}
