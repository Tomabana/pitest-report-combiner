package com.banas.tools.pitestReportCombiner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;

public class FileUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class);
    private static final String INDEX_HTML = "index.html";

    public void deleteDir(String directory) {
        try {
            LOGGER.info("Deleting directory=" + directory);
            org.apache.commons.io.FileUtils.deleteDirectory(new File(directory));
        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    public void findDirectories(File root, String pitReportDir, List<File> directories) {
        FileFilter directoryFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };

        File[] files = root.listFiles(directoryFilter);
        for (File file : files) {
            if (pitReportDir.equals(file.getName())) {
                directories.add(file);
            }
            findDirectories(file, pitReportDir, directories);
        }
    }

    public void copyDirectory(File source, File destination, String pitReportDirectoryName) {
        if (source.getName().equals(pitReportDirectoryName) && source.listFiles() != null
            && source.listFiles().length > 0) {
            try {
                org.apache.commons.io.FileUtils.copyDirectory(source.listFiles()[0], destination);
                renameIndexHtml(source, destination);
            } catch (IOException e) {
                LOGGER.error("", e);
            }
        }
    }

    private void renameIndexHtml(File source, File directory) {
        String sourcePath = source.getPath();
        String moduleName = sourcePath.substring(0, source.getPath().indexOf("target") - 1);
        moduleName = moduleName.substring(moduleName.lastIndexOf(File.separator) + 1, moduleName.length());
        String newIndexHtmlName = moduleName + ".html";

        File indexHtmlFile = new File(directory + File.separator + INDEX_HTML);
        if (indexHtmlFile.exists()) {
            LOGGER.info("index.html file for module=" + moduleName + " will be renamed to=" + newIndexHtmlName);
            File destination = new File(directory + File.separator + newIndexHtmlName);
            indexHtmlFile.renameTo(destination);
        }
    }
}
