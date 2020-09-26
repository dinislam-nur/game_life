package ru.innopolis.student.dinislam.game.impl;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class CycleOfLife {

    private final Map<Integer, Set<Integer>> oldGeneration;

    private final int sizeCourt;

    private Map<Integer, Set<Integer>> newGeneration;


    public Map<Integer, Set<Integer>> startCycle() {

        newGeneration = new HashMap<>();

        oldGeneration.forEach(
                (coordinateX, value) -> value.forEach(
                        coordinateY -> processCell(coordinateX, coordinateY, true)));

        return newGeneration;
    }

    public Map<Integer, Set<Integer>> startConcurrentCycle() {
        newGeneration = new ConcurrentHashMap<>();

        try {
            final ExecutorService service = Executors.newFixedThreadPool(8);
            final CountDownLatch latch = new CountDownLatch(oldGeneration.size());

            oldGeneration.forEach(
                    (coordinateX, value) ->
                            service.execute(() -> {
                                value.forEach(
                                        coordinateY -> processCell(coordinateX, coordinateY, true));
                                latch.countDown();
                            }));

            latch.await();
            service.shutdown();
        } catch (InterruptedException e) {
            System.out.println("Thread is interrupted");
            e.printStackTrace();
        }

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
