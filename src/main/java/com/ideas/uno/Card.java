package com.ideas.uno;

/**
 * Card entity which can be access only when its a part of either deck ,player hand,or discard pile
 *
 * @Author TAPANM
 */
public class Card {

    private final CardValue cardValue;
    private Color color;

    public Card(CardValue cardValue, Color color) {
        this.cardValue = cardValue;
        this.color = color;
    }

    public String toString() {
        if (cardValue.equals(CardValue.WILD) || cardValue.equals(CardValue.WILD_DRAW_FOUR)) {
            return cardValue.name();
        } else {
            return cardValue.name() + "-" + color.name();
        }
    }

    CardValue getCardValue() {
        return cardValue;
    }

    Color getColor() {
        return color;
    }
}
