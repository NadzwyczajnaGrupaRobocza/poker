package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Game(players: List<Player>, startingChips: Int) {
    fun deal(): Deal {
        val playersToPlay = activePlayersInOrder
        if (playersToPlay.size == 1)
            throw InvalidPlayersNumber(1)
        val shift = dealsCount % playersToPlay.size
        val deal = Deal(playersToPlay.drop(shift) + playersToPlay.take(shift))
        dealsCount = dealsCount.inc()
        return deal
    }

    fun acceptDealResult(result: DealResult) {
        result.playersResults.forEach {
            players[it.uuid]?.chipsChange(it.chips)
        }
    }

    val activePlayers: Set<Player>
        get() = activePlayersInOrder.toSet()

    private val activePlayersInOrder: List<Player>
        get() = players.filter { it.value.chips.amount > 0 }.toExternal()

    private val players = players.toInternal(startingChips)
    private var dealsCount = 0

    init {
        if (players.size > 8 || players.size < 2) throw InvalidPlayersNumber(players.size)
    }

}

private class InternalPlayer(val player: Player, val chips: Chips) {
    fun chipsChange(change: ChipsChange) {
        chips.change(change)
    }
}

private fun List<Player>.toInternal(startingChips: Int) =
    map { it.uuid to InternalPlayer(it, Chips(startingChips)) }.toMap()

private fun Map<String, InternalPlayer>.toExternal() = map { it.value.player }

class InvalidPlayersNumber(val size: Int) : Throwable() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InvalidPlayersNumber

        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        return size
    }
}


