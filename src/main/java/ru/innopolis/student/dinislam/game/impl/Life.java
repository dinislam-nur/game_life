package ru.innopolis.student.dinislam.game.impl;

import lombok.RequiredArgsConstructor;
import ru.innopolis.student.dinislam.game.api.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Реализация игры жизнь.
 */
@RequiredArgsConstructor
public class Life implements Game {

    /**
     * Метка переключения из однопоточной версии к многопоточной, сравниваемая с
     * размером Map.
     */
    private static final int LABEL = 50;

    /**
     * Стартовый список живых клеток.
     */
    private final Map<Integer, Set<Integer>> initCells;

    /**
     * Длина площадки.
     */
    private final int sizeCourt;

    /**
     * Количество циклов игры жизнь.
     */
    private final int numberOfCycle;

    /**
     * Запуск игры.
     * @return - выжившие клетки.
     */
    @Override
    public Map<Integer, Set<Integer>> start() {
        Map<Integer, Set<Integer>> cells = new HashMap<>(initCells);
        Map<Integer, Set<Integer>> newGeneration;

        final Court court = new Court(sizeCourt);
        court.draw(cells);
        for (int i = 0; i < numberOfCycle; i++) {
            if (cells.size() < LABEL) {
                newGeneration = new CycleOfLife(cells, sizeCourt).startCycle();
            } else {
                newGeneration = new CycleOfLife(cells, sizeCourt).startConcurrentCycle();
            }
            court.draw(newGeneration);
            if (newGeneration.isEmpty() || cells.equals(newGeneration)) {
                cells = newGeneration;
                break;
            }
            cells = newGeneration;
        }
        return cells;
    }
}
