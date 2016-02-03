package com.banas.tools.pitestReportCombiner;

public class Runner {
    public static void main(String[] args) throws Exception {
        CommandLineArgumentParser commandLineArgumentParser = new CommandLineArgumentParser();
        ReportConcatParameters reportConcatParameters =
                commandLineArgumentParser.createReportConcatBasedOnCommandLineArguments(args);
        ReportConcat reportConcat = new ReportConcat(reportConcatParameters);
        reportConcat.run();
    }
}
