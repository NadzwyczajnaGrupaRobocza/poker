package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Game(val players: List<Player>) {
    init {
        throw NotEnoughPlayers(players.size)
    }

}

class NotEnoughPlayers(val size: Int) : Throwable() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NotEnoughPlayers

        if (size != other.size) return false

        return true
    }

    override fun hashCode(): Int {
        return size
    }
}
