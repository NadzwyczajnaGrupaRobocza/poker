package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNot.not

import org.junit.Test

class CardTest {
    private val twoClubs = Card(Suit.Clubs, Rank.Two)
    private val twoDiamonds = Card(Suit.Diamonds, Rank.Two)
    private val threeClubs = Card(Suit.Clubs, Rank.Three)

    @Test
    fun `Create Card`() {
        Card(Suit.Clubs, Rank.King)
    }

    @Test
    fun `Cards should be equal`() {
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
