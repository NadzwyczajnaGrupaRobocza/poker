import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Hand
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.HandType
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Rank

private data class RanksToCompare(val lhs: Rank, val rhs: Rank)

private val equal = 0
private val bigger = 1
private val less = -1

fun Hand.compareTo(other: Hand): Int =
    when {
        this.type < other.type -> less
        this.type > other.type -> bigger
        this.type == HandType.HighCard -> compareHighCards(this, other)
        this.type == HandType.Pair -> comparePairs(this, other)
        else -> equal
    }

fun compareHighCards(lhs: Hand, rhs: Hand): Int {
    val lhsCards = lhs.cards.sortedByDescending { it.rank }.take(5)
    val rhsCards = rhs.cards.sortedByDescending { it.rank }.take(5)
    val rankPairs =
        lhsCards.mapIndexed { index, card -> RanksToCompare(card.rank, rhsCards[index].rank) }
    return compareToFirstNotEqual(rankPairs)
}

fun comparePairs(lhs: Hand, rhs: Hand): Int {
    return equal
}

private fun compareToFirstNotEqual(ranks: List<RanksToCompare>): Int = when {
    ranks.isEmpty() -> equal
    ranks.first().lhs == ranks.first().rhs -> compareToFirstNotEqual(ranks.drop(1))
    ranks.first().lhs < ranks.first().rhs -> less
    else -> bigger
}

