package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Deal(val players: List<DealPlayer>, blinds: Blinds) {
    private val deal = createDeal(players)

    init {
        assert(players.size > 1)
    }

    private fun createDeal(players: List<DealPlayer>) = when (players.size) {
        2 -> TwoPlayerDeal(players)
        else -> ManyPlayersDeal(players)
    }

    private val betterId: Int
        get() = bettingRound % players.size
    private var bettingRound = deal.initialBettingRound
    private var internalPot = Chips(0)

    val nextBetter: String?
        get() = players[betterId].uuid
    val dealer: String
        get() = deal.dealer
    val smallBlind: String
        get() = deal.smallBlind
    val bigBlind: String
        get() = deal.bigBlind
    val pot: Int
        get() = internalPot.amount

    init {
        players.find { it.uuid == smallBlind }?.let { getChipsFromPlayer(it, blinds.small) }
        players.find { it.uuid == bigBlind }?.let { getChipsFromPlayer(it, blinds.big) }
    }

    fun move(move: DealMove) {
        bettingRound = bettingRound.inc()
    }

    private fun getChipsFromPlayer(player: DealPlayer, amount: Int) {
        player.chips.change(ChipsChange(-amount))
        internalPot.change(ChipsChange(amount))
    }
}

private abstract class DealImpl(val players: List<DealPlayer>) {
    abstract val initialBettingRound: Int
    val dealer = players.last().uuid
    abstract val smallBlind: String
    abstract val bigBlind: String

}

private class TwoPlayerDeal(players: List<DealPlayer>) : DealImpl(players) {
    override val initialBettingRound = 1
    override val smallBlind = dealer
    override val bigBlind = players.first().uuid
}

private class ManyPlayersDeal(players: List<DealPlayer>) : DealImpl(players) {
    override val initialBettingRound = 2
    override val smallBlind = players.first().uuid
    override val bigBlind = players[1].uuid
}
