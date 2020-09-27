package ru.innopolis.student.dinislam.game.api;

import java.util.Map;
import java.util.Set;

/**
 * Игра "жизнь".
 */
public interface Game {

    /**
     * Запускает игру.
     *
     * @return - выжившие клетки.
     */
    Map<Integer, Set<Integer>> start();
}
