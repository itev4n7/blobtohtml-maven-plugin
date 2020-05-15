package com.example;

import j2html.tags.ContainerTag;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import static j2html.TagCreator.*;

public class Base64Creator {
    private static final Logger LOGGER = Logger.getLogger(Base64Creator.class);

    public static ContainerTag createHtmlTable() {
        LOGGER.info("create html table");
        return table().attr("align='center'").with(tr().with(th(h3("id"))).with(th(h3("photo"))));
    }

    public static String createPhoto(ResultSet rs) throws SQLException {
        LOGGER.info("create photo use base64");
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(rs.getBytes("photo"));
    }
}
