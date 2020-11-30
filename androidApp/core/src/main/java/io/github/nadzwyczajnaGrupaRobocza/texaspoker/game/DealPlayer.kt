package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class DealPlayer(val uuid: PlayerId, initialChips: Int, chipsBet: Int = 0) {
    val chips = Chips(initialChips)
    val chipsBet = Chips(chipsBet)

    override fun toString(): String {
        return "DealPlayer(uuid='$uuid', chips=${chips.amount})"
    }
}

typealias DealPlayers = List<DealPlayer>
