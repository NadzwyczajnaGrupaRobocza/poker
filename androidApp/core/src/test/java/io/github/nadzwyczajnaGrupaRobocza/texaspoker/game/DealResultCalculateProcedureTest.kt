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
    private val lostChips3Amount = 79
    private val lostChips3 = ChipsChange(-lostChips3Amount)
    private val maxBetChipsAmount = 200
    private val maxBetChipsLost = ChipsChange(-maxBetChipsAmount)
    private val showdownChipsWon =
        ChipsChange(maxBetChipsAmount + lostChips1Amount + lostChips2Amount)
    private val wonChipsAmount = 120
    private val wonChips1 = ChipsChange(wonChipsAmount)
    private val noChipsChange = ChipsChange(0)
    private val initialChips = 1000
    private val smallInitialChips = 150
    private val almostSmallInitialChips = smallInitialChips - 1
    private val anotherInitialChips = 888
    private val anotherInitialChips2 = 889
    private val nextRoundDealResult = NextRoundResult(player3Id)

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
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
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
    fun `When all players win should be no change in chips`() {
        val cardDistribution = getCardDistributionWithBestHandOnTable()

        val dealPlayers = getDealPlayersWithAllPlayersShowdown()

        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, noChipsChange),
                        PlayerResult(player2Id, noChipsChange),
                        PlayerResult(player3Id, noChipsChange),
                        PlayerResult(player4Id, noChipsChange),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )
    }

    @Test
    fun `When all players win with different all ins should be no change in chips`() {
        val cardDistribution = getCardDistributionWithBestHandOnTable()

        val dealPlayers = getDealPlayersWithAllPlayersShowdownWithSomeAllIn()

        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, noChipsChange),
                        PlayerResult(player2Id, noChipsChange),
                        PlayerResult(player3Id, noChipsChange),
                        PlayerResult(player4Id, noChipsChange),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )
    }

    @Test
    fun `When multiple players left given two equal hands should split win`() {
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

        val dealPlayers = getDealPlayersWithThreePlayersShowdown()

        val splitChipsWon = ChipsChange((maxBetChipsAmount + lostChips2Amount) / 2)
        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
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

    @Test
    fun `When cannot divide chips equally extra chips goes to first better left`() {
        val cardDistribution =
            getCardDistributionWithThreeWinners()

        val dealPlayers =
            listOf(
                DealPlayer(player1Id, initialChips, maxBetChipsAmount),
                DealPlayer(player2Id, initialChips, maxBetChipsAmount),
                DealPlayer(player3Id, initialChips, lostChips3Amount),
                DealPlayer(player4Id, initialChips, maxBetChipsAmount),
                DealPlayer(player5Id, initialChips),
            )

        val splitChipsWon = ChipsChange(lostChips3Amount / 3)
        val splitChipsWithLeftoverChipWon = ChipsChange(splitChipsWon.change + 1)
        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, splitChipsWon),
                        PlayerResult(player2Id, splitChipsWon),
                        PlayerResult(player3Id, lostChips3),
                        PlayerResult(player4Id, splitChipsWithLeftoverChipWon),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )
    }

    @Test
    fun `When cannot divide chips equally extra chips goes to first two betters left`() {
        val cardDistribution =
            getCardDistributionWithThreeWinners()

        val dealPlayers = getDealPlayersWithThreePlayersShowdown()

        val splitChipsWon = ChipsChange(lostChips2Amount / 3)
        val splitChipsWithLeftoverChipWon = ChipsChange(splitChipsWon.change + 1)
        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, splitChipsWithLeftoverChipWon),
                        PlayerResult(player2Id, splitChipsWon),
                        PlayerResult(player3Id, lostChips2),
                        PlayerResult(player4Id, splitChipsWithLeftoverChipWon),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )
    }

    @Test
    fun `When wins player with all in he should win "only all" in amount from other players, then rest goes to next winner`() {
        val cardDistribution =
            getCardDistributionWithBestHandsPlayer3Player2Player1Player4Player5()

        val dealPlayers = getDealPlayersWithPlayer3AllInAndPlayers2And1MaxBetRestWithoutBet()

        val restShowdownPlayersCount = 2
        val allInPlayerWins = ChipsChange(smallInitialChips * restShowdownPlayersCount)
        val restChipsLost = -100
        val maxBetPlayerChange = ChipsChange(restChipsLost)

        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, maxBetChipsLost),
                        PlayerResult(player2Id, maxBetPlayerChange),
                        PlayerResult(player3Id, allInPlayerWins),
                        PlayerResult(player4Id, noChipsChange),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )
    }

    @Test
    fun `When wins player with all and all other folded should win all pot`() {
        val cardDistribution =
            getCardDistributionWithBestHandsPlayer3Player2Player1Player4Player5()

        val dealPlayers = getDealPlayersWithAllPlayersWon()

        val betPlayersAndFolded = 2
        val allInPlayerWins = ChipsChange(almostSmallInitialChips * betPlayersAndFolded)
        val otherPlayerLost = ChipsChange(-almostSmallInitialChips)

        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, allInPlayerWins),
                        PlayerResult(player2Id, otherPlayerLost),
                        PlayerResult(player3Id, otherPlayerLost),
                        PlayerResult(player4Id, noChipsChange),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )
    }

    @Test
    fun `When split win with one all in player and other player should split win properly`() {
        val cardDistribution = getCardDistributionWithBestHandOnTable()

        val dealPlayers = getDealPlayersWithAllInPlayerAndOneMoreBetAndSomeLessBets()

        val allInPlayerWin = ChipsChange(smallInitialChips / 2 + lostChips2Amount / 2)
        val secondPlayerWin =
            ChipsChange(smallInitialChips / 2 + lostChips2Amount / 2 + lostChips2Amount)
        val player4Lost = ChipsChange(-lostChips2Amount - smallInitialChips)

        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, allInPlayerWin),
                        PlayerResult(player2Id, secondPlayerWin),
                        PlayerResult(player3Id, lostChips2),
                        PlayerResult(player4Id, player4Lost),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )
    }

    @Test
    fun `When split two all different all ins one should win half of own all in, second half of first all in plus difference between them`() {
        val cardDistribution = getCardDistributionWithThreeWinners()

        val dealPlayers = getDealPlayersWithTwoDifferentAllInsAndOneMaxBet()

        val firstAllInPlayerWin = ChipsChange(smallInitialChips / 2)
        val allInsDifference = maxBetChipsAmount - smallInitialChips
        val secondAllInPlayerWin = ChipsChange(smallInitialChips / 2 + allInsDifference)
        val playerLost = ChipsChange(-smallInitialChips - allInsDifference)

        assertThat(
            DealResultCalculateProcedure(dealPlayers).dealResult(
                nextRoundDealResult,
                cardDistribution
            ), equalTo(
                DealResult(
                    listOf(
                        PlayerResult(player1Id, firstAllInPlayerWin),
                        PlayerResult(player2Id, secondAllInPlayerWin),
                        PlayerResult(player3Id, playerLost),
                        PlayerResult(player4Id, noChipsChange),
                        PlayerResult(player5Id, noChipsChange),
                    )
                )
            )
        )
    }

    private fun getDealPlayersWithPlayer3AllInAndPlayers2And1MaxBetRestWithoutBet(): List<DealPlayer> =
        listOf(
            DealPlayer(player1Id, initialChips, maxBetChipsAmount),
            DealPlayer(player2Id, initialChips, maxBetChipsAmount),
            DealPlayer(player3Id, smallInitialChips, smallInitialChips),
            DealPlayer(player4Id, initialChips),
            DealPlayer(player5Id, initialChips),
        )

    private fun getDealPlayersWithThreePlayersShowdown(): List<DealPlayer> =
        listOf(
            DealPlayer(player1Id, initialChips, maxBetChipsAmount),
            DealPlayer(player2Id, initialChips, maxBetChipsAmount),
            DealPlayer(player3Id, initialChips, lostChips2Amount),
            DealPlayer(player4Id, initialChips, maxBetChipsAmount),
            DealPlayer(player5Id, initialChips),
        )

    private fun getDealPlayersWithAllPlayersShowdown(): List<DealPlayer> =
        listOf(
            DealPlayer(player1Id, initialChips, maxBetChipsAmount),
            DealPlayer(player2Id, initialChips, maxBetChipsAmount),
            DealPlayer(player3Id, initialChips, maxBetChipsAmount),
            DealPlayer(player4Id, initialChips, maxBetChipsAmount),
            DealPlayer(player5Id, initialChips, maxBetChipsAmount),
        )

    private fun getDealPlayersWithAllPlayersWon(): List<DealPlayer> =
        listOf(
            DealPlayer(player1Id, smallInitialChips, smallInitialChips),
            DealPlayer(player2Id, initialChips, almostSmallInitialChips),
            DealPlayer(player3Id, anotherInitialChips, almostSmallInitialChips),
            DealPlayer(player4Id, initialChips),
            DealPlayer(player5Id, anotherInitialChips2),
        )


    private fun getDealPlayersWithAllPlayersShowdownWithSomeAllIn(): List<DealPlayer> =
        listOf(
            DealPlayer(player1Id, smallInitialChips, smallInitialChips),
            DealPlayer(player2Id, initialChips, initialChips),
            DealPlayer(player3Id, anotherInitialChips, anotherInitialChips),
            DealPlayer(player4Id, initialChips, initialChips),
            DealPlayer(player5Id, anotherInitialChips2, anotherInitialChips2),
        )

    private fun getDealPlayersWithAllInPlayerAndOneMoreBetAndSomeLessBets(): List<DealPlayer> =
        listOf(
            DealPlayer(player1Id, smallInitialChips, smallInitialChips),
            DealPlayer(player2Id, initialChips, maxBetChipsAmount * 2),
            DealPlayer(player3Id, initialChips, lostChips2Amount),
            DealPlayer(player4Id, initialChips, lostChips2Amount + smallInitialChips),
            DealPlayer(player5Id, initialChips),
        )

    private fun getDealPlayersWithTwoDifferentAllInsAndOneMaxBet() = listOf(
        DealPlayer(player1Id, smallInitialChips, smallInitialChips),
        DealPlayer(player2Id, maxBetChipsAmount, maxBetChipsAmount),
        DealPlayer(player3Id, initialChips, 2 * maxBetChipsAmount),
        DealPlayer(player4Id, initialChips),
        DealPlayer(player5Id, initialChips),
    )

    private fun getCardDistributionWithThreeWinners(): CardsDistribution {
        val firstPlayerCard1 = heartsFive
        val firstPlayerCard2 = heartsThree
        val secondPlayerCard1 = diamondsFive
        val secondPlayerCard2 = clubsTwo
        val thirdPlayerCard1 = diamondsKing
        val thirdPlayerCard2 = spadesEight
        val fourthPlayerCard1 = spadesFive
        val fourthPlayerCard2 = spadesTwo
        val fifthPlayerCard1 = clubsKing
        val fifthPlayerCard2 = clubsNine
        val burn1 = spadesKing
        val flopCards = listOf(clubsFive, clubsFour, heartsSeven)
        val burn2 = spadesAce
        val turn = spadesQueen
        val burn3 = diamondsAce
        val river = clubsQueen
        return CardsDistribution.createCardsDistribution(
            listOf(firstPlayerCard1) + secondPlayerCard1 + thirdPlayerCard1 + fourthPlayerCard1 + fifthPlayerCard1 + firstPlayerCard2 + secondPlayerCard2 + thirdPlayerCard2 + fourthPlayerCard2 + fifthPlayerCard2 + burn1 + flopCards + burn2 + turn + burn3 + river,
            playersIds
        )
    }

    private fun getCardDistributionWithBestHandsPlayer3Player2Player1Player4Player5(): CardsDistribution {
        val firstPlayerCard1 = diamondsQueen
        val firstPlayerCard2 = heartsThree
        val secondPlayerCard1 = spadesFour
        val secondPlayerCard2 = heartsFour
        val thirdPlayerCard1 = diamondsSeven
        val thirdPlayerCard2 = spadesSeven
        val fourthPlayerCard1 = spadesFive
        val fourthPlayerCard2 = diamondsKing
        val fifthPlayerCard1 = heartsKing
        val fifthPlayerCard2 = clubsJack
        val burn1 = spadesKing
        val flopCards = listOf(clubsFive, clubsFour, heartsSeven)
        val burn2 = spadesAce
        val turn = spadesQueen
        val burn3 = diamondsAce
        val river = clubsQueen
        return CardsDistribution.createCardsDistribution(
            listOf(firstPlayerCard1) + secondPlayerCard1 + thirdPlayerCard1 + fourthPlayerCard1 + fifthPlayerCard1 + firstPlayerCard2 + secondPlayerCard2 + thirdPlayerCard2 + fourthPlayerCard2 + fifthPlayerCard2 + burn1 + flopCards + burn2 + turn + burn3 + river,
            playersIds
        )
    }

    private fun getCardDistributionWithBestHandOnTable(): CardsDistribution {
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
        val burn1 = clubsFour
        val flopCards = listOf(spadesAce, spadesKing, spadesTen)
        val burn2 = diamondsQueen
        val turn = spadesQueen
        val burn3 = diamondsAce
        val river = spadesJack
        return CardsDistribution.createCardsDistribution(
            listOf(firstPlayerCard1) + secondPlayerCard1 + thirdPlayerCard1 + fourthPlayerCard1 + fifthPlayerCard1 + firstPlayerCard2 + secondPlayerCard2 + thirdPlayerCard2 + fourthPlayerCard2 + fifthPlayerCard2 + burn1 + flopCards + burn2 + turn + burn3 + river,
            playersIds
        )
    }
}
