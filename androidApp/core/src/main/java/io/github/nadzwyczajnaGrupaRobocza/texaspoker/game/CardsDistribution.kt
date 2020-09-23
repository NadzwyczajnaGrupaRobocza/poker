package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.*

data class PlayerWithCards(val id: PlayerId, val cards: List<Card>) {
    init {
        assert(cards.size == 2)
    }
}

class CardsDistribution private constructor(
    val playersCards: List<PlayerWithCards>,
    val flopCommunityCards: FlopCommunityCards,
    val turnCommunityCards: TurnCommunityCards,
    val riverCommunityCards: RiverCommunityCards,
) {

    companion object {
        fun createCardsDistribution(deck: Deck, players: List<PlayerId>): CardsDistribution {
            val (playersCards, cardsLeft) = dealCardsAmongPlayers(deck, players)
            val noCommunityCards = NoCommunityCards()
            val (flopCards, cardsLeftAfterFlop) = takeFlopCards(cardsLeft)
            val flopCommunityCards =
                noCommunityCards.flop(flopCards.first, flopCards.second, flopCards.third)
            val (turnCard, cardsLeftAfterTurn) = takeCard(cardsLeftAfterFlop)
            val turnCommunityCards = flopCommunityCards.turn(turnCard)
            val (riverCard, _) = takeCard(cardsLeftAfterTurn)
            val riverCommunityCards = turnCommunityCards.river(riverCard)
            return CardsDistribution(playersCards, flopCommunityCards, turnCommunityCards, riverCommunityCards)
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

        private fun takeCard(deck: Deck) =
            Pair(deck.first(), deck.drop(1))
    }
}

