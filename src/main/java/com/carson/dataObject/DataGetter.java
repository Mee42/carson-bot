package com.carson.dataObject;

import com.carson.commands.main.tac.RunningTacGame;

import java.util.ArrayList;
import java.util.List;

public class DataGetter {
    private static DataGetter ourInstance = new DataGetter();

    public static DataGetter getInstance() {
        return ourInstance;
    }

    private DataGetter() {
    }

    private List<Long> easter = new ArrayList<>();
    private List<RunningTacGame> games = new ArrayList<>();

    /**
     * MAY RETURN NULL
     */
    public RunningTacGame getGameWithUser(long id) {//NEEDS TO CHECK FOR NULL WHEN CALLED
        for(RunningTacGame game : games) {
            if(game.getIdP1() == id || game.getIdP2() == id) {
                return game;
            }
        }
        return null;
    }



    /**
     * MAY RETURN NULL
     */
    public RunningTacGame getGameWithID(long id) {//NEEDS TO CHECK FOR NULL WHEN CALLED
        for(RunningTacGame game : games) {
            if(game.getGameId() == id) {
                return game;
            }
        }
        return null;
    }

    public RunningTacGame registerGame(RunningTacGame game) {
        games.add(game);
        return game;
    }

    public boolean endGame(RunningTacGame toRemove) {
        if(games.contains(toRemove)) {
            games.remove(toRemove);
            return true;
        }
        return false;

    }

    public List<Long> getEaster() {
        return easter;
    }

    public void setEaster(List<Long> easter) {
        this.easter = easter;
    }

}
