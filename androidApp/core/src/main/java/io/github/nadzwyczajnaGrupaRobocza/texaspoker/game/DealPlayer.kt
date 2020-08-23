package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class DealPlayer(val uuid: String, initialChips: Int) {
    val chips = Chips(initialChips)

    override fun toString(): String {
        return "DealPlayer(uuid='$uuid', chips=${chips.amount})"
    }
}
