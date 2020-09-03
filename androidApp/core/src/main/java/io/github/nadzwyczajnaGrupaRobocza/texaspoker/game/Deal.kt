package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Deal(gamePlayers: List<DealPlayer>, private val blinds: Blinds) {
    init {
        assert(gamePlayers.size > 1)
    }

    private val dealConstants = createDeal(gamePlayers)
    private val internalPlayers = gamePlayers.toInternal()
    private val internalPot = Chips(0)
    private var bettingRound = dealConstants.initialBettingRound
    private var lastRaiser = dealConstants.playerAfterBigBlind

    private fun createDeal(players: List<DealPlayer>) = when (players.size) {
        2 -> TwoPlayerDeal(players)
        else -> ManyPlayersDeal(players)
    }

    private val betterPosition: Int
        get() = bettingRound % internalPlayers.size

    fun players() = internalPlayers.toDealPlayers()
    fun nextBetter() = internalPlayers[betterPosition].dealPlayer.uuid

    val dealer: String
        get() = dealConstants.dealer
    val smallBlind: String
        get() = dealConstants.smallBlind
    val bigBlind: String
        get() = dealConstants.bigBlind
    val pot: Int
        get() = internalPot.amount

    init {
        betBlinds()
    }

    private fun betBlinds() {
        internalPlayers.find { it.dealPlayer.uuid == smallBlind }
            ?.let { getChipsFromPlayer(it, blinds.small) }
        internalPlayers.find { it.dealPlayer.uuid == bigBlind }
            ?.let { getChipsFromPlayer(it, blinds.big) }
    }

    fun move(move: DealMove): DealMoveResult {
        checkAndMarkFolded(move)
        bet(move)

        val typeOfMove = calculateMoveType(move)
        val currentBetter = betterPosition
        bettingRound = bettingRound.inc()
        val activePlayers = internalPlayers.filter { !it.folded }

        return when {
            typeOfMove == MoveType.Raise -> moveWithRaise(currentBetter)
            activePlayers.size == 1 -> getResultWithWinner(activePlayers)
            nextBetter() == internalPlayers[lastRaiser].dealPlayer.uuid -> moveToNextRound()
            else -> DealMoveResult(intermediate = IntermediateDealResult(nextBetter()))
        }
    }

    private fun checkAndMarkFolded(move: DealMove) {
        if (move.folded) {
            internalPlayers[betterPosition].folded = true
        }
    }

    private fun moveWithRaise(currentBetter: Int): DealMoveResult {
        lastRaiser = currentBetter
        return DealMoveResult(intermediate = IntermediateDealResult(nextBetter()))
    }

    private fun calculateMoveType(move: DealMove): MoveType {
        val moveChips = move.chipsChange.change
        val currentPlayer = internalPlayers[betterPosition]
        val currentPlayerBet = currentPlayer.chipsBet.amount
        val playerLastRaised = internalPlayers[lastRaiser]
        val lastRaiserBet = playerLastRaised.chipsBet.amount


        return when {
            currentPlayerBet != lastRaiserBet && moveChips == 0 -> throw InvalidMove("Should fold/call/raise when bet less then max bet")
            currentPlayerBet == lastRaiserBet && moveChips == 0 -> MoveType.Check
            currentPlayerBet == lastRaiserBet -> MoveType.Call
            else -> MoveType.Raise
        }
    }

    private fun bet(move: DealMove) {
        val moveChips = move.chipsChange.change
        val currentPlayer = internalPlayers[betterPosition]
        getChipsFromPlayer(currentPlayer, moveChips)
    }

    enum class MoveType {
        Call,
        Raise,
        Check
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
        bettingRound = dealConstants.playerAfterBigBlind
        return DealMoveResult(nextRound = NextRoundResult(nextBetter = nextBetter()))
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

private abstract class DealConstants(val players: List<DealPlayer>) {
    abstract val initialBettingRound: Int
    val dealer = players.last().uuid
    abstract val smallBlind: String
    abstract val bigBlind: String
    abstract val playerAfterBigBlind: Int
}

private class TwoPlayerDeal(players: List<DealPlayer>) : DealConstants(players) {
    override val initialBettingRound = 1
    override val smallBlind = dealer
    override val bigBlind = players.first().uuid
    override val playerAfterBigBlind = 1
}

private class ManyPlayersDeal(players: List<DealPlayer>) : DealConstants(players) {
    override val initialBettingRound = 2
    override val smallBlind = players.first().uuid
    override val bigBlind = players[1].uuid
    override val playerAfterBigBlind = 2
}

class InvalidMove(s: String) : Throwable()

