package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test

class GameTest {
    val player1= Player("1")
    val player2= Player("2")
    val player3= Player("3")
    val player4= Player("4")
    val player5= Player("5")
    val player6= Player("6")
    val player7= Player("7")
    val player8= Player("8")
    val player9= Player("9")

    @Test
    fun `Game should not be constructed with no players`() {
        assertThat({ Game(emptyList()) }, throws(equalTo(InvalidPlayersNumber(0))))
    }

    @Test
    fun `Game should not be constructed with one player`() {
        assertThat({ Game(listOf(player1)) }, throws(equalTo(InvalidPlayersNumber(1))))
    }

    @Test
    fun `Game should be constructed with two players`() {
        Game(listOf(player1, player2))
    }

    @Test
    fun `Game should not be constructed with nine players`() {
        assertThat({
            Game(
                listOf(
                    player1,
                    player2,
                    player3,
                    player4,
                    player5,
                    player6,
                    player7,
                    player8,
                    player9
                )
            )
        }, throws(equalTo(InvalidPlayersNumber(9))))
    }

    @Test
    fun `Game should be constructed with eight players`() {
        Game(listOf(player1, player2, player3, player4, player5, player6, player7, player8))
    }

}
