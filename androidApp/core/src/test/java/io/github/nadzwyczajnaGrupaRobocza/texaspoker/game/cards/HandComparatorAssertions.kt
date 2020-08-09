package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.lessThan

fun assertHandsEqual(
    lhs: Hand,
    rhs: Hand
) {
    assertThat(lhs.compareTo(rhs), equalTo(0))
}

fun assertHandBigger(
    lhs: Hand,
    rhs: Hand
) {
    assertThat(lhs.compareTo(rhs), greaterThan(0))
}

fun assertHandLess(
    lhs: Hand,
    rhs: Hand
) {
    assertThat(lhs.compareTo(rhs), lessThan(0))
}
