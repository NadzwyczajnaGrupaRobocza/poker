package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Game(val players: List<Player>) {
    init {
        throw InvalidPlayersNumber(players.size)
    }

}

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
