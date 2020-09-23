package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Before
import org.junit.Test

open class GameTestData {
    private val player1Id = PlayerId("1")
    private val player2Id = PlayerId("2")
    private val player3Id = PlayerId("3")
    private val player4Id = PlayerId("4")
    val player1 = Player(player1Id)
    val player2 = Player(player2Id)
    val player3 = Player(player3Id)
    val player4 = Player(player4Id)
    val startingChips = 1000
    val blinds = Blinds(10)
    val gameConfiguration = GameConfiguration(startingChips, blinds)
    val fourPlayersGame = Game(listOf(player1, player2, player3, player4), gameConfiguration)
    val twoPlayersGame = Game(listOf(player1, player2), gameConfiguration)

    val winningDealResult =
        DealResult(
            listOf(
                PlayerResult(
                    player1.uuid, ChipsChange(3000),
                ),
                PlayerResult(
                    player2.uuid, ChipsChange(-1000),
                ),
                PlayerResult(
                    player3.uuid, ChipsChange(-1000),
                ),
                PlayerResult(
                    player4.uuid, ChipsChange(-1000),
                ),
            )
        )

    val nonWiningDealResult =
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

    fun getDeal(game : Game) :Deal{
        val (deal, _) = game.deal()
        return deal
    }
}

class GameTest : GameTestData() {
    private val player5Id = PlayerId("5")
    private val player6Id = PlayerId("6")
    private val player7Id = PlayerId("7")
    private val player8Id = PlayerId("8")
    private val player9Id = PlayerId("9")
    private val player5 = Player(player5Id)
    private val player6 = Player(player6Id)
    private val player7 = Player(player7Id)
    private val player8 = Player(player8Id)
    private val player9 = Player(player9Id)

    @Test
    fun `Game should not be constructed with no players`() {
        assertThat(
            { Game(emptyList(), gameConfiguration) },
            throws(equalTo(InvalidPlayersNumber(0)))
        )
    }

    @Test
    fun `Game should not be constructed with one player`() {
        assertThat(
            { Game(listOf(player1), gameConfiguration) },
            throws(equalTo(InvalidPlayersNumber(1)))
        )
    }

    @Test
    fun `Game should be constructed with two players`() {
        Game(listOf(player1, player2), gameConfiguration)
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
                ), gameConfiguration
            )
        }, throws(equalTo(InvalidPlayersNumber(9))))
    }

    @Test
    fun `Game should be constructed with eight players`() {
        Game(
            listOf(player1, player2, player3, player4, player5, player6, player7, player8),
            gameConfiguration
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
        val deal = getDeal(fourPlayersGame)
        assertThat(
            deal.players().toSet().map { it.uuid },
            equalTo(fourPlayersGame.activePlayers.map { it.uuid })
        )
    }

    @Test
    fun `When applying non winning DealResult should return null`() {
        assertThat(fourPlayersGame.acceptDealResult(nonWiningDealResult), equalTo(null))
    }

    @Test
    fun `When applying DealResult with player lost his chips should return three active players`() {
        fourPlayersGame.acceptDealResult(nonWiningDealResult)
        val deal = getDeal(fourPlayersGame)

        assertThat(
            deal.players().map { it.uuid }.toSet(),
            equalTo(setOf(player1.uuid, player3.uuid, player4.uuid))
        )
    }

    @Test
    fun `In first deal last player should be dealer`() {
        val deal = getDeal(fourPlayersGame)
        assertThat(deal.dealer, equalTo(player4.uuid))
    }

    @Test
    fun `In first deal first player should be on small blind`() {
        val deal = getDeal(fourPlayersGame)
        assertThat(deal.smallBlind, equalTo(player1.uuid))
    }

    @Test
    fun `In first deal last player should be on big blind`() {
        val deal = getDeal(fourPlayersGame)
        assertThat(deal.bigBlind, equalTo(player2.uuid))
    }

    @Test
    fun `After winning deal result should return winner`() {
        assertThat(fourPlayersGame.acceptDealResult(winningDealResult), equalTo(player1.uuid))
    }
}

class GameWithWinner : GameTestData() {
    @Before
    fun makeWinner() {
        fourPlayersGame.acceptDealResult(winningDealResult)
    }

    @Test
    fun `Should not deal when one player left`() {
        assertThat({ getDeal(fourPlayersGame) }, throws(equalTo(InvalidPlayersNumber(1))))
    }
}

class GameAfterFirstDealTest : GameTestData() {
    @Before
    fun runFirstDeal() {
        fourPlayersGame.deal()
    }

    @Test
    fun `In second deal first player should be dealer`() {
        val deal = getDeal(fourPlayersGame)
        assertThat(deal.dealer, equalTo(player1.uuid))
    }

    @Test
    fun `In second deal second player should be on small blind`() {
        val deal = getDeal(fourPlayersGame)
        assertThat(deal.smallBlind, equalTo(player2.uuid))
    }

    @Test
    fun `In second deal third player should be on big blind`() {
        val deal = getDeal(fourPlayersGame)
        assertThat(deal.bigBlind, equalTo(player3.uuid))
    }
}

class TwoPlayerGame : GameTestData() {
    @Test
    fun `First player should be dealer`() {
        val deal = getDeal(twoPlayersGame)
        assertThat(deal.dealer, equalTo(player2.uuid))
    }

    @Test
    fun `First player should be on small blind`() {
        val deal = getDeal(twoPlayersGame)
        assertThat(deal.smallBlind, equalTo(player2.uuid))
    }

    @Test
    fun `Second player should be on big blind`() {
        val deal = getDeal(twoPlayersGame)
        assertThat(deal.bigBlind, equalTo(player1.uuid))
    }
}

class TwoPlayerGameSecondDeal : GameTestData() {
    @Before
    fun firstDeal() {
        twoPlayersGame.deal()
    }

    @Test
    fun `Second player should be dealer`() {
        val deal = getDeal(twoPlayersGame)
        assertThat(deal.dealer, equalTo(player1.uuid))
    }

    @Test
    fun `Second player should be on small blind`() {
        val deal = getDeal(twoPlayersGame)
        assertThat(deal.smallBlind, equalTo(player1.uuid))
    }

    @Test
    fun `Third player should be on big blind`() {
        val deal = getDeal(twoPlayersGame)
        assertThat(deal.bigBlind, equalTo(player2.uuid))
    }
}

class TwoPlayerGameThirdDeal : GameTestData() {
    @Before
    fun twoDeals() {
        twoPlayersGame.deal()
        twoPlayersGame.deal()
    }

    @Test
    fun `First player should be dealer`() {
        val deal = getDeal(twoPlayersGame)
        assertThat(deal.dealer, equalTo(player2.uuid))
    }

    @Test
    fun `First player should be on small blind`() {
        val deal = getDeal(twoPlayersGame)
        assertThat(deal.smallBlind, equalTo(player2.uuid))
    }

    @Test
    fun `Second player should be on big blind`() {
        val deal = getDeal(twoPlayersGame)
        assertThat(deal.bigBlind, equalTo(player1.uuid))
    }
}

