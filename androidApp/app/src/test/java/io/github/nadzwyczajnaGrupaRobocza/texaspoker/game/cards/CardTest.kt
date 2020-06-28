package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNot.not

import org.junit.Test

class CardTest {

    @Test
    fun `Create Card`() {
        val card = Card(Suit.Clubs)
    }

    @Test
    fun `Cards should be equal with same suite`() {
        assertThat(Card(Suit.Clubs), equalTo(Card(Suit.Clubs)))
    }

    @Test
    fun `Cards should differ with suite`() {
        assertThat(Card(Suit.Diamonds), not(Card(Suit.Clubs)))
    }

    @Test
    fun ``() {

    }
}
