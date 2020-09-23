package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Card
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Deck

data class PlayerWithCards(val id: PlayerId, val cards: List<Card>) {
    init {
        assert(cards.size == 2)
    }
}

class CardsDistribution(deck: Deck, players: List<Player>) {
    val playersCards: List<PlayerWithCards> = dealCardsAmongPlayers(deck, players)

    private fun dealCardsAmongPlayers(
        deck: List<Card>,
        players: List<Player>
    ): List<PlayerWithCards> {
        val playersSize = players.size
        return players.mapIndexed { index, player ->
            PlayerWithCards(
                player.uuid,
                deck.slice(index..(index + playersSize) step playersSize)
            )
        }
    }
}

