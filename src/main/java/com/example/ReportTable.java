package com.example;

import j2html.tags.ContainerTag;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static j2html.TagCreator.*;

public class ReportTable {
    private static final Logger LOGGER = Logger.getLogger(ReportTable.class);

    public static void writeHtmlPhotoReport(Connection conn, ContainerTag html) {
        LOGGER.info("try to write html report");
        for (int id = 1; id < Database.getRows(conn); id++) {
            String selectSQL = String.format("select photo from savedPhotos where id=%d;", id);
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String binary = Base64Creator.createPhoto(rs);
                    html.with(tr().with(td(h3(String.valueOf(id)))).with(td(img().withSrc(binary))));
                }
            } catch (SQLException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
