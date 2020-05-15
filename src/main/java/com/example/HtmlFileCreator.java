package com.example;

import j2html.tags.ContainerTag;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HtmlFileCreator {
    private static final Logger LOGGER = Logger.getLogger(HtmlFileCreator.class);

    public static void saveHtml(String path, ContainerTag html) {
        try {
            LOGGER.info("try to save file, path=" + path);
            Files.write(Paths.get(path), html.render().getBytes());
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
