package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

data class Blinds(val small: Int, val big: Int) {
    constructor(small: Int) : this(small, small * 2)
}
