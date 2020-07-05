package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.lessThan
import org.junit.Test

class HandTypeTest {
    @Test
    fun `Hand types should be in order`() {
        assertThat(HandType.Highcard, lessThan(HandType.Pair))
        assertThat(HandType.Pair, lessThan(HandType.TwoPairs))
        assertThat(HandType.TwoPairs, lessThan(HandType.Three))
        assertThat(HandType.Three, lessThan(HandType.Straight))
        assertThat(HandType.Straight, lessThan(HandType.Flush))
        assertThat(HandType.Flush, lessThan(HandType.Full))
        assertThat(HandType.Full, lessThan(HandType.Four))
        assertThat(HandType.Four, lessThan(HandType.StraightFlush))
        assertThat(HandType.StraightFlush, lessThan(HandType.RoyalFlush))
    }

}
