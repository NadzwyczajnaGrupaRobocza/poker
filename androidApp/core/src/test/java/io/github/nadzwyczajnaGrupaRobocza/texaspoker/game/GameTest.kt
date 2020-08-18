package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test

class GameTest {
    private val player1 = Player("1")
    private val player2 = Player("2")
    private val player3 = Player("3")
    private val player4 = Player("4")
    private val player5 = Player("5")
    private val player6 = Player("6")
    private val player7 = Player("7")
    private val player8 = Player("8")
    private val player9 = Player("9")
    private val startingChips = 1000
    private val fourPlayersGame = Game(listOf(player1, player2, player3, player4), startingChips)

    @Test
    fun `Game should not be constructed with no players`() {
        assertThat({ Game(emptyList(), startingChips) }, throws(equalTo(InvalidPlayersNumber(0))))
    }

    @Test
    fun `Game should not be constructed with one player`() {
        assertThat(
            { Game(listOf(player1), startingChips) },
            throws(equalTo(InvalidPlayersNumber(1)))
        )
    }

    @Test
    fun `Game should be constructed with two players`() {
        Game(listOf(player1, player2), startingChips)
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
                ), startingChips
            )
        }, throws(equalTo(InvalidPlayersNumber(9))))
    }

    @Test
    fun `Game should be constructed with eight players`() {
        Game(
            listOf(player1, player2, player3, player4, player5, player6, player7, player8),
            startingChips
        )
    }

    @Test
    fun `Game should return all players as active after initialization`() {
        assertThat(
            fourPlayersGame.activePlayers,
            equalTo(setOf(player1, player2, player3, player4))
        )
    }

    @Test
    fun `When all players active Game should start Deal with all players`() {
        assertThat(fourPlayersGame.deal().players.toSet(), equalTo(fourPlayersGame.activePlayers))
    }

    @Test
    fun `When applying DealResult with player lost his chips should return three active players`() {
        fourPlayersGame.acceptDealResult(
            DealResult(
                listOf(
                    PlayerResult(
                        player1.uuid, ChipsChange(1000),
                    ),
                    PlayerResult(
                        player2.uuid, ChipsChange(-1000),
                    ),
                    PlayerResult(
                        player3.uuid, ChipsChange(0),
                    ),
                    PlayerResult(
                        player4.uuid, ChipsChange(0),
                    ),
                )
            )
        )

        assertThat(
            fourPlayersGame.deal().players.toSet(),
            equalTo(setOf(player1, player3, player4))
        )
    }
}
