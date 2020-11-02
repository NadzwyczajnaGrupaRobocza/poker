package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Hand

class DealResultCalculateProcedure(val players: List<DealPlayer>) {
    fun dealResult(finalDealResult: FinalDealResult): DealResult {
        return DealResult(finalDealResult.players.map { PlayerResult(it.key, it.value) })
    }

    fun dealResult(cardDistribution: CardsDistribution): DealResult {
        val maxBet = players.map { it.chipsBet.amount }.maxOrNull()
        val maxBetters = players.filter { it.chipsBet.amount == maxBet }.map { it.uuid }
        val maxBettersWithCards = cardDistribution.playersCards.filter { it.id in maxBetters }.map {
            PlayerWithHand(
                id = it.id, hand =
                Hand(
                    river = cardDistribution.riverCommunityCards,
                    pocketCards = it.cards,
                )
            )
        }.sortedByDescending { it.hand }
        val bestPlayers = getBestPlayers(maxBettersWithCards).map { it.id }
        val pot = players.sumBy { it.chipsBet.amount }
        return DealResult(players.map {
            PlayerResult(
                it.uuid,
                ChipsChange(
                    (if (it.uuid  in bestPlayers) pot / bestPlayers.size else 0) - it.chipsBet.amount
                )
            )
        })
    }

    private fun getBestPlayers(
        maxBettersWithCards: List<PlayerWithHand>,
        bestPlayers: List<PlayerWithHand> = emptyList()
    ): List<PlayerWithHand> = when {
        maxBettersWithCards.isEmpty() -> bestPlayers
        bestPlayers.isEmpty() || maxBettersWithCards.first().hand == bestPlayers.first().hand -> getBestPlayers(
            maxBettersWithCards.drop(1),
            bestPlayers + maxBettersWithCards.first()
        )
        else -> bestPlayers
    }

    private class PlayerWithHand(val id: PlayerId, val hand: Hand)

}
