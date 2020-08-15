package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Game(players: List<Player>, startingChips: Int) {
    val activePlayers: Set<Player>
        get() = players.filter { it.chips > 0 }.toExternal().toSet()

    private val players = players.toInternal(startingChips)

    init {
        if (players.size > 8 || players.size < 2) throw InvalidPlayersNumber(players.size)
    }

}

private class InternalPlayer(val player: Player, val chips: Int)

private fun List<Player>.toInternal(startingChips: Int) = map { InternalPlayer(it, startingChips) }

private fun List<InternalPlayer>.toExternal() = map { it.player }

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


