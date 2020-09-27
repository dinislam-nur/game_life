package ru.innopolis.student.dinislam.game;


import ru.innopolis.student.dinislam.game.api.Game;
import ru.innopolis.student.dinislam.game.configuration_processing.api.ConfigurationProcessor;
import ru.innopolis.student.dinislam.game.configuration_processing.impl.ConfigurationProcessorImpl;
import ru.innopolis.student.dinislam.game.impl.Life;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Launcher {

    public static void main(String[] args) throws IOException {

        final String resourcePath = args[0];
        final String outputDirectory = args[1];

        final ConfigurationProcessor configurationProcessor = new ConfigurationProcessorImpl();
        configurationProcessor.readFrom(resourcePath);

        final Game life = new Life(configurationProcessor.getInitialCells(),
                configurationProcessor.getLengthOfCourt(),
                configurationProcessor.getNumberOfCycles());

        final Map<Integer, Set<Integer>> result = life.start();
        configurationProcessor.setResultCells(result);
        configurationProcessor.writeTo(outputDirectory);
    }
}
