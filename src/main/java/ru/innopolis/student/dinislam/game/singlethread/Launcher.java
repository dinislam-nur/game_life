package ru.innopolis.student.dinislam.game.singlethread;


import ru.innopolis.student.dinislam.game.singlethread.api.Game;
import ru.innopolis.student.dinislam.game.singlethread.impl.Life;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Launcher {

    public static void main(String[] args) {


        Map<Integer, Set<Integer>> initConf = new HashMap<>();

        initConf.put(0, new HashSet<Integer>() {{
            add(8);
        }});
        initConf.put(1, new HashSet<Integer>() {{
            add(7);
        }});
        initConf.put(2, new HashSet<Integer>() {{
            add(7);
            add(8);
            add(9);
        }});

        final int sizeCourt = 10;
        final int numberOfCycle = 1000;
        final Game life = new Life(initConf, sizeCourt, numberOfCycle);
        life.start();
    }
}
