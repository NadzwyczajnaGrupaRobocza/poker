package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test


class DealTest : FivePlayersDealTestData() {
    private val bigBlindPlayer = player2
    private val smallBlindPlayer = player1
    private val noChange = 0

    @Test
    fun `First better should be player 3`() {
        assertThat(fivePlayerDeal.nextBetter(), equalTo(player3.uuid))
    }

    @Test
    fun `Blinds should be bet after Deal started`() {
        assertThat(player1.chips.amount, equalTo(990))
        assertThat(player2.chips.amount, equalTo(4975))
    }

    @Test
    fun `After Deal start pot should have big and small blinds`() {
        assertThat(fivePlayerDeal.pot, equalTo(smallBlind + bigBlind))
    }

    @Test
    fun `When all players call and check should move to next round`() {
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(blindDiff)))

        assertThat(
            fivePlayerDeal.move(DealMove.check()),
            equalTo(DealMoveResult(nextRound = NextRoundResult(nextBetter = player1.uuid)))
        )
    }

    @Test
    fun `When some player raise and then all players call and check should move to next round and start with player after big dealer`() {
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))

        val raised = 2 * bigBlind
        fivePlayerDeal.move(DealMove.raise(ChipsChange(raised))) //player5

        fivePlayerDeal.move(DealMove.call(ChipsChange(raised - smallBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(raised - bigBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(raised - bigBlind)))

        assertThat(
            fivePlayerDeal.move(DealMove.call(ChipsChange(raised - bigBlind))),
            equalTo(DealMoveResult(nextRound = NextRoundResult(nextBetter = player1.uuid)))
        )
    }

    @Test
    fun `When all players folds big blind player should win`() {
        fivePlayerDeal.move(DealMove.fold())
        fivePlayerDeal.move(DealMove.fold())
        fivePlayerDeal.move(DealMove.fold())

        assertThat(
            fivePlayerDeal.move(DealMove.fold()), equalTo(
                DealMoveResult(
                    final = FinalDealResult(
                        winner = bigBlindPlayer.uuid, players = mapOf(
                            player1.uuid to ChipsChange(-smallBlind),
                            player2.uuid to ChipsChange(smallBlind),
                            player3.uuid to ChipsChange(noChange),
                            player4.uuid to ChipsChange(noChange),
                            player5.uuid to ChipsChange(noChange),
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `When call with less then already max bet should throw`() {
        assertThat(
            { fivePlayerDeal.move(DealMove.call(ChipsChange(blindDiff))) },
            throws<InvalidMove>()
        )
    }

    @Test
    fun `When small blind player folds big blind player should start next round`() {
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))
        fivePlayerDeal.move(DealMove.fold())

        assertThat(
            fivePlayerDeal.move(DealMove.check()), equalTo(
                DealMoveResult(nextRound = NextRoundResult(bigBlindPlayer.uuid))
            )
        )
    }

    @Test
    fun `When two players fold should move to next round`() {
        fivePlayerDeal.move(DealMove.fold())
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))
        fivePlayerDeal.move(DealMove.fold())
        fivePlayerDeal.move(DealMove.call(ChipsChange(blindDiff)))

        assertThat(
            fivePlayerDeal.move(DealMove.check()),
            equalTo(DealMoveResult(nextRound = NextRoundResult(smallBlindPlayer.uuid)))
        )
    }

    @Test
    fun `When two players fold in next round should only three players to bet`() {
        fivePlayerDeal.move(DealMove.fold())
        fivePlayerDeal.move(DealMove.call(ChipsChange(bigBlind)))
        fivePlayerDeal.move(DealMove.fold())
        fivePlayerDeal.move(DealMove.call(ChipsChange(blindDiff)))
        fivePlayerDeal.move(DealMove.check())

        fivePlayerDeal.move(DealMove.check())
        fivePlayerDeal.move(DealMove.check())
        assertThat(
            fivePlayerDeal.move(DealMove.check()),
            equalTo(DealMoveResult(nextRound = NextRoundResult(smallBlindPlayer.uuid)))
        )
    }

    @Test
    fun `When multiple players raises should move to next round when all call`() {
        val raise1 = 30
        val raise2 = 50
        val raise3 = 80

        fivePlayerDeal.move(DealMove.raise(raise1))
        fivePlayerDeal.move(DealMove.raise(raise2))
        fivePlayerDeal.move(DealMove.raise(raise3))
        fivePlayerDeal.move(DealMove.call(raise3 - smallBlind))
        fivePlayerDeal.move(DealMove.call(raise3 - bigBlind))
        fivePlayerDeal.move(DealMove.Companion.call(raise3 - raise1))

        assertThat(
            fivePlayerDeal.move(DealMove.Companion.call(raise3 - raise2)),
            equalTo(DealMoveResult(nextRound = NextRoundResult(smallBlindPlayer.uuid)))
        )
    }

    @Test
    fun `All in player should not bet but remain active`() {
        val allIn = 100

        fivePlayerDeal.move(DealMove.fold())
        fivePlayerDeal.move(DealMove.raise(ChipsChange(allIn)))
        fivePlayerDeal.move(DealMove.fold())
        fivePlayerDeal.move(DealMove.call(ChipsChange(allIn - smallBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(allIn - bigBlind)))

        fivePlayerDeal.move(DealMove.check())
        assertThat(
            fivePlayerDeal.move(DealMove.check()),
            equalTo(DealMoveResult(nextRound = NextRoundResult(smallBlindPlayer.uuid)))
        )
    }

    @Test
    fun `All in player bet when max bet is bigger then it's chips`() {
        val maxBet = 200
        val allIn = 100

        fivePlayerDeal.move(DealMove.raise(chips = maxBet))
        assertThat(
            fivePlayerDeal.move(DealMove.call(chips = allIn)),
            equalTo(DealMoveResult(intermediate = IntermediateDealResult(nextBetter = player5Id)))
        )
    }

    @Test
    fun `All in player with less bet then remaining active`() {
        val allIn = 100
        val bet = 150

        fivePlayerDeal.move(DealMove.raise(bet))
        fivePlayerDeal.move(DealMove.raise(allIn))
        fivePlayerDeal.move(DealMove.fold())
        fivePlayerDeal.move(DealMove.call(ChipsChange(bet - smallBlind)))
        fivePlayerDeal.move(DealMove.call(ChipsChange(bet - bigBlind)))

        fivePlayerDeal.move(DealMove.check())
        fivePlayerDeal.move(DealMove.check())
        assertThat(
            fivePlayerDeal.move(DealMove.check()),
            equalTo(DealMoveResult(nextRound = NextRoundResult(smallBlindPlayer.uuid)))
        )
    }
}

class TwoPlayerDealTest : TwoPlayersDealTestData() {
    @Test
    fun `First better should be player 2`() {
        assertThat(twoPlayersDeal.nextBetter(), equalTo(player2.uuid))
    }

    @Test
    fun `Second better should be player 1`() {
        twoPlayersDeal.move(DealMove.call(ChipsChange(blindDiff)))

        assertThat(twoPlayersDeal.nextBetter(), equalTo(player1.uuid))
    }

    @Test
    fun `When small blind player call result should be next better`() {
        assertThat(
            twoPlayersDeal.move(DealMove.call(ChipsChange(blindDiff))),
            equalTo(DealMoveResult(intermediate = IntermediateDealResult(nextBetter = player1.uuid)))
        )
    }

    @Test
    fun `When small blind player call and big blind checks should move to next round`() {
        twoPlayersDeal.move(DealMove.call(ChipsChange(blindDiff)))
        assertThat(
            twoPlayersDeal.move(DealMove.check()), equalTo(
                DealMoveResult(nextRound = NextRoundResult(nextBetter = player2.uuid))
            )
        )
    }

    @Test
    fun `When small blind player fold big blind player should win`() {
        assertThat(
            twoPlayersDeal.move(DealMove.fold()), equalTo(
                DealMoveResult(
                    final = FinalDealResult(
                        winner = player1.uuid, players = mapOf(
                            player1.uuid to ChipsChange(smallBlind),
                            player2.uuid to ChipsChange(-smallBlind)
                        )
                    )
                )
            )
        )
    }
}
