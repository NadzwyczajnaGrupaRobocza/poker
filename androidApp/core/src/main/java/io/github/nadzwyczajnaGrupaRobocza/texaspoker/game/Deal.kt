package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Deal(val players: List<DealPlayer>) {
    init {
        assert(players.size > 1)
    }

    val dealer = players.last().uuid
    val smallBlind = if (players.size == 2) dealer else players.first().uuid
    val bigBlind = if (players.size == 2) players.first().uuid else players[1].uuid
}
