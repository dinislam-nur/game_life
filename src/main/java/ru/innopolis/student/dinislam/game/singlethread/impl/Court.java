package ru.innopolis.student.dinislam.game.singlethread.impl;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class Court {

    private final int size;

    public void draw(Map<Integer, Set<Integer>> cells){
        char[][] court = new char[size][size];

        fillCourt(court);

        cells.forEach(
                (x, yAxis) -> yAxis.forEach(
                        y -> court[size - 1 - y][x] = 'O'));

        for (char[] ints : court) {
            for (char anInt : ints) {
                System.out.print(anInt + " ");
            }
            System.out.print(System.lineSeparator());
        }
    }

    private void fillCourt(char[][] court) {
        final int length = court.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                court[i][j] = '*';
            }

        }
    }
}
