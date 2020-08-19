package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class DealPlayer(val uuid: String, val initialChips :Int) {
    override fun toString(): String {
        return "DealPlayer(uuid='$uuid', initialChips=$initialChips)"
    }
}
