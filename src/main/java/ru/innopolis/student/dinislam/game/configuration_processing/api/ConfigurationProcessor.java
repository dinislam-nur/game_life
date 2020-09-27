package ru.innopolis.student.dinislam.game.configuration_processing.api;

import java.io.*;
import java.util.Map;
import java.util.Set;

/**
 * Обработчик файла конфигурации стартового состояния игры "Жизнь".
 */
public interface ConfigurationProcessor {

    /**
     * Считывает начальное состояние из файла.
     * @param propertyPath - путь к файлу.
     * @throws IOException - ошибка чтения из файла.
     */
    void readFrom(String propertyPath) throws  IOException;

    /**
     * Запись результата в файл.
     * @param outputDirectory - директория, в которую будет записан файл.
     * @throws IOException - ошибка записи файла.
     */
    void writeTo(String outputDirectory) throws IOException;

    int getNumberOfCycles();
    int getLengthOfCourt();
    Map<Integer, Set<Integer>> getInitialCells();
    Map<Integer, Set<Integer>> getResultCells();
    void setResultCells(Map<Integer, Set<Integer>> resultCells);
}
