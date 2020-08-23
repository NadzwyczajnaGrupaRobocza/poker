package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

open class DealTestData {
    val player1 = DealPlayer("1", 1000)
    val player2 = DealPlayer("2", 5000)
    val smallBlind = 10
    val bigBlind = 25
    val blinds = Blinds(smallBlind, bigBlind)
}

class DealTest : DealTestData() {
    private val player3 = DealPlayer("3", 500)
    private val player4 = DealPlayer("4", 100)
    private val player5 = DealPlayer("5", 1000)
    private val deal = Deal(listOf(player1, player2, player3, player4, player5), blinds)

    @Test
    fun `First better should be player 3`() {
        assertThat(deal.nextBetter, equalTo(player3.uuid))
    }

    @Test
    fun `Blinds should be bet after Deal started`() {
        assertThat(player1.chips.amount, equalTo(990))
        assertThat(player2.chips.amount, equalTo(4975))
    }

    @Test
    fun `After Deal start pot should have big and small blinds`() {
        assertThat(deal.pot, equalTo(smallBlind+bigBlind))
    }
}

class TwoPlayerDealTest : DealTestData() {
    private val deal = Deal(listOf(player1, player2), blinds)

    @Test
    fun `First better should be player 2`() {
        assertThat(deal.nextBetter, equalTo(player2.uuid))
    }

    @Test
    fun `Second better should be player 1`() {
        deal.move(DealMove(ChipsChange(0)))

        assertThat(deal.nextBetter, equalTo(player1.uuid))
    }

}