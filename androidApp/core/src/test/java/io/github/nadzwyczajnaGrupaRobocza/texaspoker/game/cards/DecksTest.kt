package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

class DecksTest {
    @Test
    fun `Deck should have 52 cards`() {
        assertThat(Decks.ordededDeck.size, equalTo(52))
    }

    @Test
    fun `Shuffled Deck should have 52 cards`() {
        assertThat(Decks.shuffledDeck().size, equalTo(52))
    }

    @Test
    fun `Two shuffled decks should not be equal - Warning, this may fail with probability 1 to 52!`() {
        assertThat(Decks.shuffledDeck(), not(equalTo(Decks.shuffledDeck())))
    }
}