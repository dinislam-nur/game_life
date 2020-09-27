package ru.innopolis.student.dinislam.game.impl;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * Класс рисует площадку в консоль.
 */
@RequiredArgsConstructor
public class Court {

    /**
     * Длина площадки.
     */
    private final int size;

    /**
     * Рисует площадку в консоль.
     *
     * @param cells - список живых клеток.
     */
    public void draw(Map<Integer, Set<Integer>> cells) {
        char[][] court = new char[size][size];

        fillCourt(court, cells);

        for (char[] ints : court) {
            for (char anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.print(System.lineSeparator());
        }
        System.out.println(System.lineSeparator());
    }

    /**
     * Заполняет площадку строковыми элементами:
     * '*' - пустая клетка; 'O' - живая клетка.
     *
     * @param court - площадка.
     * @param cells - список живых клеток.
     */
    public static void fillCourt(char[][] court, Map<Integer, Set<Integer>> cells) {

        final int length = court.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                court[i][j] = '*';
            }
        }
        cells.forEach(
                (x, yAxis) -> yAxis.forEach(
                        y -> court[length - 1 - y][x] = 'O'));
    }
}
