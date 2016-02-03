package com.banas.tools.pitestReportCombiner;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

public class CommandLineArgumentParser {

    private static final Logger LOGGER = Logger.getLogger(CommandLineArgumentParser.class);
    private static final String PROJECT_ROOT_PATH_PARAM = "projectRootPath";
    private static final String CONCAT_REPORT_DIRECTORY_PATH_PARAM = "concatDirectoryPath";
    private static final String PIT_DIRECTORY_NAME = "pitDirName";
    private static final String DEFAULT_PIT_DIRECTORY_NAME = "pit-reports";

    public ReportConcatParameters createReportConcatBasedOnCommandLineArguments(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(PROJECT_ROOT_PATH_PARAM, true, "path for project root");
        options.addOption(CONCAT_REPORT_DIRECTORY_PATH_PARAM, true, "path for directory for concat report");
        options.addOption(PIT_DIRECTORY_NAME, true, "directory name for pit reports in projects");
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        String rootProjectDirectory = cmd.getOptionValue(PROJECT_ROOT_PATH_PARAM);
        String reportConcatDirectory = cmd.getOptionValue(CONCAT_REPORT_DIRECTORY_PATH_PARAM);
        String pitReportDirectory = cmd.getOptionValue(PIT_DIRECTORY_NAME);
        if (rootProjectDirectory == null) {
            LOGGER.error("You must provide path for project root using parameter --projectRootPath");
            System.exit(1);
        } else if (reportConcatDirectory == null) {
            LOGGER.error(
                "You must provide path where concat report will be saved using parameter --concatDirectoryPath");
            System.exit(1);
        } else if (pitReportDirectory == null) {
            pitReportDirectory = DEFAULT_PIT_DIRECTORY_NAME;
            LOGGER.info("Pit directory name parameter --pitDirName is not provided. Using default="
                + DEFAULT_PIT_DIRECTORY_NAME);
        }

        return new ReportConcatParameters(rootProjectDirectory, reportConcatDirectory, pitReportDirectory);
    }
}
