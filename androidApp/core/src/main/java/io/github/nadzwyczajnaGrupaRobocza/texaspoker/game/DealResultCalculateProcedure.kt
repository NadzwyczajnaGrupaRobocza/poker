package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class DealResultCalculateProcedure(val players: List<DealPlayer>) {
    fun dealResult(finalDealResult: FinalDealResult): DealResult {
        val pot = players.map { it.chipsBet.amount }.sum()
        return DealResult(players.map {
            PlayerResult(
                it.uuid,
                getChipsChange(it, finalDealResult.winner, pot)
            )
        })
    }

    private fun getChipsChange(player: DealPlayer, winner: PlayerId, pot: Int) = ChipsChange(
        when (player.uuid) {
            winner -> pot - player.chipsBet.amount
            else -> -player.chipsBet.amount
        }
    )

}
