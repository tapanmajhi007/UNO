package com.ideas.uno;

import com.ideas.uno.exception.UNOException;
import com.ideas.uno.sytem.input.stdin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * @Author TAPANM
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UNOGame.class)
public class UNOGameTest {

    @Test
    public void testPlayGame() throws Exception {
        UNOGame unoGame=   new UNOGame();
        Whitebox.setInternalState(unoGame,"winningPoint",100);
        assertEquals(100,(int)unoGame.getWinningPoint());
        List<Resource> resourceList=new ArrayList<Resource>();
        Map<String, Integer> scoreCard=new HashMap<String, Integer>();
        resourceList.add(new Resource("Tapan", null));
        resourceList.add(new Resource("Shraddha", null));
        resourceList.add(new Resource("Gaurav", null));
        scoreCard.put("Tapan",0);
        scoreCard.put("Shraddha",0);
        scoreCard.put("Gaurav",0);
        Whitebox.setInternalState(unoGame,"players",resourceList);
        Whitebox.setInternalState(unoGame,"scoreCard",scoreCard);
        unoGame.playGame();
        assertNotNull(unoGame.getWinner());
        if(unoGame.getWinner().equals("Tapan")){
            assertEquals("Tapan",unoGame.getWinner());
        }else if(unoGame.getWinner().equals("Shraddha")){
            assertEquals("Shraddha",unoGame.getWinner());
        }else if(unoGame.getWinner().equals("Gaurav")){
            assertEquals("Gaurav",unoGame.getWinner());
        }else{
            assertTrue(false);
        }
    }
}