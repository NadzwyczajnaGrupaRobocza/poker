package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test

open class DealTestData {
    val player1 = DealPlayer("1", 1000)
    val player2 = DealPlayer("2", 5000)
    val smallBlind = 10
    val bigBlind = 25
    val blindDiff = bigBlind - smallBlind
    val blinds = Blinds(smallBlind, bigBlind)
}

class DealTest : DealTestData() {
    private val bigBlindPlayer = player2
    private val smallBlindPlayer = player1
    private val player3 = DealPlayer("3", 500)
    private val player4 = DealPlayer("4", 100)
    private val player5 = DealPlayer("5", 1000)
    private val deal = Deal(listOf(player1, player2, player3, player4, player5), blinds)
    private val noChange = 0

    @Test
    fun `First better should be player 3`() {
        assertThat(deal.nextBetter(), equalTo(player3.uuid))
    }

    @Test
    fun `Blinds should be bet after Deal started`() {
        assertThat(player1.chips.amount, equalTo(990))
        assertThat(player2.chips.amount, equalTo(4975))
    }

    @Test
    fun `After Deal start pot should have big and small blinds`() {
        assertThat(deal.pot, equalTo(smallBlind + bigBlind))
    }

    @Test
    fun `When all players call and check should move to next round`() {
        deal.move(DealMove.call(ChipsChange(bigBlind)))
        deal.move(DealMove.call(ChipsChange(bigBlind)))
        deal.move(DealMove.call(ChipsChange(bigBlind)))
        deal.move(DealMove.call(ChipsChange(blindDiff)))

        assertThat(
            deal.move(DealMove.check()),
            equalTo(DealMoveResult(nextRound = NextRoundResult(nextBetter = player1.uuid)))
        )
    }

    @Test
    fun `When some player raise and then all players call and check should move to next round and start with player after big dealer`() {
        deal.move(DealMove.call(ChipsChange(bigBlind)))
        deal.move(DealMove.call(ChipsChange(bigBlind)))

        val raised = 2 * bigBlind
        deal.move(DealMove.raise(ChipsChange(raised))) //player5

        deal.move(DealMove.call(ChipsChange(raised - smallBlind)))
        deal.move(DealMove.call(ChipsChange(raised - bigBlind)))
        deal.move(DealMove.call(ChipsChange(raised - bigBlind)))

        assertThat(
            deal.move(DealMove.call(ChipsChange(raised - bigBlind))),
            equalTo(DealMoveResult(nextRound = NextRoundResult(nextBetter = player1.uuid)))
        )
    }

    @Test
    fun `When all players folds big blind player should win`() {
        deal.move(DealMove.fold())
        deal.move(DealMove.fold())
        deal.move(DealMove.fold())

        assertThat(
            deal.move(DealMove.fold()), equalTo(
                DealMoveResult(
                    final = FinalDealResult(
                        winner = bigBlindPlayer.uuid, players = mapOf(
                            Pair(player1.uuid, ChipsChange(-smallBlind)),
                            Pair(player2.uuid, ChipsChange(smallBlind)),
                            Pair(player3.uuid, ChipsChange(noChange)),
                            Pair(player4.uuid, ChipsChange(noChange)),
                            Pair(player5.uuid, ChipsChange(noChange)),
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `When call with less then already max bet should throw`() {
        assertThat({ deal.move(DealMove.call(ChipsChange(blindDiff))) }, throws<InvalidMove>())
    }

    @Test
    fun `When small blind player folds big blind player should start next round`() {
        deal.move(DealMove.call(ChipsChange(bigBlind)))
        deal.move(DealMove.call(ChipsChange(bigBlind)))
        deal.move(DealMove.call(ChipsChange(bigBlind)))
        deal.move(DealMove.fold())

        assertThat(
            deal.move(DealMove.check()), equalTo(
                DealMoveResult(nextRound = NextRoundResult(bigBlindPlayer.uuid))
            )
        )
    }
}

class TwoPlayerDealTest : DealTestData() {
    private val deal = Deal(listOf(player1, player2), blinds)

    @Test
    fun `First better should be player 2`() {
        assertThat(deal.nextBetter(), equalTo(player2.uuid))
    }

    @Test
    fun `Second better should be player 1`() {
        deal.move(DealMove.call(ChipsChange(blindDiff)))

        assertThat(deal.nextBetter(), equalTo(player1.uuid))
    }

    @Test
    fun `When small blind player call result should be next better`() {
        assertThat(
            deal.move(DealMove.call(ChipsChange(blindDiff))),
            equalTo(DealMoveResult(intermediate = IntermediateDealResult(nextBetter = player1.uuid)))
        )
    }

    @Test
    fun `When small blind player call and big blind checks should move to next round`() {
        deal.move(DealMove.call(ChipsChange(blindDiff)))
        assertThat(
            deal.move(DealMove.check()), equalTo(
                DealMoveResult(nextRound = NextRoundResult(nextBetter = player2.uuid))
            )
        )
    }

    @Test
    fun `When small blind player fold big bilind player should win`() {
        assertThat(
            deal.move(DealMove.fold()), equalTo(
                DealMoveResult(
                    final = FinalDealResult(
                        winner = player1.uuid, players = mapOf(
                            Pair(player1.uuid, ChipsChange(smallBlind)),
                            Pair(player2.uuid, ChipsChange(-smallBlind))
                        )
                    )
                )
            )
        )
    }
}
