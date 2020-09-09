package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Deal(gamePlayers: List<DealPlayer>, private val blinds: Blinds) {
    init {
        assert(gamePlayers.size > 1)
    }

    private val dealConstants = createDealConstants(gamePlayers)
    private val internalPlayers = gamePlayers.toInternal()
    private val internalPot = Chips(0)
    private val bettingStep =
        BettingStep(
            dealConstants.firstRoundBettingPlayerIndicator,
            dealConstants.smallBlindPlayerIndicator,
            internalPlayers,
        )

    private fun createDealConstants(players: List<DealPlayer>) = when (players.size) {
        2 -> TwoPlayerDeal(players)
        else -> ManyPlayersDeal(players)
    }

    fun players() = internalPlayers.toDealPlayers()
    fun nextBetter() = bettingStep.getBetter().dealPlayer.uuid

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

        val biggestBet = internalPlayers.maxOf { it.chipsBet.amount }
        val typeOfMove = calculateMoveType(move, biggestBet)
        val currentBetter = bettingStep.getBetterIndicator()
        bettingStep.step()
        val activePlayers = internalPlayers.filter { it.notFolded() }

        return when {
            typeOfMove == MoveType.Raise -> moveWithRaise()
            activePlayers.size == 1 -> getResultWithWinner(activePlayers)
            internalPlayers.all { it.betInRound(biggestBet) } -> moveToNextRound()
            else -> DealMoveResult(intermediate = IntermediateDealResult(nextBetter()))
        }
    }

    private fun checkAndMarkFolded(move: DealMove) {
        if (move.folded) {
            bettingStep.getBetter().folded = true
        }
    }

    private fun moveWithRaise(): DealMoveResult {
        return DealMoveResult(intermediate = IntermediateDealResult(nextBetter()))
    }

    private fun calculateMoveType(move: DealMove, biggestBet: Int): MoveType {
        val moveChips = move.chipsChange.change
        val currentPlayer = bettingStep.getBetter()
        val currentPlayerBet = currentPlayer.chipsBet.amount

        return when {
            currentPlayer.folded -> MoveType.Fold
            currentPlayerBet < biggestBet -> throw InvalidMove("To call player need to equal biggestBet ($biggestBet). Current bet $currentPlayerBet")
            currentPlayerBet != biggestBet && moveChips == 0 -> throw InvalidMove("Should fold/call/raise when bet ($currentPlayerBet) less then max bet ($biggestBet)")
            currentPlayerBet == biggestBet && moveChips == 0 -> MoveType.Check
            currentPlayerBet == biggestBet -> MoveType.Call
            else -> MoveType.Raise
        }
    }

    private fun bet(move: DealMove) {
        val moveChips = move.chipsChange.change
        val currentPlayer = bettingStep.getBetter()
        currentPlayer.betOnce = true
        getChipsFromPlayer(currentPlayer, moveChips)
    }

    enum class MoveType {
        Call,
        Raise,
        Check,
        Fold
    }

    private fun getResultWithWinner(activePlayers: List<InternalPlayer>): DealMoveResult {
        val winner = activePlayers.first()
        return DealMoveResult(
            final = FinalDealResult(
                winner = winner.dealPlayer.uuid,
                players =
                (listOf(
                    winner.dealPlayer.uuid to
                            ChipsChange(internalPot.amount - winner.chipsBet.amount)
                ) + internalPlayers.filter { it.dealPlayer.uuid != winner.dealPlayer.uuid }
                    .map {
                        it.dealPlayer.uuid to
                                ChipsChange(-it.chipsBet.amount)
                    }).toMap()
            )
        )
    }

    private fun moveToNextRound(): DealMoveResult {
        internalPlayers.forEach { it.betOnce = false }
        bettingStep.nextRound()
        return DealMoveResult(nextRound = NextRoundResult(nextBetter = nextBetter()))
    }

    private fun getChipsFromPlayer(player: InternalPlayer, amount: Int) {
        player.dealPlayer.chips.change(ChipsChange(-amount))
        player.chipsBet.change(ChipsChange(amount))
        internalPot.change(ChipsChange(amount))
    }

    class InternalPlayer(val dealPlayer: DealPlayer) {
        var betOnce = false
        val chipsBet = Chips(0)
        var folded = false

        fun betInRound(biggestBet: Int) =
            folded || equalToBiggestBet(biggestBet) || allInBet()

        private fun equalToBiggestBet(biggestBet: Int) = betOnce && chipsBet.amount == biggestBet

        fun allInBet() = dealPlayer.chips.amount == 0 && chipsBet.amount > 0
    }
}

private fun Deal.InternalPlayer.notFolded() = !folded
private fun List<DealPlayer>.toInternal() = map { Deal.InternalPlayer(it) }
private fun List<Deal.InternalPlayer>.toDealPlayers() = map { it.dealPlayer }

private abstract class DealConstants(val players: List<DealPlayer>) {
    abstract val firstRoundBettingPlayerIndicator: Int
    val dealer = players.last().uuid
    abstract val smallBlind: String
    abstract val smallBlindPlayerIndicator: Int
    abstract val bigBlind: String
    abstract val playerAfterBigBlind: Int
}

private class TwoPlayerDeal(players: List<DealPlayer>) : DealConstants(players) {
    override val firstRoundBettingPlayerIndicator = 1
    override val smallBlind = dealer
    override val smallBlindPlayerIndicator = players.size - 1
    override val bigBlind = players.first().uuid
    override val playerAfterBigBlind = 1
}

private class ManyPlayersDeal(players: List<DealPlayer>) : DealConstants(players) {
    override val firstRoundBettingPlayerIndicator = 2
    override val smallBlind = players.first().uuid
    override val smallBlindPlayerIndicator = 0
    override val bigBlind = players[1].uuid
    override val playerAfterBigBlind = 2
}

private class BettingStep(
    firstRoundStartingPlayer: Int,
    val otherRoundsStartingPlayer: Int,
    val players: List<Deal.InternalPlayer>
) {
    private class PlayerIndicator(var indicator: Int, val players: List<Deal.InternalPlayer>) {
        private tailrec fun getNextNotFoldedNorAllIn(ind: Int): Int =
            if (players[ind].folded || players[ind].allInBet()) getNextNotFoldedNorAllIn(
                nextIndex(
                    ind
                )
            ) else ind

        fun restart(position: Int) {
            indicator = getNextNotFoldedNorAllIn(getIndex(position))
        }

        operator fun inc(): PlayerIndicator {
            indicator = getNextNotFoldedNorAllIn(nextIndex(indicator))
            return this
        }

        private fun nextIndex(indicator: Int) = getIndex(indicator + 1)
        private fun getIndex(i: Int) = i % players.size
    }

    private var bettingPlayer =
        PlayerIndicator(indicator = firstRoundStartingPlayer, players = players)

    fun nextRound() {
        bettingPlayer.restart(otherRoundsStartingPlayer)
    }

    fun step() {
        bettingPlayer++
    }

    fun getBetterIndicator() = bettingPlayer.indicator
    fun getBetter() = players[bettingPlayer.indicator]
}

class InvalidMove(private val why: String) : Throwable() {
    override fun toString(): String {
        return "InvalidMove: $why"
    }
}

