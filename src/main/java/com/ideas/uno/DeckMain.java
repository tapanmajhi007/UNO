package com.ideas.uno;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author TAPANM
 */
public class DeckMain {

    private final List<Card> deck = new ArrayList<Card>();
/*    private static List<Card>specialCard=new ArrayList<Card>();
    private static List<Card>normalCard=new ArrayList<Card>();*/

    public DeckMain() {
        initCards();
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void initCards() {
        for (Color color : Color.values()) {
            for (int i = 0; i < 2; i++) {
                for (CardValue cardValue : CardValue.values()) {
                    if (i == 1 && cardValue.equals(CardValue.ZERO) || i == 1 && (cardValue.equals(CardValue.WILD) || cardValue.equals(CardValue.WILD_DRAW_FOUR))) {
                        continue;
                    } else if (cardValue.equals(CardValue.WILD) || cardValue.equals(CardValue.WILD_DRAW_FOUR)) {
                        deck.add(new Card(cardValue, null));
                    } else {
                        deck.add(new Card(cardValue, color));
                    }

                }
            }
        }

    }


}
