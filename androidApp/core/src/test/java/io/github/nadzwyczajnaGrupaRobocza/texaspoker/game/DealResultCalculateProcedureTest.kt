package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class DealResultCalculateProcedureTest : FivePlayersDealTestData() {
    private val lostChips1Amount = -20
    private val lostChips1 = ChipsChange(lostChips1Amount)
    private val lostChips2Amount = -80
    private val lostChips2 = ChipsChange(lostChips2Amount)
    private val wonChipsAmount = 120
    private val wonChips1 = ChipsChange(wonChipsAmount)
    private val noChipsChange = ChipsChange(0)
    private val initialChips = 100

    @Test
    fun `When one player left after round should return this player as winner`() {
        val dealPlayers = listOf(
            DealPlayer(player1Id, initialChips, lostChips1Amount),
            DealPlayer(player2Id, initialChips, wonChipsAmount),
            DealPlayer(player3Id, initialChips, lostChips2Amount),
            DealPlayer(player4Id, initialChips, lostChips1Amount),
            DealPlayer(player5Id, initialChips),
        )

        assertThat(DealResultCalculateProcedure(dealPlayers), equalTo(DealResult(listOf(
            PlayerResult(player1Id, lostChips1),
            PlayerResult(player2Id, wonChips1),
            PlayerResult(player3Id, lostChips2),
            PlayerResult(player4Id, lostChips1),
            PlayerResult(player5Id, noChipsChange),
        ))))
    }
}

