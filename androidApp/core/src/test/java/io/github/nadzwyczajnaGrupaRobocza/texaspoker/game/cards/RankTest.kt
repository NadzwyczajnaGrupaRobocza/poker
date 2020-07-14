package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.diamondsKing
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.minus
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.spadesQueen
import org.junit.Test

class RankTest {
    @Test
    fun `Hand should have five cards`() {
        assertThat(diamondsKing.rank - spadesQueen.rank, equalTo(1))
    }
}
