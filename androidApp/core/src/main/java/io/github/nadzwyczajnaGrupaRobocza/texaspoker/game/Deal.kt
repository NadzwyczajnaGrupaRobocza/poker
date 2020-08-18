package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Deal(val players : List<Player>) {
    init {
        assert(players.size > 2)
    }

    val dealer = players.last().uuid
    val smallBlind = players.first().uuid
    val bigBlind = players[1].uuid
}
