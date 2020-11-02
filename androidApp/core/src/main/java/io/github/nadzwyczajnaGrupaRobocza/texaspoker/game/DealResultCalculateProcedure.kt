package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Hand

class DealResultCalculateProcedure(val players: List<DealPlayer>) {
    fun dealResult(finalDealResult: FinalDealResult): DealResult {
        return DealResult(finalDealResult.players.map { PlayerResult(it.key, it.value) })
    }

    fun dealResult(
        nextRoundDealResult: NextRoundResult,
        cardDistribution: CardsDistribution
    ): DealResult {
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
        val pot = players.sumBy { it.chipsBet.amount }
        val bestPlayers = getBestPlayers(maxBettersWithCards).map { it.id }
        val splitPot = pot / bestPlayers.size
        val potDifferences = pot - splitPot * bestPlayers.size
        val bestPlayersWithLeftoverChips =
            getPlayersWithLeftoverChips(potDifferences, nextRoundDealResult, bestPlayers)

        return DealResult(players.map {
            PlayerResult(
                it.uuid,
                ChipsChange(
                    when (it.uuid) {
                        in bestPlayersWithLeftoverChips -> splitPot + 1
                        in bestPlayers -> splitPot
                        else -> 0
                    } - it.chipsBet.amount
                )
            )
        })
    }

    private fun getPlayersWithLeftoverChips(
        leftoverChips: Int,
        nextRoundDealResult: NextRoundResult,
        bestPlayers: List<PlayerId>
    ) = if (leftoverChips > 0) {
        queuePlayersFrom(
            players.map { it.uuid },
            nextRoundDealResult.nextBetter
        ).filter { it in bestPlayers }
            .take(leftoverChips)
    } else {
        emptyList()
    }

    private fun queuePlayersFrom(
        originalList: List<PlayerId>,
        nextBetter: PlayerId,
        listEnd: List<PlayerId> = emptyList()
    ): List<PlayerId> = when {
        nextBetter == originalList.first() -> originalList + listEnd
        else -> queuePlayersFrom(originalList.drop(1), nextBetter, listEnd + originalList.first())
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

