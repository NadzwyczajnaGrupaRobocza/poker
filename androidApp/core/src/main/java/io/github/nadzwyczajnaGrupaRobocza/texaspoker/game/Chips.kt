package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Chips(initialAmount: Int) {
    fun change(change: ChipsChange) {
        changes.add(change.change)
    }

    val amount: Int
        get() = changes.sum()

    private val changes = listOf(initialAmount).toMutableList()
}

class ChipsChange(val change: Int)
