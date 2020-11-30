package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

private data class RanksToCompare(val lhs: Rank, val rhs: Rank)

internal const val bigger = 1
internal const val less = -1
internal const val equal = 0

fun compareFirstImportantCardsThenKickers(lhs: Hand, rhs: Hand) =
    when (val it = compareCards(lhs.importantCards, rhs.importantCards)) {
        equal -> compareCards(lhs.kickers, rhs.kickers)
        else -> it
    }

fun compareCards(lhs: List<Card>, rhs: List<Card>): Int {
    val rankPairs =
        lhs.mapIndexed { index, card -> RanksToCompare(card.rank, rhs[index].rank) }
    return compareToFirstNotEqual(rankPairs)
}

private fun compareToFirstNotEqual(ranks: List<RanksToCompare>): Int = when {
    ranks.isEmpty() -> equal
    ranks.first().lhs == ranks.first().rhs -> compareToFirstNotEqual(ranks.drop(1))
    ranks.first().lhs < ranks.first().rhs -> less
    else -> bigger
}

