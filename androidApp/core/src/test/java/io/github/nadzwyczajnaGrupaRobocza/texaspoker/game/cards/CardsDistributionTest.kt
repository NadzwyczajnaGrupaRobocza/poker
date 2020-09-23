package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.CardsDistribution
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.Player
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.PlayerId
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.PlayerWithCards
import org.junit.Test

class CardsDistributionTest {
    private val player1Id = PlayerId("1")
    private val player1 = Player(player1Id)
    private val player2Id = PlayerId("2")
    private val player2 = Player(player2Id)
    private val player3Id = PlayerId("3")
    private val player3 = Player(player3Id)
    private val player4Id = PlayerId("4")
    private val player4 = Player(player4Id)
    private val players = listOf(player1, player2, player3, player4)
    private val deck = Decks.shuffledDeck()
    private val distribution = CardsDistribution(deck, players)

    @Test
    fun `should distribute cards among players`() {
        val player1Cards = listOf(deck[0], deck[players.size])
        val player2Cards = listOf(deck[1], deck[players.size + 1])
        val player3Cards = listOf(deck[2], deck[players.size + 2])
        val player4Cards = listOf(deck[3], deck[players.size + 3])
        val playersCards = listOf(
            PlayerWithCards(player1Id, player1Cards),
            PlayerWithCards(player2Id, player2Cards),
            PlayerWithCards(player3Id, player3Cards),
            PlayerWithCards(player4Id, player4Cards),
        )

        assertThat(distribution.playersCards, equalTo(playersCards))
    }
}