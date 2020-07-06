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
    fun `Flop should be added to community cards`()
    {}

}
