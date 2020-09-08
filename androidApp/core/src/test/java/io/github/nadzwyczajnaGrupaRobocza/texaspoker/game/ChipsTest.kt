package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.throws
import org.junit.Test

class ChipsTest {
    private val initial = 1000
    private val chips = Chips(initial)

    @Test
    fun `initially chips should be equal to initial chips`() {
        assertThat(chips.amount, equalTo(initial))
    }

    @Test
    fun `after change chips should be equal initial with applied change`() {
        val after = 800
        val change = ChipsChange(-200)

        chips.change(change)

        assertThat(chips.amount, equalTo(after))
    }

    @Test
    fun `after few changes chips should have correct sum`() {
        val after = 0
        val changes = listOf(ChipsChange(-300), ChipsChange(1300), ChipsChange(-2000))

        changes.forEach { chips.change(it) }

        assertThat(chips.amount, equalTo(after))
    }

    @Test
    fun `chips cannot drop below zero`() {
        val change = ChipsChange(-1001)

        assertThat({chips.change(change)}, throws<NotEnoughChips>())

    }
}
