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
    private val maxBetChipsAmount = 200
    private val maxBetChipsLost = ChipsChange(-maxBetChipsAmount)
    private val showdownChipsWon =
        ChipsChange(maxBetChipsAmount + lostChips1Amount + lostChips2Amount)
    private val wonChipsAmount = 120
    private val wonChips1 = ChipsChange(wonChipsAmount)
    private val noChipsChange = ChipsChange(0)
    private val initialChips = 1000

    @Test
    fun `When one player left after round should return this player as winner`() {
        val dealPlayers = listOf(
            DealPlayer(player1Id, initialChips, lostChips1Amount),
            DealPlayer(player2Id, initialChips, maxBetChipsAmount),
            DealPlayer(player3Id, initialChips, lostChips2Amount),
            DealPlayer(player4Id, initialChips, lostChips1Amount),
            DealPlayer(player5Id, initialChips),
        )

        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                FinalDealResult(
                    winner = player2Id,
                    players = mapOf(
                        player1Id to lostChips1,
                        player2Id to wonChips1,
                        player3Id to lostChips2,
                        player4Id to lostChips1,
                        player5Id to noChipsChange,
                    )
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

        val dealPlayers = listOf(
            DealPlayer(player1Id, initialChips, maxBetChipsAmount),
            DealPlayer(player2Id, initialChips, maxBetChipsAmount),
            DealPlayer(player3Id, initialChips, lostChips2Amount),
            DealPlayer(player4Id, initialChips, lostChips1Amount),
            DealPlayer(player5Id, initialChips),
        )

        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(cardDistribution), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, showdownChipsWon),
                        PlayerResult(player2Id, maxBetChipsLost),
                        PlayerResult(player3Id, lostChips2),
                        PlayerResult(player4Id, lostChips1),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )

    }

    @Test
    fun `When multiple players left given equal hands should split win`() {
        val firstPlayerCard1 = heartsFive
        val firstPlayerCard2 = diamondsFive
        val secondPlayerCard1 = heartsThree
        val secondPlayerCard2 = diamondsThree
        val thirdPlayerCard1 = diamondsKing
        val thirdPlayerCard2 = spadesEight
        val fourthPlayerCard1 = spadesFive
        val fourthPlayerCard2 = clubsFive
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

        val dealPlayers = listOf(
            DealPlayer(player1Id, initialChips, maxBetChipsAmount),
            DealPlayer(player2Id, initialChips, maxBetChipsAmount),
            DealPlayer(player3Id, initialChips, lostChips2Amount),
            DealPlayer(player4Id, initialChips, maxBetChipsAmount),
            DealPlayer(player5Id, initialChips),
        )

        val splitChipsWon = ChipsChange((maxBetChipsAmount + lostChips2Amount) / 2)
        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(cardDistribution), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, splitChipsWon),
                        PlayerResult(player2Id, maxBetChipsLost),
                        PlayerResult(player3Id, lostChips2),
                        PlayerResult(player4Id, splitChipsWon),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )

    }
}
