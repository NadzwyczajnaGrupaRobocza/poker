package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CardCompareTest {
    private val twoClubs = Card(Suit.Clubs, Rank.Two)
    private val twoDiamonds = Card(Suit.Diamonds, Rank.Two)
    private val threeClubs = Card(Suit.Clubs, Rank.Three)

    @Test
    fun `Create Card`() {
        val card = Card(Suit.Clubs, Rank.King)
    }

    @Test
    fun `Cards with same suit should have same suit`() {
        assertThat(twoClubs.hasSameSuit(threeClubs), equalTo(true))
    }

    @Test
    fun `Cards with different suit should have different suit`() {
        assertThat(twoClubs.hasSameSuit(twoDiamonds), equalTo(false))
    }

    @Test
    fun `Cards with same rank should have same rank`() {
        assertThat(twoClubs.hasSameRank(twoDiamonds), equalTo(true))
    }

    @Test
    fun `Cards with different rank should have different rank`() {
        assertThat(twoClubs.hasSameRank(threeClubs), equalTo(false))
    }
}
