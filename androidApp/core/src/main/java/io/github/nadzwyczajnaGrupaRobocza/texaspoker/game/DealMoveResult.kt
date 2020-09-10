package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

class FinalDealResult(val winner: PlayerId, val players: Map<PlayerId, ChipsChange>) {
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

class IntermediateDealResult(val nextBetter: PlayerId) {
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

class NextRoundResult(val nextBetter: PlayerId) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NextRoundResult

        if (nextBetter != other.nextBetter) return false

        return true
    }

    override fun hashCode(): Int {
        return nextBetter.hashCode()
    }

    override fun toString(): String {
        return "NextRoundResult(nextBetter='$nextBetter')"
    }
}

class DealMoveResult(
    val intermediate: IntermediateDealResult? = null,
    val final: FinalDealResult? = null,
    val nextRound: NextRoundResult? = null
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
