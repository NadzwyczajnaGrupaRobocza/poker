package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import org.junit.Test

class DealResultCalculateProcedureTest : DealTestData() {
    @Test
    fun `should accept deal`() {
        val procedure = DealResultCalculateProcedure(dealWithCards())
    }
}

