package com.ideas.uno;

import com.ideas.uno.exception.UNOException;

/**
 * @Author TAPANM
 */
public class PlayUNOGame {

    public static void main(String[] args) throws UNOException {
        UNOGame myGame = new UNOGame();
        try {
            myGame.playGame();
        } catch (UNOException e) {
            e.printStackTrace();
        }
    }
}
