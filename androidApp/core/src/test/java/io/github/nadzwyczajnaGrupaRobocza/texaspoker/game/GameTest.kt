package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test

class GameTest {

    @Test
    fun `Game should not be constructed with no players`() {
        assertThat({ Game(emptyList()) }, throws(equalTo(InvalidPlayersNumber(0))))
    }

    @Test
    fun `Game should not be constructed with one player`() {
        val player = Player()
        assertThat({ Game(listOf(player)) }, throws(equalTo(InvalidPlayersNumber(1))))
    }

    @Test
    fun `Game should be constructed with two players`() {
        val player = Player()
        Game(listOf(player, player))
    }

    @Test
    fun `Game should not be constructed with nine players`() {
        val player = Player()
        assertThat({
            Game(
                listOf(
                    player,
                    player,
                    player,
                    player,
                    player,
                    player,
                    player,
                    player,
                    player
                )
            )
        }, throws(equalTo(InvalidPlayersNumber(9))))
    }

    @Test
    fun `Game should be constructed with eight players`() {
        val player = Player()
        Game(listOf(player, player, player, player, player, player, player, player))
    }

}
