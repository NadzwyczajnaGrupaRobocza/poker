package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Deal(gamePlayers: List<DealPlayer>, blinds: Blinds) {
    private val deal = createDeal(gamePlayers)
    private val internalPlayers = gamePlayers.toInternal()
    val players: List<DealPlayer>
        get() = internalPlayers.toDealPlayers()

    init {
        assert(gamePlayers.size > 1)
    }

    private fun createDeal(players: List<DealPlayer>) = when (players.size) {
        2 -> TwoPlayerDeal(players)
        else -> ManyPlayersDeal(players)
    }

    private val betterPosition: Int
        get() = bettingRound % internalPlayers.size
    private var bettingRound = deal.initialBettingRound
    private var internalPot = Chips(0)
    private var lastRaiser = deal.playerAfterBigBlind

    val nextBetter: String?
        get() = internalPlayers[betterPosition].dealPlayer.uuid
    val dealer: String
        get() = deal.dealer
    val smallBlind: String
        get() = deal.smallBlind
    val bigBlind: String
        get() = deal.bigBlind
    val pot: Int
        get() = internalPot.amount

    init {
        internalPlayers.find { it.dealPlayer.uuid == smallBlind }
            ?.let { getChipsFromPlayer(it, blinds.small) }
        internalPlayers.find { it.dealPlayer.uuid == bigBlind }
            ?.let { getChipsFromPlayer(it, blinds.big) }
    }

    fun move(move: DealMove): DealMoveResult {
        if (move.folded) {
            internalPlayers[betterPosition].folded = true
        }
        //getChipsFromPlayer(internalPlayers[betterPosition], move.chipsChange.change)
        bettingRound = bettingRound.inc()
        val activePlayers = internalPlayers.filter { !it.folded }
        return when {
            activePlayers.size == 1 -> getResultWithWinner(activePlayers)
            nextBetter == internalPlayers[lastRaiser].dealPlayer.uuid -> moveToNextRound()
            else -> DealMoveResult(intermediate = IntermediateDealResult(nextBetter!!))
        }
    }

    private fun getResultWithWinner(activePlayers: List<InternalPlayer>): DealMoveResult {
        val winner = activePlayers.first()
        return DealMoveResult(
            final = FinalDealResult(
                winner = winner.dealPlayer.uuid,
                players =
                (listOf(
                    Pair(
                        winner.dealPlayer.uuid,
                        ChipsChange(internalPot.amount - winner.chipsBet.amount)
                    )
                ) + internalPlayers.filter { it.dealPlayer.uuid != winner.dealPlayer.uuid }
                    .map {
                        Pair(
                            it.dealPlayer.uuid,
                            ChipsChange(-it.chipsBet.amount)
                        )
                    }).toMap()
            )
        )
    }

    private fun moveToNextRound(): DealMoveResult {
        //lastRaiser = deal.playerAfterBigBlind
        //bettingRound = deal.playerAfterBigBlind
        return DealMoveResult(nextRound = nextBetter?.let { NextRoundResult(nextBetter = it) })
    }

    private fun getChipsFromPlayer(player: InternalPlayer, amount: Int) {
        player.dealPlayer.chips.change(ChipsChange(-amount))
        player.chipsBet.change(ChipsChange(amount))
        internalPot.change(ChipsChange(amount))
    }

    class InternalPlayer(val dealPlayer: DealPlayer) {
        val chipsBet = Chips(0)
        var folded = false
    }
}

private fun List<DealPlayer>.toInternal() = map { Deal.InternalPlayer(it) }
private fun List<Deal.InternalPlayer>.toDealPlayers() = map { it.dealPlayer }

private abstract class DealImpl(val players: List<DealPlayer>) {
    abstract val initialBettingRound: Int
    val dealer = players.last().uuid
    abstract val smallBlind: String
    abstract val bigBlind: String
    abstract val playerAfterBigBlind: Int
}

private class TwoPlayerDeal(players: List<DealPlayer>) : DealImpl(players) {
    override val initialBettingRound = 1
    override val smallBlind = dealer
    override val bigBlind = players.first().uuid
    override val playerAfterBigBlind = 1
}

private class ManyPlayersDeal(players: List<DealPlayer>) : DealImpl(players) {
    override val initialBettingRound = 2
    override val smallBlind = players.first().uuid
    override val bigBlind = players[1].uuid
    override val playerAfterBigBlind = 2
}
