package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import kotlin.math.abs

class Chips(initialAmount: Int) {
    fun change(change: ChipsChange) {
        if (change.change < 0 && abs(change.change) > amount)
            throw NotEnoughChips()
        changes.add(change.change)
    }

    val amount: Int
        get() = changes.sum()

    private val changes = listOf(initialAmount).toMutableList()
}

class ChipsChange(val change: Int)

class NotEnoughChips : Throwable()
