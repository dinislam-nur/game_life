package ru.innopolis.student.dinislam.game.impl;

import org.junit.jupiter.api.Test;
import ru.innopolis.student.dinislam.game.api.Game;
import ru.innopolis.student.dinislam.game.configuration_processing.api.ConfigurationProcessor;
import ru.innopolis.student.dinislam.game.configuration_processing.impl.ConfigurationProcessorImpl;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LifeTest {

    @Test
    void startTest() throws IOException {
        final String resourcePath = "src/test/resources/initial_configurations";

        final ConfigurationProcessor configurationProcessor = new ConfigurationProcessorImpl();

        configurationProcessor.readFrom(resourcePath + "/test_court_config.txt");

        final Map<Integer, Set<Integer>> initialCells = configurationProcessor.getInitialCells();
        final int lengthOfCourt = configurationProcessor.getLengthOfCourt();
        final int numberOfCycles = configurationProcessor.getNumberOfCycles();

        final Game life = new Life(initialCells, lengthOfCourt, numberOfCycles);

        final Map<Integer, Set<Integer>> result = life.start();

        configurationProcessor.readFrom(resourcePath + "/result_of_more_50_cycles.txt");
        final Map<Integer, Set<Integer>> answer = configurationProcessor.getInitialCells();

        assertEquals(answer, result);
    }

}