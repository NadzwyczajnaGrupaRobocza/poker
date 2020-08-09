package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

private data class RanksToCompare(val lhs: Rank, val rhs: Rank)

private const val equal = 0
private const val bigger = 1
private const val less = -1

fun Hand.compareTo(other: Hand): Int =
    when {
        this.type < other.type -> less
        this.type > other.type -> bigger
        else -> compareFirstImportantCardsThenKickers(this, other)
    }

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

