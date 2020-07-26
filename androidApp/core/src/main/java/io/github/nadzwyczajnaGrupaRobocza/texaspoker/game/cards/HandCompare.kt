import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Hand
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Rank

private data class RanksToCompare(val lhs: Rank, val rhs: Rank)

private val equal = 0
private val bigger = 1
private val lesser = -1

fun Hand.compareTo(other: Hand): Int {
    val ownCards = this.cards.sortedByDescending { it.rank }.take(5)
    val otherCards = other.cards.sortedByDescending { it.rank }.take(5)
    val rankPairs =
        ownCards.mapIndexed { index, card -> RanksToCompare(card.rank, otherCards[index].rank) }
    return compareToFirstNotEqual(rankPairs)
}

private fun compareToFirstNotEqual(ranks: List<RanksToCompare>): Int = when {
    ranks.isEmpty() -> equal
    ranks.first().lhs == ranks.first().rhs -> compareToFirstNotEqual(ranks.drop(1))
    ranks.first().lhs < ranks.first().rhs -> lesser
    else -> bigger
}

