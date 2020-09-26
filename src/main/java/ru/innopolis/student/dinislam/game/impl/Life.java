package ru.innopolis.student.dinislam.game.impl;

import lombok.RequiredArgsConstructor;
import ru.innopolis.student.dinislam.game.api.Game;

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
        for (int i = 0; i < numberOfCycle; i++) {
            if (cells.size() < 50) {
                newGeneration = new CycleOfLife(cells, sizeCourt).startCycle();
            } else {
                newGeneration = new CycleOfLife(cells, sizeCourt).startConcurrentCycle();
            }
            court.draw(newGeneration);
            if (newGeneration.isEmpty() || cells.equals(newGeneration)) {
                break;
            }
            cells = newGeneration;
        }
    }
}
