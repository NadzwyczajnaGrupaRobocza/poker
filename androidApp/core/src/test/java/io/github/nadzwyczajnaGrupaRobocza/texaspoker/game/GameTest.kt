package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Assert
import org.junit.Test

class GameTest {

    @Test
    fun `Game should not be constructed with no players`() {
        assertThat({ Game(emptyList()) }, throws(equalTo(NotEnoughPlayers(0))))
    }

    @Test
    fun `Game should not be constructed with one player`() {
        val player = Player()
        assertThat({ Game(listOf(player)) }, throws(equalTo(NotEnoughPlayers(1))))
    }

}
