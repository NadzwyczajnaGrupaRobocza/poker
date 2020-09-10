package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

data class FinalDealResult(val winner: PlayerId, val players: Map<PlayerId, ChipsChange>)

data class IntermediateDealResult(val nextBetter: PlayerId)

data class NextRoundResult(val nextBetter: PlayerId)

data class DealMoveResult(
    val intermediate: IntermediateDealResult? = null,
    val final: FinalDealResult? = null,
    val nextRound: NextRoundResult? = null
)
