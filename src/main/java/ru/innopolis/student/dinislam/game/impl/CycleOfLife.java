package ru.innopolis.student.dinislam.game.impl;

import lombok.RequiredArgsConstructor;
import ru.innopolis.student.dinislam.game.api.Cycle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Реализация цикла игры "Жизнь".
 */
@RequiredArgsConstructor
public class CycleOfLife implements Cycle {

    /**
     * Начальный список живых клеток.
     */
    private final Map<Integer, Set<Integer>> oldGeneration;

    /**
     * Длина площадки.
     */
    private final int sizeCourt;

    /**
     * Список выживших клеток за цикл.
     */
    private Map<Integer, Set<Integer>> newGeneration;


    /**
     * Запуск цикла жизни в однопоточной реализации.
     *
     * @return - выжившие клетки.
     */
    public Map<Integer, Set<Integer>> startCycle() {

        newGeneration = new HashMap<>();

        oldGeneration.forEach(
                (coordinateX, value) -> value.forEach(
                        coordinateY -> processCell(coordinateX, coordinateY, true)));

        return newGeneration;
    }

    /**
     * Запуск цикла жизни в многопоточной реализации.
     *
     * @return - выжившие клетки.
     */
    public Map<Integer, Set<Integer>> startConcurrentCycle() {
        newGeneration = new ConcurrentHashMap<>();

        try {
            final ExecutorService service = Executors.newFixedThreadPool(4);
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

    /**
     * Обработка клетки площадки. Если есть 2-3 живых клеток рядом с живой клеткой, клетка продлжает жить.
     * Если рядом с пустой клеткой 3 живых, в этой клетке зарождается жизнь.
     *
     * @param coordinateX - координата x клетки.
     * @param coordinateY - координата y клетки.
     * @param isAlive     - состояние клетки, true - живая клетка, false - мертвая клетка.
     */
    private void processCell(int coordinateX, int coordinateY, boolean isAlive) {
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

    /**
     * Анализирует 8 соседних клеток с исходной.
     *
     * @param coordinateX - координата x клетки.
     * @param coordinateY - координата y клетки.
     * @param isAlive     - состояние клетки, true - живая клетка, false - мертвая клетка.
     * @return - true - если в клетке будет жизнь, false - будет мертвой клеткой.
     */
    private boolean willBeAlive(int coordinateX, int coordinateY, boolean isAlive) {
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
