package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

abstract class Player(val uuid: PlayerId) {
    override fun toString(): String {
        return "Player(uuid='$uuid')"
    }

    abstract fun move(): DealMove
}

class AlwaysFoldPlayer(uuid:PlayerId) : Player(uuid) {
    override fun move() = DealMove.fold()
}

typealias Players = Map<PlayerId, Player>
