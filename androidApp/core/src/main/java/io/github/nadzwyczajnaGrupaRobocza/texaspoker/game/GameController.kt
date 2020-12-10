package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game

import io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards.DealWithCards

interface GameView {
    fun gameStarted(players: Players)
    fun gameFinished(winner: Player)
    fun dealStarted(deal: DealWithCards)
    fun moved(move: DealMove, moveResult: DealMoveResult)
    fun dealFinished(winner: DealResult)
    fun flop()
    fun turn()
    fun river()
}

class GameController private constructor(
    private val configuration: GameConfiguration,
    private val players: Players,
    private val view: GameView,
) {
    private val game = Game(players.values.toList(), configuration)

    init {
        runGame()
    }

    companion object {
        fun createGame(configuration: GameConfiguration, players: Players, view: GameView) =
            GameController(configuration, players, view)
    }

    private fun runGame() {
        view.gameStarted(players)

        dealUntilOnePlayerLeft()
    }

    private fun dealUntilOnePlayerLeft() {
        if (game.activePlayers.size == 1) {
            view.gameFinished(game.activePlayers.first())
            return
        }
        runDeal()
        dealUntilOnePlayerLeft()
    }

    private fun runDeal() {
        val deal = game.deal()
        view.dealStarted(deal)
        val winner = preFlop(deal)
        winner.final?.let {
            view.dealFinished(DealResultCalculateProcedure(deal.deal.players()).dealResult(it))
            return
        }
        continueWithFlop(deal)
    }

    private fun continueWithFlop(deal: DealWithCards) {
        view.flop()
        val winner = flop(deal)
        winner.final?.let {
            view.dealFinished(DealResultCalculateProcedure(deal.deal.players()).dealResult(it))
            return
        }
        continueWithTurn(deal)
    }

    private fun continueWithTurn(deal: DealWithCards) {
        view.turn()
        val winner = turn(deal)
        winner.final?.let {
            view.dealFinished(DealResultCalculateProcedure(deal.deal.players()).dealResult(it))
            return
        }
        continueWithRiver(deal)
    }

    private fun continueWithRiver(deal: DealWithCards) {
        view.river()
        val winner = river(deal)
        winner.final?.let {
            view.dealFinished(DealResultCalculateProcedure(deal.deal.players()).dealResult(it))
            return
        }
        winner.nextRound?.let {
            view.dealFinished(
                DealResultCalculateProcedure(deal.deal.players()).dealResult(
                    it,
                    deal.cardsDistribution
                )
            )
        }
    }

    private fun preFlop(deal: DealWithCards) = betUntilWinnerOrNextRound(deal)
    private fun flop(deal: DealWithCards) = betUntilWinnerOrNextRound(deal)
    private fun turn(deal: DealWithCards) = betUntilWinnerOrNextRound(deal)
    private fun river(deal: DealWithCards) = betUntilWinnerOrNextRound(deal)

    private fun betUntilWinnerOrNextRound(deal: DealWithCards): DealMoveResult {
        val nextPlayer = players[deal.deal.nextBetter()]
        if (nextPlayer != null) {
            val move = nextPlayer.move()
            val moveResult = deal.deal.move(move)
            view.moved(move, moveResult)
            return when {
                moveResult.final != null -> moveResult
                moveResult.nextRound != null -> moveResult
                else -> betUntilWinnerOrNextRound(deal)
            }
        }
        throw InvalidGameState()
    }
}

class InvalidGameState : Throwable()
