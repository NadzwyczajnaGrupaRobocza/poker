package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Hand
import java.lang.Integer.max

class DealResultCalculateProcedure(val players: List<DealPlayer>) {
    fun dealResult(finalDealResult: FinalDealResult): DealResult {
        return DealResult(finalDealResult.players.map { PlayerResult(it.key, it.value) })
    }

    fun dealResult(
        nextRoundDealResult: NextRoundResult,
        cardDistribution: CardsDistribution
    ): DealResult = calculateDealResults(nextRoundDealResult, cardDistribution, players)

}

private fun calculateDealResults(
    nextRoundDealResult: NextRoundResult,
    cardDistribution: CardsDistribution,
    players: List<DealPlayer>,
): DealResult {
    val maxBettersWithCards = getMaxBettersWithCards(cardDistribution, players)
    val bestPlayers = getBestPlayers(maxBettersWithCards).map { it.id }
    return when (maxBettersWithCards.any { it.id.allIn() }) {
        true -> splitPotWithAllInWInner(
            bestPlayers, nextRoundDealResult,
            cardDistribution,
            players
        )
        false -> splitPotWithoutAllInWinner(
            bestPlayers,
            nextRoundDealResult,
            players
        )
    }
}

private fun splitPotWithAllInWInner(
    bestPlayers: List<DealPlayer>,
    nextRoundDealResult: NextRoundResult,
    cardDistribution: CardsDistribution,
    players: List<DealPlayer>,
): DealResult {
    val allInWinners = bestPlayers.filter { it.allIn() }.sortedBy { it.chipsBet.amount }
    val allInChips = allInWinners.first().chipsBet.amount
    val playersWithLimitedBets = players.map {
        DealPlayer(
            uuid = it.uuid,
            chipsBet = if (it.chipsBet.amount > allInChips) allInChips else it.chipsBet.amount,
            initialChips = it.chips.amount
        )
    }
    val playersBetMinusAllIn = players.map {
        DealPlayer(
            uuid = it.uuid,
            chipsBet = max(0, it.chipsBet.amount - allInChips),
            initialChips = max(0, it.chips.amount - allInChips),
        )
    }
    return splitPotWithoutAllInWinner(bestPlayers, nextRoundDealResult, playersWithLimitedBets) +
            calculateDealResults(nextRoundDealResult, cardDistribution, playersBetMinusAllIn)
}

private operator fun DealResult.plus(other: DealResult) =
    DealResult(playersResults.mapIndexed { index, playerResult ->
        PlayerResult(
            uuid = playerResult.uuid,
            chips = ChipsChange(playerResult.chips.change + other.playersResults[index].chips.change)
        )
    })

private fun splitPotWithoutAllInWinner(
    bestPlayers: List<DealPlayer>,
    nextRoundDealResult: NextRoundResult,
    players: List<DealPlayer>
): DealResult {
    val (splitPot, potDifferences) = getPot(bestPlayers, players)
    val bestPlayersWithLeftoverChips =
        getPlayersWithLeftoverChips(potDifferences, nextRoundDealResult, bestPlayers, players)

    return DealResult(players.map {
        PlayerResult(
            it.uuid,
            getChipsChange(it, bestPlayersWithLeftoverChips, splitPot, bestPlayers)
        )
    })
}

private fun getChipsChange(
    dealPlayer: DealPlayer,
    bestPlayersWithLeftoverChips: List<PlayerId>,
    splitPot: Int,
    bestPlayers: List<DealPlayer>
) =
    ChipsChange(
        getWonChips(
            dealPlayer,
            bestPlayersWithLeftoverChips,
            splitPot,
            bestPlayers
        ) - dealPlayer.chipsBet.amount
    )

private fun getWonChips(
    it: DealPlayer,
    bestPlayersWithLeftoverChips: List<PlayerId>,
    splitPot: Int,
    bestPlayers: List<DealPlayer>
) =
    when (it.uuid) {
        in bestPlayersWithLeftoverChips -> splitPot + 1
        in bestPlayers.map { it.uuid } -> splitPot
        else -> 0
    }

private fun getPot(
    bestPlayers: List<DealPlayer>,
    players: List<DealPlayer>
): Pair<Int, Int> {
    val pot = players.sumBy { it.chipsBet.amount }
    val splitPot = pot / bestPlayers.size
    val potDifferences = pot - splitPot * bestPlayers.size
    return Pair(splitPot, potDifferences)
}

private fun getMaxBettersWithCards(
    cardDistribution: CardsDistribution,
    players: List<DealPlayer>
): List<PlayerWithHand> {
    val maxBet = players.map { it.chipsBet.amount }.maxOrNull()
    val maxBetters =
        players.filter { it.maxBetter(maxBet) || it.allIn() }.map { it.uuid to it }.toMap()
    return cardDistribution.playersCards.filter { it.id in maxBetters.keys }.map {
        PlayerWithHand(
            id = maxBetters.getValue(it.id), hand =
            Hand(
                river = cardDistribution.riverCommunityCards,
                pocketCards = it.cards,
            )
        )
    }.sortedByDescending { it.hand }
}

private fun getPlayersWithLeftoverChips(
    leftoverChips: Int,
    nextRoundDealResult: NextRoundResult,
    bestPlayers: List<DealPlayer>,
    players: List<DealPlayer>
) = if (leftoverChips > 0) {
    queuePlayersFrom(
        players.map { it.uuid },
        nextRoundDealResult.nextBetter
    ).filter { it in bestPlayers.map { it.uuid } }
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

private class PlayerWithHand(val id: DealPlayer, val hand: Hand)


fun DealPlayer.allIn() = chips.amount == chipsBet.amount && chipsBet.amount != 0

fun DealPlayer.maxBetter(maxBet: Int?) = chipsBet.amount == maxBet

