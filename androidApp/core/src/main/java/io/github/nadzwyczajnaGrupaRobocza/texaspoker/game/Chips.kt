package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import kotlin.math.abs

class Chips(initialAmount: Int) {
    fun change(change: ChipsChange) {
        if (change.change < 0 && abs(change.change) > amount)
            throw NotEnoughChips()
        changes.add(change.change)
    }

    override fun toString(): String {
        return "Chips(amount=$amount)"
    }

    val amount: Int
        get() = changes.sum()

    private val changes = listOf(initialAmount).toMutableList()
}

class ChipsChange(val change: Int) {
    override fun toString(): String {
        return "ChipsChange(change=$change)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is ChipsChange) {
            if (change == other.change) return true
        }
        return false
    }

    fun plus(rhs : ChipsChange) = ChipsChange(change + rhs.change)

    override fun hashCode(): Int {
        return change
    }
}

class NotEnoughChips : Throwable()
