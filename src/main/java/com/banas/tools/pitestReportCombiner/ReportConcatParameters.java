package com.banas.tools.pitestReportCombiner;

public class ReportConcatParameters {

    private String rootProjectDirectoryPath;
    private String reportConcatDirectoryPath;
    private String pitReportDirectoryName;

    public ReportConcatParameters(String rootProjectDirectoryPath, String reportConcatDirectoryPath,
                                  String pitReportDirectoryName) {
        this.rootProjectDirectoryPath = rootProjectDirectoryPath;
        this.reportConcatDirectoryPath = reportConcatDirectoryPath;
        this.pitReportDirectoryName = pitReportDirectoryName;
    }

    public String getRootProjectDirectoryPath() {
        return rootProjectDirectoryPath;
    }

    public String getReportConcatDirectoryPath() {
        return reportConcatDirectoryPath;
    }

    public String getPitReportDirectoryName() {
        return pitReportDirectoryName;
    }
}
