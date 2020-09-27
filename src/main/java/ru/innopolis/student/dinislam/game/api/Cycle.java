package ru.innopolis.student.dinislam.game.api;

import java.util.Map;
import java.util.Set;

/**
 * Интерфейс выполняющий один цикл игры "Жизнь".
 */
public interface Cycle {

    /**
     * Запускает цикл жизни в однопоточной реализации.
     *
     * @return - выжившие клетки.
     */
    Map<Integer, Set<Integer>> startCycle();

    /**
     * Запускает цикл жизни в многопоточной реализации.
     *
     * @return - выжившие клетки.
     */
    Map<Integer, Set<Integer>> startConcurrentCycle();
}
