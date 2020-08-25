package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class FinalDealResult(val winner: String, val players: Map<String, ChipsChange>) {
    override fun toString(): String {
        return "FinalDealResult(winner='$winner', players=$players)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FinalDealResult

        if (winner != other.winner) return false
        if (players != other.players) return false

        return true
    }

    override fun hashCode(): Int {
        var result = winner.hashCode()
        result = 31 * result + players.hashCode()
        return result
    }
}

class IntermediateDealResult(val nextBetter: String) {
    override fun toString(): String {
        return "IntermediateDealResult(nextBetter='$nextBetter')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntermediateDealResult

        if (nextBetter != other.nextBetter) return false

        return true
    }

    override fun hashCode(): Int {
        return nextBetter.hashCode()
    }

}

class NextRoundResult() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return "NextRoundResult()"
    }
}

class DealMoveResult(
    val intermediate: IntermediateDealResult? = null,
    val final: FinalDealResult? = null,
    val nextRound : NextRoundResult? = null
) {
    override fun toString(): String {
        return "DealMoveResult(intermediate=$intermediate, final=$final, nextRound=$nextRound)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DealMoveResult

        if (intermediate != other.intermediate) return false
        if (final != other.final) return false
        if (nextRound != other.nextRound) return false

        return true
    }

    override fun hashCode(): Int {
        var result = intermediate?.hashCode() ?: 0
        result = 31 * result + (final?.hashCode() ?: 0)
        result = 31 * result + (nextRound?.hashCode() ?: 0)
        return result
    }


}
