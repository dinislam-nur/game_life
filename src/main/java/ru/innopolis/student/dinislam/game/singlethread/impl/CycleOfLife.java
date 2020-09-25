package ru.innopolis.student.dinislam.game.singlethread.impl;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class CycleOfLife {

    private final Map<Integer, Set<Integer>> oldGeneration;

    private final int sizeCourt;

    private final Map<Integer, Set<Integer>> newGeneration = new HashMap<>();


    public Map<Integer, Set<Integer>> startCycle() {
        oldGeneration.forEach(
                (coordinateX, value) -> value.forEach(
                        coordinateY -> processCell(coordinateX, coordinateY, true)));

        return newGeneration;
    }

    private void processCell(int coordinateX ,int coordinateY, boolean isAlive) {
        if (willBeAlive(coordinateX, coordinateY, isAlive)) {
            newGeneration.compute(coordinateX, (key, value) -> {
                if (coordinateX >= 0 && coordinateX < sizeCourt
                    && coordinateY > -1 && coordinateY < sizeCourt) {
                    value = (value == null) ? new HashSet<>() : value;
                    value.add(coordinateY);
                }
                return value;
            });
        }
    }

    private boolean willBeAlive(int coordinateX , int coordinateY , boolean isAlive) {
        int countNeighbours = 0;
        for (int i = coordinateX - 1; i <= coordinateX + 1; i++) {
            for (int j = coordinateY - 1; j <= coordinateY + 1; j++) {
                if (!(i == coordinateX && j == coordinateY)) {
                    final Set<Integer> yAxis = oldGeneration.get(i);
                    if (yAxis != null) {
                        if (yAxis.contains(j)) {
                            countNeighbours++;
                        } else if (isAlive) {
                            processCell(i, j, false);
                        }
                    } else if (isAlive) {
                        processCell(i, j, false);
                    }
                }
            }
        }
        return (isAlive && countNeighbours == 2) || countNeighbours == 3;
    }
}
