package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Game(players: List<Player>, private val gameConfiguration: GameConfiguration) {
    fun deal(): Deal {
        val playersToPlay = activePlayersInOrder
        if (playersToPlay.size == 1)
            throw InvalidPlayersNumber(1)
        val shift = dealsCount % playersToPlay.size
        val deal =
            Deal((playersToPlay.drop(shift) + playersToPlay.take(shift)).toDealPlayer(), gameConfiguration.blinds)
        dealsCount = dealsCount.inc()
        return deal
    }

    fun acceptDealResult(result: DealResult): String? {
        result.playersResults.forEach {
            players[it.uuid]?.chipsChange(it.chips)
        }
        val playersLeft = activePlayers
        return when (activePlayers.size) {
            1 -> playersLeft.first().uuid
            else -> null
        }
    }

    val activePlayers: Set<Player>
        get() = activePlayersInOrder.toExternal().toSet()

    private val activePlayersInOrder: List<InternalPlayer>
        get() = players.filter { it.value.chips.amount > 0 }.values.toList()

    private val players = players.toInternal(gameConfiguration.startingChips)
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

private fun List<InternalPlayer>.toExternal() = map { it.player }

private fun List<InternalPlayer>.toDealPlayer() =
    map { DealPlayer(it.player.uuid, it.chips.amount) }


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


