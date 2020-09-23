package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Card
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Deck
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.FlopCommunityCards
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.NoCommunityCards

data class PlayerWithCards(val id: PlayerId, val cards: List<Card>) {
    init {
        assert(cards.size == 2)
    }
}

class CardsDistribution private constructor(
    val playersCards: List<PlayerWithCards>,
    val flopCommunityCards: FlopCommunityCards
) {

    companion object {
        fun createCardsDistribution(deck: Deck, players: List<PlayerId>): CardsDistribution {
            val (playersCards, cardsLeft) = dealCardsAmongPlayers(deck, players)
            val noCommunityCards = NoCommunityCards()
            val (flopCards, cardsLeftAfterFlop) = takeFlopCards(cardsLeft)
            val flopCommunityCards =
                noCommunityCards.flop(flopCards.first, flopCards.second, flopCards.third)
            return CardsDistribution(playersCards, flopCommunityCards)
        }

        private fun dealCardsAmongPlayers(
            deck: List<Card>,
            players: List<PlayerId>
        ): Pair<List<PlayerWithCards>, List<Card>> {
            val playersSize = players.size
            val cardsPerPlayer = 2
            return Pair(players.mapIndexed { index, player ->
                PlayerWithCards(
                    player,
                    deck.slice(index..(index + playersSize) step playersSize)
                )
            }, deck.drop(playersSize * cardsPerPlayer))
        }

        private fun takeFlopCards(deck: Deck) =
            Pair(Triple(deck[0], deck[1], deck[2]), deck.drop(3))
    }
}

