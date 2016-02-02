package com.banas.tools.pitestReportCombiner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class ReportConcat {

    private static final Logger LOGGER = Logger.getLogger(ReportConcat.class);
    private static final String DEFAULT_CODING_OF_HTML_FILES = "UTF-8";
    private static final FileFilter HTML_EXTENSION_FILTER = new FileFilter() {
        public boolean accept(File file) {
            return file.getName().endsWith(".html");
        }
    };
    private FileUtils fileUtils = new FileUtils();

    public void run(String rootProjectDirectoryPath, String reportConcatDirectoryPath, String pitReportDirectoryName) {
        fileUtils.deleteDir(reportConcatDirectoryPath);
        File root = new File(rootProjectDirectoryPath);
        List<File> reports = new ArrayList<File>();
        fileUtils.findDirectories(root, pitReportDirectoryName, reports);

        File reportConcatDirectory = new File(reportConcatDirectoryPath);
        if (!reportConcatDirectory.exists()) {
            try {
                Files.createDirectory(reportConcatDirectory.toPath());
            } catch (IOException e) {
                LOGGER.error("", e);
            }
        }
        for (File report : reports) {
            fileUtils.copyDirectory(report, reportConcatDirectory, pitReportDirectoryName);
        }

        concatHTMLFiles(reportConcatDirectoryPath);
    }

    private void concatHTMLFiles(String reportConcatDirectoryPath) {
        LOGGER.info("Concatenating html files");
        List<Elements> elementsList = new ArrayList<Elements>();
        File reportConcatDirectory = new File(reportConcatDirectoryPath);
        File[] htmlFiles = reportConcatDirectory.listFiles(HTML_EXTENSION_FILTER);
        for (File file : htmlFiles) {
            try {
                Document docRoot = Jsoup.parse(file, DEFAULT_CODING_OF_HTML_FILES, "");
                Elements bodyChildren = docRoot.select("body").get(0).children();
                //Remove Pit Test Coverage Report header
                bodyChildren.remove(0);
                //Removing Report generated by element
                bodyChildren.remove(bodyChildren.size() - 1);
                //Add module name header
                Element moduleNameHeader = new Element(Tag.valueOf("h1"), "");
                String moduleName = file.getName().substring(0, file.getName().indexOf(".html"));
                moduleNameHeader.html(moduleName);
                bodyChildren.add(0, moduleNameHeader);
                elementsList.add(bodyChildren);
            } catch (IOException e) {
                LOGGER.error("", e);
            }
        }

        File lastHtmlFile = htmlFiles[htmlFiles.length - 1];
        File newIndexHtmlFile = new File(reportConcatDirectoryPath + File.separator + "index.html");
        if (!newIndexHtmlFile.exists()) {
            try {
                newIndexHtmlFile.createNewFile();
            } catch (IOException e) {
                LOGGER.error("", e);
            }
        }

        List<Element> elementList = new ArrayList<Element>();
        for (Elements elements : elementsList) {
            for (Element element : elements) {
                elementList.add(element);
            }
        }

        try {
            Document docRoot = Jsoup.parse(lastHtmlFile, DEFAULT_CODING_OF_HTML_FILES, "");
            docRoot.select("body").empty();
            for (Element element : elementList) {
                docRoot.select("body").append(element.outerHtml());
            }
            org.apache.commons.io.FileUtils.writeStringToFile(newIndexHtmlFile, docRoot.outerHtml(),
                DEFAULT_CODING_OF_HTML_FILES);
        } catch (IOException e) {
            LOGGER.error("", e);
        }

        LOGGER.info("Concatenation finished.");
    }
}