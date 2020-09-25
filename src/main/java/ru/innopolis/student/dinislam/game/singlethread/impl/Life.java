package ru.innopolis.student.dinislam.game.singlethread.impl;

import lombok.RequiredArgsConstructor;
import ru.innopolis.student.dinislam.game.singlethread.api.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class Life implements Game {

    private final Map<Integer, Set<Integer>> initCells;
    private final int sizeCourt;
    private final int numberOfCycle;

    @Override
    public void start() {
        Map<Integer, Set<Integer>> cells = new HashMap<>(initCells);
        Map<Integer, Set<Integer>> newGeneration;

        final Court court = new Court(sizeCourt);
        court.draw(cells);
        System.out.println(System.lineSeparator());
        for (int i = 0; i < numberOfCycle; i++) {
            newGeneration = new CycleOfLife(cells, sizeCourt).startCycle();
            if (cells.isEmpty() || cells.equals(newGeneration)) {
                break;
            }
            court.draw(newGeneration);
            System.out.println(System.lineSeparator());
            cells = newGeneration;
        }
    }
}
