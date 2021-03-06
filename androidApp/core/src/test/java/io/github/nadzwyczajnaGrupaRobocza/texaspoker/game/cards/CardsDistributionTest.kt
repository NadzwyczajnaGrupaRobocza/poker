package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.CardsDistribution
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.PlayerId
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.PlayerWithCards
import org.junit.Test

class CardsDistributionTest {
    private val player1Id = PlayerId("1")
    private val player2Id = PlayerId("2")
    private val player3Id = PlayerId("3")
    private val player4Id = PlayerId("4")
    private val players = listOf(player1Id, player2Id, player3Id, player4Id)
    private val deck = Decks.shuffledDeck()
    private val distribution = CardsDistribution.createCardsDistribution(deck, players)

    @Test
    fun `should distribute cards among players`() {
        val player1Cards = PocketCards(deck[0], deck[players.size])
        val player2Cards = PocketCards(deck[1], deck[players.size + 1])
        val player3Cards = PocketCards(deck[2], deck[players.size + 2])
        val player4Cards = PocketCards(deck[3], deck[players.size + 3])
        val playersCards = listOf(
            PlayerWithCards(player1Id, player1Cards),
            PlayerWithCards(player2Id, player2Cards),
            PlayerWithCards(player3Id, player3Cards),
            PlayerWithCards(player4Id, player4Cards),
        )

        assertThat(distribution.playersCards, equalTo(playersCards))
    }

    @Test
    fun `should distribute flop`() {
        assertThat(
            distribution.flopCommunityCards.cards,
            equalTo(listOf(deck[9], deck[10], deck[11]))
        )
    }

    @Test
    fun `should distribute turn`() {
        assertThat(distribution.turnCommunityCards.turn, equalTo(deck[13]))
    }

    @Test
    fun `should distribute river`() {
        assertThat(distribution.riverCommunityCards.river, equalTo(deck[15]))
    }
}
