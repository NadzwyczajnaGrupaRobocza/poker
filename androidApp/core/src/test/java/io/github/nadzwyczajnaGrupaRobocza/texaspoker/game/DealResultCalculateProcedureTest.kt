package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class DealResultCalculateProcedureTest : DealTestData() {
    private val lostChips1 = ChipsChange(-20)
    private val lostChips2 = ChipsChange(-80)
    private val wonChips1 = ChipsChange(120)
    private val noChipsChange = ChipsChange(0)

    @Test
    fun `When one player left after round should return this player as winner`() {
        assertThat(DealResultCalculateProcedure(dealWithCards()), equalTo(DealResult(listOf(
            PlayerResult(player1Id, lostChips1),
            PlayerResult(player2Id, wonChips1),
            PlayerResult(player3Id, lostChips2),
            PlayerResult(player4Id, lostChips1),
            PlayerResult(player5Id, noChipsChange),
        ))))
    }
}

