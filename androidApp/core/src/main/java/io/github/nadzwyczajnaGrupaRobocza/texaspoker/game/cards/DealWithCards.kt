package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.CardsDistribution
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.Deal

data class DealWithCards(val deal: Deal, val cards: Deck) {
    val cardsDistribution = CardsDistribution.createCardsDistribution(cards, deal.players().map { it.uuid })
}
