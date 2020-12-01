package io.github.nadzwyczajnaGrupaRobocza.texaspoker.math

import java.lang.Math

fun degree(rad: Double): Double {
    return rad * 180 / Math.PI
}

fun radian(degree: Double): Double {
    return degree / 180 * Math.PI
}
