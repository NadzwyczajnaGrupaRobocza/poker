package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class DealMove private constructor(
    val chipsChange: ChipsChange = ChipsChange(0),
    val folded: Boolean = false
) {
    companion object {
        fun fold() = DealMove(folded = true)
        fun check() = DealMove()
        fun call(chips: ChipsChange) = DealMove(chipsChange = chips)
        fun raise(chips: ChipsChange) = DealMove(chipsChange = chips)
    }
}
