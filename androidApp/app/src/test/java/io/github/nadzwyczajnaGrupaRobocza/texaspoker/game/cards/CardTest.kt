package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNot.not

import org.junit.Test

class CardTest {
    val twoClubs = Card(Suit.Clubs, Rank.Two)
    val twoDiamonds = Card(Suit.Diamonds, Rank.Two)
    val threeClubs = Card(Suit.Clubs, Rank.Three)

    @Test
    fun `Create Card`() {
        val card = Card(Suit.Clubs, Rank.King)
    }

    @Test
    fun `Cards should be equal with same suite`() {
        assertThat(twoClubs, equalTo(twoClubs))
    }

    @Test
    fun `Cards should differ with suite`() {
        assertThat(twoClubs, not(twoDiamonds))
    }

    @Test
    fun `Cards should differ with rank`() {
        assertThat(twoClubs, not(threeClubs))
    }
}
