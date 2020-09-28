package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.DealWithCards
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Deck
import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.Decks

open class DealTestData {
    internal val player1Id = PlayerId("1")
    internal val player2Id = PlayerId("2")
    internal val player3Id = PlayerId("3")
    internal val player4Id = PlayerId("4")
    internal val player5Id = PlayerId("5")

    internal val player1 = DealPlayer(player1Id, 1000)
    internal val player2 = DealPlayer(player2Id, 5000)
    internal val player3 = DealPlayer(player3Id, 500)
    internal val player4 = DealPlayer(player4Id, 100)
    internal val player5 = DealPlayer(player5Id, 1000)

    internal val smallBlind = 10
    internal val bigBlind = 25
    internal val blindDiff = bigBlind - smallBlind
    internal val blinds = Blinds(smallBlind, bigBlind)

}

open class FivePlayersDealTestData : DealTestData() {
    internal val fivePlayerDeal = Deal(listOf(player1, player2, player3, player4, player5), blinds)
    internal fun dealWithCards(deck: Deck = Decks.shuffledDeck()) =
        DealWithCards(fivePlayerDeal, deck)
}

open class TwoPlayersDealTestData : DealTestData() {
    internal val twoPlayersDeal = Deal(listOf(player1, player2), blinds)
    internal fun dealWithCards(deck: Deck = Decks.shuffledDeck()) =
        DealWithCards(twoPlayersDeal, deck)
}
