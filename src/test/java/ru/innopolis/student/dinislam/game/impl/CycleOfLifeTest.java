package ru.innopolis.student.dinislam.game.impl;

import org.junit.jupiter.api.Test;
import ru.innopolis.student.dinislam.game.configuration_processing.api.ConfigurationProcessor;
import ru.innopolis.student.dinislam.game.configuration_processing.impl.ConfigurationProcessorImpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CycleOfLifeTest {

    @Test
    void singleThreadTest() throws IOException {
        String resourcePath = "src/test/resources/initial_configurations";

        final ConfigurationProcessorImpl configurationProcessor = new ConfigurationProcessorImpl();
        configurationProcessor.readFrom(resourcePath + "/test_court_config.txt");
        final Map<Integer, Set<Integer>> initialCells = configurationProcessor.getInitialCells();
        final int lengthOfCourt = configurationProcessor.getLengthOfCourt();

        final CycleOfLife cycleOfLife = new CycleOfLife(initialCells, lengthOfCourt);
        final Map<Integer, Set<Integer>> result = cycleOfLife.startCycle();

        configurationProcessor.readFrom(resourcePath + "/result_of_one_cycle.txt");
        final Map<Integer, Set<Integer>> answer = configurationProcessor.getInitialCells();

        assertEquals(answer, result);
    }

    @Test
    void concurrentThreadTest() throws IOException {
        String resourcePath = "src/test/resources/initial_configurations";

        final ConfigurationProcessorImpl configurationProcessor = new ConfigurationProcessorImpl();
        configurationProcessor.readFrom(resourcePath + "/test_court_config.txt");
        final Map<Integer, Set<Integer>> initialCells = configurationProcessor.getInitialCells();
        final int lengthOfCourt = configurationProcessor.getLengthOfCourt();

        final CycleOfLife cycleOfLife = new CycleOfLife(initialCells, lengthOfCourt);
        final Map<Integer, Set<Integer>> result = cycleOfLife.startConcurrentCycle();

        configurationProcessor.readFrom(resourcePath + "/result_of_one_cycle.txt");
        final Map<Integer, Set<Integer>> answer = configurationProcessor.getInitialCells();

        assertEquals(answer, result);
    }

    @Test
    void compareSingleThreadWithConcurrentTest() throws IOException {
        final String resourcePath = "src/test/resources/initial_configurations/generated";
        final String resourceName = "/generated_0.txt";
        final int numberOfCycles = 10_000;
        final int courtLength = 199;
        generateResource(resourceName, numberOfCycles, courtLength);

        final ConfigurationProcessor configurationProcessor = new ConfigurationProcessorImpl();
        configurationProcessor.readFrom(resourcePath + resourceName);

        final Map<Integer, Set<Integer>> initialCells = configurationProcessor.getInitialCells();

        Map<Integer, Set<Integer>> cells = initialCells;
        long start = System.currentTimeMillis();
        for (int i = 0; i < numberOfCycles; i++) {
            cells = new CycleOfLife(cells, courtLength).startCycle();
        }
        final long durationSingleThread = System.currentTimeMillis() - start;
        System.out.println("Время работы однопоточной версии: " + TimeUnit.MILLISECONDS.toSeconds(durationSingleThread));
        Map<Integer, Set<Integer>> resultSingleThreading = cells;

        cells = initialCells;
        start = System.currentTimeMillis();
        for (int i = 0; i < numberOfCycles; i++) {
            cells = new CycleOfLife(cells, courtLength).startConcurrentCycle();
        }
        final long durationMultithreading = System.currentTimeMillis() - start;
        System.out.println("Время работы многопоточной версии: " + TimeUnit.MILLISECONDS.toSeconds(durationMultithreading));
        Map<Integer, Set<Integer>> resultMultithreading = cells;

        configurationProcessor.setResultCells(cells);
        configurationProcessor.writeTo(resourcePath);

        assertEquals(resultSingleThreading, resultMultithreading);
        assertTrue(durationMultithreading < durationSingleThread);
    }


    private void generateResource(String resourceName, int numberOfCycles, int courtLength) {
        final String resourcePath = "src/test/resources/initial_configurations/generated";

        final File resourceDir = new File(resourcePath);
        if (!resourceDir.exists()) {
            resourceDir.mkdir();
        }
        final File file = new File(resourceDir, resourceName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String data = "number_of_cycles = " + numberOfCycles + System.lineSeparator() +
                    "court_length = " + courtLength + System.lineSeparator() +
                    generateCourt(courtLength);
            writer.write(data, 0, data.length());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String generateCourt(int courtLength) {
        final StringBuilder stringBuilder = new StringBuilder();
        int size = courtLength * courtLength;

        for (int i = 0; i < size; i++) {
            switch (i % 2) {
                case 0 : {
                    stringBuilder.append("* ");
                    break;
                }
                case 1: {
                    stringBuilder.append("O ");
                    break;
                }
            }
            if ((i + 1) % courtLength == 0) {
                stringBuilder.setLength(stringBuilder.length() - 1);
                stringBuilder.append(System.lineSeparator());
            }
        }

        return stringBuilder.toString();
    }
}