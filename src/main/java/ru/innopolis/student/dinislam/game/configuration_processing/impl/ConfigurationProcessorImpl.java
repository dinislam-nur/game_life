package ru.innopolis.student.dinislam.game.configuration_processing.impl;

import lombok.Getter;
import lombok.Setter;
import ru.innopolis.student.dinislam.game.configuration_processing.api.ConfigurationProcessor;
import ru.innopolis.student.dinislam.game.impl.Court;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Реализация обработчика конфигурации начального состояния.
 */
@Getter
public class ConfigurationProcessorImpl implements ConfigurationProcessor {


    /**
     * Количество циклов жизни.
     */
    private int numberOfCycles;

    /**
     * Начальный список живых клеток.
     */
    private Map<Integer, Set<Integer>> initialCells;

    /**
     * Результирующий список клеток, который будет записан в файл.
     */
    @Setter
    private Map<Integer, Set<Integer>> resultCells;

    /**
     * Длина площадки.
     */
    private int lengthOfCourt;


    /**
     * Читает начальную конфигурацию из файла и записывает в поля класса.
     *
     * @param propertyPath - путь к файлу.
     * @throws IOException - ошибка чтения файла.
     */
    @Override
    public void readFrom(String propertyPath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(propertyPath))) {

            String numberCycles = reader.readLine();
            if (numberCycles.matches("\\s*number_of_cycles = \\d+\\s*")) {
                getNumberOfCycles(numberCycles);
            } else {
                throw new IllegalArgumentException("Неправильный формат количества циклов");
            }

            final String length = reader.readLine();
            if (length.matches("\\s*court_length = (\\d+)\\s*")) {
                getLengthOfCourt(length);
            } else {
                throw new IllegalArgumentException("Неправильный формат длины площадки");
            }

            String[][] court2 = new String[lengthOfCourt][lengthOfCourt];
            for (int i = 0; i < lengthOfCourt; i++) {
                final String line = reader.readLine();
                if (line == null) {
                    throw new IllegalStateException("Неправильная площадка");
                }
                final String[] splittedLine = line.trim().split(" ");
                if (splittedLine.length != lengthOfCourt) {
                    throw new IllegalStateException("Неправильная площадка");
                }
                for (String element : splittedLine) {
                    if (!element.matches("[*O]")) {
                        throw new IllegalStateException("Неправильная площадка");
                    }
                }
                court2[i] = splittedLine;
            }
            createCells(court2);

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Файл из path не найден");
        } catch (IOException e) {
            throw new IOException("Проблемы с потоком ввода");
        }
    }

    /**
     * Запись результат из поля класса resultCells в файл.
     *
     * @param outputDirectory - директория, в которую будет записан файл.
     * @throws IOException - ошибка записи в файл.
     */
    @Override
    public void writeTo(String outputDirectory) throws IOException {
        if (resultCells == null) {
            throw new IllegalStateException("Result cells have not been initialized!");
        }
        final File file = new File(outputDirectory);
        if (!file.exists()) {
            file.mkdir();
        }
        try (Writer writer = new FileWriter(new File(file, "/result.txt"))) {
            writer.write(prepareResult());
        } catch (IOException e) {
            throw new IOException("Проблемы с записью результата!");
        }
    }

    /**
     * Парсер из строкового представления площадки в список живых клеток.
     *
     * @param court - строковое представление площадки.
     */
    private void createCells(String[][] court) {

        initialCells = new HashMap<>();
        for (int i = 0; i < lengthOfCourt; i++) {
            for (int j = 0; j < lengthOfCourt; j++) {
                if ("O".equals(court[i][j])) {
                    final int coordinateY = lengthOfCourt - 1 - i;
                    initialCells.compute(j, (key, value) -> {
                        value = (value == null) ? new HashSet<>() : value;
                        value.add(coordinateY);
                        return value;
                    });
                }
            }
        }
    }

    /**
     * Читает длину площадки из строки и записывает в соответствующее поле.
     *
     * @param inputString - входная строка.
     */
    private void getLengthOfCourt(String inputString) {
        final Matcher matcherCourtLength = Pattern.compile("(\\d+)")
                .matcher(inputString);
        matcherCourtLength.find();
        lengthOfCourt = Integer.parseInt(matcherCourtLength.group());
    }

    /**
     * Читает количество циклов из строки и записывает в соответствующее поле.
     *
     * @param inputString - входная строка.
     */
    private void getNumberOfCycles(String inputString) {
        final Matcher matcherCycles = Pattern.compile("(\\d+)").
                matcher(inputString);
        matcherCycles.find();
        numberOfCycles = Integer.parseInt(matcherCycles.group());
    }

    /**
     * Подготовка строкового представления площадки.
     *
     * @return - строковое представление площадки.
     */
    private String prepareResult() {
        if (lengthOfCourt == 0) {
            throw new IllegalStateException("Размер площадки равен 0");
        }
        char[][] court = new char[lengthOfCourt][lengthOfCourt];

        Court.fillCourt(court, resultCells);
        final StringBuilder stringBuilder = new StringBuilder(System.lineSeparator());
        for (char[] yAxis : court) {
            stringBuilder.append(yAxis)
                    .append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
