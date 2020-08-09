package io.github.nadzwyczajnaGrupaRobocza.texaspoker.game.cards

fun createPocketCards(
    pocketCard1: Card,
    pocketCard2: Card
) = PocketCards(pocketCard1, pocketCard2)

fun createRiver(
    flop1: Card,
    flop2: Card,
    flop3: Card,
    turnCard: Card,
    riverCard: Card
) =
    RiverCommunityCards(
        TurnCommunityCards(
            FlopCommunityCards(
                NoCommunityCards(),
                flop1,
                flop2,
                flop3
            ), turnCard
        ), riverCard
    )

fun createHand(
    card1: Card,
    card2: Card,
    card3: Card,
    card4: Card,
    card5: Card,
    card6: Card,
    card7: Card
) = Hand(createRiver(card1, card2, card3, card4, card5), createPocketCards(card6, card7))
