package com.ideas.uno;

import com.ideas.uno.exception.UNOException;
import com.ideas.uno.utility.DeckUtility;

import java.util.*;

/**
 * @Author TAPANM
 */
public class UNOGame {
    final private static int NUMCARDSHAND = 7;
    private DeckMain deckMain;
    private Resource drawPile;
    private Resource discardPile;
    private List<Resource> players;
    private boolean isManual;
    private Map<String, Integer> scoreCard;
    private Integer winningPoint;

    public UNOGame() {
        initVariables();
    }

    public static void main(String[] args) {
        UNOGame myGame = new UNOGame();
        try {
            myGame.playGame();
        } catch (UNOException e) {
            e.printStackTrace();
        }
    }

    public void playGame() throws UNOException {
        Collections.shuffle(deckMain.getDeck());
        registerPlayer();
        distributeCards();
        // Reshuffling in case first card is wild card
        while (isWildCard(drawPile.getCards().get(drawPile.getCards().size() - 1))) {
            System.out.println("The card at the top of the discard pile is wild card :" + drawPile.getTopCard().toString());
            System.out.println("Reshuffling...");
            Collections.shuffle(drawPile.getCards());
        }
        discardPile.addCard(drawPile.getTopCard());
        drawPile.removeCard(1);
        System.out.println("The card at the top of the discard pile is " + discardPile.getTopCard());
        //Its time to play for the first player
        playTurn(0, null);
    }

    private boolean isWildCard(Card card) {
        return card.getCardValue().equals(CardValue.WILD) || card.getCardValue().equals(CardValue.WILD_DRAW_FOUR);
    }

    private void registerPlayer() throws UNOException {
        Scanner stdin = new Scanner(System.in);
        print("Winning Point :\n");
        winningPoint = Integer.parseInt(stdin.nextLine());
        print("Enter The number of Player Want to Play :\n");
        String input = stdin.nextLine();
        Integer numberOfPlayer = Integer.parseInt(input);
        if (numberOfPlayer < 11 && numberOfPlayer > 1) {
            String tempName;
            String tempAge;
            for (int i = 0; i < numberOfPlayer; i++) {
                print("Enter Player " + (i + 1) + " Name:\n");
                tempName = stdin.nextLine();
                print("Enter Player " + (i + 1) + " Age:\n");
                tempAge = stdin.nextLine();
                if (Integer.parseInt(tempAge) < 7) {
                    print("Below 7 years Players can not play");
                    continue;
                }
                players.add(new Resource(tempName, null));
                scoreCard.put(tempName, 0);
            }
            if (!(players.size() > 1)) {
                throw new UNOException("Not enuf players");
            }
        } else {
            print("Only 2-10 players can play this game.. Try Again");
            registerPlayer();
        }
    }

    private void distributeCards() {
        print("Distributing card to players...");
        for (int i = 0; i < NUMCARDSHAND; i++) {
            for (Resource resource : players) {
                resource.addCard(deckMain.getDeck().remove(deckMain.getDeck().size() - 1));
            }
        }
        drawPile.addCard(deckMain.getDeck());
        print("Distribution done ...");
    }

    // Plays one turn for the player number indicated.
    private void playTurn(int playerIndex, Color color) throws UNOException {
        if (color != null)
            print("Color is changed to ", color.toString());
        if (playerIndex > (players.size() - 1)) {
            playerIndex = playerIndex - players.size();
        }
        if (isManual) {

        } else {
            playAutomatic(playerIndex, color);
        }
    }

    private void playAutomatic(int playerIndex, Color color) throws UNOException {
        Resource resource = players.get(playerIndex);
        print(players.get(playerIndex).getName(), " Will play now");
        print(players.get(playerIndex).toString());
        Card cardPlayed = drawProperMatchinCard(players.get(playerIndex), color);
        if (cardPlayed != null) {
            print(cardPlayed.toString(), " Played by ", resource.getName());
            print("----------------------------------------------");
            if (resource.getCards().size() == 0) {
                declareMatchWinner(resource);
                return;
            }else if(resource.getCards().size()==1){
                print("Player ",resource.getName(), " said UNO !!!");
            }
            discardPile.addCard(cardPlayed);
            playNextPlayer(cardPlayed, playerIndex);

        } else {
            resource.addCard(drawPile.getTopCard());//Player will draw card from deck if no match found
            print(resource.getName(), " will draw one card");
            print("-----------------------------------------------");
            removeFromDrawPile(1);
            playNextPlayer(null, playerIndex);
        }
    }

    /**
     * If the draw pile is empty or contains less number of cards than what expected then
     * 1. Remove what is already present
     * 2. All the cards from discard pile should go to draw pile
     * 3. After that draw pile should be shuffled and
     * 4. Remove the pending number of cards from the draw pile
     * ****From the discard pile top card should be left untouched.
     *
     * @param i
     * @throws UNOException
     */
    private void removeFromDrawPile(int i) throws UNOException {
        if (drawPile.getCards().size() > i) {
            drawPile.removeCard(i);
        } else {
            int pendingNumberOfCardToDraw = i - drawPile.getCards().size();
            drawPile.removeCard(drawPile.getCards().size());
            Card lastCard = discardPile.removeCard(discardPile.getTopCard());
            drawPile.addCard(discardPile.getCards());
            discardPile.removeCard(discardPile.getCards().size());
            discardPile.addCard(lastCard);
            Collections.shuffle(drawPile.getCards());
            drawPile.removeCard(pendingNumberOfCardToDraw);
        }
    }

    private void playNextPlayer(Card cardDrawn, int playerIndex) throws UNOException {
        if (cardDrawn != null) {
            if (cardDrawn.getCardValue().equals(CardValue.SKIP)) {
                playTurn(playerIndex + 2, null);
            } else if (cardDrawn.getCardValue().equals(CardValue.REVERSE)) {
                Collections.reverse(players);
                playTurn(players.size() - playerIndex, null);
            } else if (cardDrawn.getCardValue().equals(CardValue.DRAW_TWO) || cardDrawn.getCardValue().equals(CardValue.WILD_DRAW_FOUR)) {
                int nextPlayerIndex = (playerIndex + 1 > players.size() - 1) ? (players.size() - playerIndex - 1) : playerIndex + 1;
                if (drawPile.getTopCard() != null) {
                    players.get(nextPlayerIndex).addCard(drawPile.getTopCard());
                    drawPile.removeCard(drawPile.getTopCard());
                    players.get(nextPlayerIndex).addCard(drawPile.getTopCard());
                    drawPile.removeCard(drawPile.getTopCard());
                    if (cardDrawn.getCardValue().equals(CardValue.WILD_DRAW_FOUR)) {//Incase of Wild Draw Four 2 extra cards need to be withdrawn
                        players.get(nextPlayerIndex).addCard(drawPile.getTopCard());
                        drawPile.removeCard(drawPile.getTopCard());
                        players.get(nextPlayerIndex).addCard(drawPile.getTopCard());
                        drawPile.removeCard(drawPile.getTopCard());
                        playTurn(nextPlayerIndex + 1, chooseColor(players.get(playerIndex)));

                    } else {
                        playTurn(nextPlayerIndex + 1, null);
                    }
                } else {
                    throw new UNOException("draw pile is over");
                }

            } else if (cardDrawn.getCardValue().equals(CardValue.WILD)) {
                playTurn(playerIndex + 1, chooseColor(players.get(playerIndex)));
            } else {
                playTurn(playerIndex + 1, null);//Next player turn
            }
        } else {
            playTurn(playerIndex + 1, null);
        }
    }

    private Color chooseColor(Resource resource) {
        Color maxCardColor = Color.BLUE;
        int blueColorIndex = 0;
        if (!DeckUtility.isEmpty(resource.getColorCardMap().get(maxCardColor))) {
            blueColorIndex = resource.getColorCardMap().get(maxCardColor).size();
        }
        for (Color color : resource.getColorCardMap().keySet()) {
            if (!DeckUtility.isEmpty(resource.getColorCardMap().get(color)) && resource.getColorCardMap().get(color).size() > blueColorIndex) {
                maxCardColor = color;
            }
        }
        return maxCardColor;
    }

    /**
     * Declaring the winner of the match
     *
     * @param resource
     */
    private void declareMatchWinner(Resource resource) {
        print(resource.getName(), "is winner of the match");
        scoreCard.put(resource.getName(), scoreCard.get(resource.getName()) + totalPointsEarned(players));
        if (scoreCard.get(resource.getName()) >= winningPoint) {
            declareWinner(resource);
        }
    }

    /**
     * Declaring the winner of the Game
     *
     * @param resource
     */
    private void declareWinner(Resource resource) {
        print(resource.getName(), " won the game!!! Congratulation");
    }

    /**
     * Points calculation function as per the rule
     *
     * @param players
     * @return
     */
    private Integer totalPointsEarned(List<Resource> players) {
        Integer total = 0;
        for (Resource resource : players) {
            Integer indTotal;
            indTotal = resource.getWildCard().size() * 50;
            for (Card card : resource.getCards()) {
                switch (card.getCardValue()) {
                    case ZERO:
                        indTotal = indTotal;
                        break;
                    case ONE:
                        indTotal = indTotal + 1;
                        break;
                    case TWO:
                        indTotal = indTotal + 2;
                        break;
                    case THREE:
                        indTotal = indTotal + 3;
                        break;
                    case FOUR:
                        indTotal = indTotal + 4;
                        break;
                    case FIVE:
                        indTotal = indTotal + 5;
                        break;
                    case SIX:
                        indTotal = indTotal + 6;
                        break;
                    case SEVEN:
                        indTotal = indTotal + 7;
                        break;
                    case EIGHT:
                        indTotal = indTotal + 8;
                        break;
                    case NINE:
                        indTotal = indTotal + 9;
                        break;
                    case SKIP:
                        indTotal = indTotal + 20;
                        break;
                    case REVERSE:
                        indTotal = indTotal + 20;
                        break;
                    case DRAW_TWO:
                        indTotal = indTotal + 20;
                        break;
                }
            }
            total = total + indTotal;
        }
        return total;
    }

    /**
     * This method will draw the proper card from the players hand
     * card is proper only if
     * its matches the value
     * its matches the color
     * its a Action card
     * <p/>
     * Now this will implementation will follow a pattern
     * value>color>skip>reverse>wild>draw Two>Wild Draw 4>null
     *
     * @param resource
     * @param color
     * @return
     */
    private Card drawProperMatchinCard(Resource resource, Color color) {
        CardValue topDiscardedCardValue = discardPile.getTopCard().getCardValue();
        Color topDiscardedColor = discardPile.getTopCard().getColor();
        if (color == null) {
            if (!DeckUtility.isEmpty(resource.getCardValueCardMap().get(topDiscardedCardValue))) {
                return resource.removeCard(resource.getCardValueCardMap().get(topDiscardedCardValue).get(0));
            } else if (!DeckUtility.isEmpty(resource.getColorCardMap().get(topDiscardedColor))) {
                return resource.removeCard(resource.getColorCardMap().get(topDiscardedColor).get(0));
            } else if (!DeckUtility.isEmpty(resource.getWildCard())) {
                return resource.removeCard(resource.getWildCard().get(0));
            } else {
                return null;
            }
        } else {
            if (!DeckUtility.isEmpty(resource.getColorCardMap().get(color)))
                return resource.getColorCardMap().get(color).get(0);
            else
                return null;
        }

    }

    private void initVariables() {
        deckMain = new DeckMain();
        players = new ArrayList<Resource>();
        isManual = false;
        drawPile = new Resource(Type.DRAW_PILE.toString(), null);
        discardPile = new Resource(Type.DISCARD_PILE.toString(), null);
        scoreCard = new HashMap<String, Integer>();
    }

    /**
     * Small function for printStatement
     *
     * @param strings
     */
    private void print(String... strings) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (String str : strings) {
            stringBuilder.append(str);
        }
        System.out.println(stringBuilder);
    }
}
