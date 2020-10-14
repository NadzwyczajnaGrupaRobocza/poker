package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Hand

class DealResultCalculateProcedure(val players: List<DealPlayer>) {
    fun dealResult(finalDealResult: FinalDealResult): DealResult {
        return DealResult(finalDealResult.players.map { PlayerResult(it.key, it.value) })
    }

    fun dealResult(dealResult: NextRoundResult, cardDistribution: CardsDistribution): DealResult {
        val maxBet = players.map { it.chipsBet.amount }.maxOrNull()
        val maxBetters = players.filter { it.chipsBet.amount == maxBet }.map { it.uuid }
        val maxBettersCards = cardDistribution.playersCards.filter { it.id in maxBetters }
        val bestCard = maxBettersCards.maxByOrNull {
            Hand(
                river = cardDistribution.riverCommunityCards,
                pocketCards = it.cards
            )
        }?.id
        val pot = players.sumBy { it.chipsBet.amount }
        return DealResult(players.map {
            PlayerResult(
                it.uuid,
                ChipsChange(
                    (if (it.uuid == bestCard) pot else 0) - it.chipsBet.amount
                )
            )
        })
    }

}
