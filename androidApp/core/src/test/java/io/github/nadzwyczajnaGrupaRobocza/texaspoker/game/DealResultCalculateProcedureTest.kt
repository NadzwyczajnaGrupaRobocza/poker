package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.*
import org.junit.Test

class DealResultCalculateProcedureTest : FivePlayersDealTestData() {
    private val lostChips1Amount = 20
    private val lostChips1 = ChipsChange(-lostChips1Amount)
    private val lostChips2Amount = 80
    private val lostChips2 = ChipsChange(-lostChips2Amount)
    private val wonChipsAmount = 120
    private val wonChips1 = ChipsChange(wonChipsAmount)
    private val noChipsChange = ChipsChange(0)
    private val initialChips = 100

    @Test
    fun `When one player left after round should return this player as winner`() {
        val dealPlayers = listOf(
            DealPlayer(player1Id, initialChips, lostChips1Amount),
            DealPlayer(player2Id, initialChips, wonChipsAmount),
            DealPlayer(player3Id, initialChips, lostChips2Amount),
            DealPlayer(player4Id, initialChips, lostChips1Amount),
            DealPlayer(player5Id, initialChips),
        )

        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                FinalDealResult(
                    winner = player2Id,
                    players = emptyMap()
                )
            ), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, lostChips1),
                        PlayerResult(player2Id, wonChips1),
                        PlayerResult(player3Id, lostChips2),
                        PlayerResult(player4Id, lostChips1),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )
    }

    @Test
    fun `When multiple players left should win player with best hand`() {
        val firstPlayerCard1 = heartsFive
        val firstPlayerCard2 = diamondsFive
        val secondPlayerCard1 = heartsThree
        val secondPlayerCard2 = diamondsThree
        val thirdPlayerCard1 = diamondsKing
        val thirdPlayerCard2 = spadesEight
        val fourthPlayerCard1 = spadesFive
        val fourthPlayerCard2 = clubsJack
        val fifthPlayerCard1 = clubsKing
        val fifthPlayerCard2 = clubsNine
        val burn1 = spadesKing
        val flopCards = listOf(diamondsQueen, clubsFour, heartsSeven)
        val burn2 = spadesAce
        val turn = spadesQueen
        val burn3 = diamondsAce
        val river = clubsQueen
        val cardDistribution =
            CardsDistribution.createCardsDistribution(
                listOf(firstPlayerCard1) + secondPlayerCard1 + thirdPlayerCard1 + fourthPlayerCard1 + fifthPlayerCard1 + firstPlayerCard2 + secondPlayerCard2 + thirdPlayerCard2 + fourthPlayerCard2 + fifthPlayerCard2 + burn1 + flopCards + burn2 + turn + burn3 + river,
                playersIds
            )


    }
}

