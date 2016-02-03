package com.banas.tools.pitestReportCombiner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class FileUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class);
    private static final String INDEX_HTML = "index.html";

    public List<File> findDirectories(File rootDirectory, String pitReportDirectoryName) {
        List<File> directories = new ArrayList<>();
        File[] files = rootDirectory.listFiles(file -> file.isDirectory());
        for (File file : files) {
            if (pitReportDirectoryName.equals(file.getName())) {
                directories.add(file);
            }
            directories.addAll(findDirectories(file, pitReportDirectoryName));
        }
        return directories;
    }

    public void copyDirectoryAndRenameIndexHtml(File source, File destination, String pitReportDirectoryName) throws IOException {
        if (source.getName().equals(pitReportDirectoryName) && source.listFiles() != null
            && source.listFiles().length > 0) {
            org.apache.commons.io.FileUtils.copyDirectory(source.listFiles()[0], destination);
            renameIndexHtml(source, destination);
        }
    }

    private void renameIndexHtml(File source, File destination) {
        String sourcePath = source.getPath();
        String moduleName = sourcePath.substring(0, source.getPath().indexOf("target") - 1);
        moduleName = moduleName.substring(moduleName.lastIndexOf(File.separator) + 1, moduleName.length());
        String newIndexHtmlName = moduleName + ".html";

        File indexHtmlFile = new File(destination + File.separator + INDEX_HTML);
        if (indexHtmlFile.exists()) {
            LOGGER.info("index.html file for module=" + moduleName + " will be renamed to=" + newIndexHtmlName);
            indexHtmlFile.renameTo(new File(destination + File.separator + newIndexHtmlName));
        }
    }
}
