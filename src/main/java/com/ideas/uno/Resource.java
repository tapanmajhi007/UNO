package com.ideas.uno;

import com.ideas.uno.exception.UNOException;
import com.ideas.uno.utility.DeckUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Resource can be of three types
 * 1. Player
 * 2. Discard Pile
 * 3. Draw Pile
 *
 * @Author TAPANM
 */
public final class Resource {
    private final String name;
    private final List<Card> cards;
    private Map<CardValue, List<Card>> cardValueCardMap = new HashMap<CardValue, List<Card>>();
    private Map<Color, List<Card>> colorCardMap = new HashMap<Color, List<Card>>();
    private List<Card> wildCard = new ArrayList<Card>();

    public Resource(String name, List<Card> cards) {
        this.name = name;
        if (cards != null) {
            this.cards = cards;
            addToGroupCards(this.cards);
        } else {
            this.cards = new ArrayList<Card>();
        }
    }

    public String getName() {
        return name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        this.cards.add(card);
        addToGroupCards(new ArrayList<Card>(asList(card)));
    }

    public void addCard(List<Card> cards) {
        this.cards.addAll(cards);
        addToGroupCards(cards);
    }

    public Card removeCard(Card card) {
        this.cards.remove(card);
        removeFromGroupCards(new ArrayList<Card>(asList(card)));
        return card;
    }

    public void removeCard(List<Card> cards) {
        this.cards.removeAll(cards);
        removeFromGroupCards(cards);
    }

    /**
     * this method will remove the number of cards from the resource hand
     * if the resource contains less than expected, exception will be thrown
     *
     * @param number
     * @throws UNOException
     */
    public void removeCard(int number) throws UNOException {
        if (this.cards.size() < number) {
            throw new UNOException("Card have less number of cards than expected");
        } else {
            for (int i = 0; i < number; i++) {
                this.cards.remove(this.cards.size() - 1);
            }
        }
    }

    /**
     * remove cards from all group map
     *
     * @param cards
     */
    private void removeFromGroupCards(List<Card> cards) {
        for (Card card : cards) {
            if (card.getCardValue().equals(CardValue.WILD) || card.getCardValue().equals(CardValue.WILD_DRAW_FOUR)) {
                wildCard.remove(card);
            } else {
                DeckUtility.removeToMapOfList(cardValueCardMap, card.getCardValue(), card);
                DeckUtility.removeToMapOfList(colorCardMap, card.getColor(), card);
            }
        }
    }

    /**
     * add cards to the group card to maintain the map
     *
     * @param cards
     */
    private void addToGroupCards(List<Card> cards) {
        if(cards!=null){
            for (Card card : cards) {
                System.out.println(card.toString());
                if (card.getCardValue().equals(CardValue.WILD) || card.getCardValue().equals(CardValue.WILD_DRAW_FOUR)) {
                    wildCard.add(card);
                } else {
                    DeckUtility.addToMapOfList(cardValueCardMap, card.getCardValue(), card);
                    DeckUtility.addToMapOfList(colorCardMap, card.getColor(), card);
                }
            }
        }
    }

    public Card getTopCard() {
        if (this.cards.size() > 0) {
            return this.cards.get(this.cards.size() - 1);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Resource{" +
                "_name='" + name + '\'' +
                ": cards=" + printCard(cards) +
                '}';
    }

    private String printCard(List<Card> cards) {
        StringBuilder toStr = new StringBuilder("");
        if (cards != null && cards.size() > 0) {
            for (Card card : cards) {
                toStr = toStr.append(card.toString()).append("|");
            }
        }
        return toStr.toString();
    }

    public Map<CardValue, List<Card>> getCardValueCardMap() {
        return cardValueCardMap;
    }

    public Map<Color, List<Card>> getColorCardMap() {
        return colorCardMap;
    }

    public List<Card> getWildCard() {
        return wildCard;
    }
}
