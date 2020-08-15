package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Test

class DeckTest {
    @Test
    fun `Deck should have 52 cards`() {
        assertThat(Deck().deck.size, equalTo(52))
    }

    @Test
    fun `Shuffled Deck should have 52 cards`() {
        assertThat(Deck().shuffledDeck().size, equalTo(52))
    }

    @Test
    fun `Two shuffled decks should not be equal - Warning, this may fail with probability 1 to 52!`() {
        val deck = Deck()
        assertThat(deck.shuffledDeck(), not(equalTo(deck.shuffledDeck())))
    }
}