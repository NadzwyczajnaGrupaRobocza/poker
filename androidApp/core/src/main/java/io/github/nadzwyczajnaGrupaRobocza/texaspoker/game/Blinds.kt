package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class Blinds(val small: Int, val big: Int) {
    constructor(small: Int) : this(small, small * 2)
}
