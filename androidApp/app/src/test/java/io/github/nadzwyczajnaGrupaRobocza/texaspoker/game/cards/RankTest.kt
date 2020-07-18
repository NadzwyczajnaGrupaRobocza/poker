package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class RankTest {
    @Test
    fun `Hand should have five cards`() {
        assertThat(diamondsKing.rank - spadesQueen.rank, equalTo(1))
    }

    @Test
    fun `toString should print`() {
        assertThat(toUnicode(diamondsKing.rank), equalTo("K"))
    }
}
