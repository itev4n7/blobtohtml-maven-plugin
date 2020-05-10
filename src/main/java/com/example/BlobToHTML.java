package com.example;

import j2html.tags.ContainerTag;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Base64;

import static j2html.TagCreator.*;

/**
 * Blob To HTML Maven Mojo {@code exampleVersion}.
 */
@Mojo(name = "blob-to-html", defaultPhase = LifecyclePhase.POST_INTEGRATION_TEST)
public class BlobToHTML extends AbstractMojo {

    @Parameter(name = "url")
    private String url;

    @Parameter(name = "username")
    private String username;

    @Parameter(name = "password")
    private String password;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("try create conn");
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            if (conn != null) {
                getLog().info("try to get rows");
                int rowsDB = getRowsDB(conn);
                getLog().info("try to get resource path");
                String path = project.getBuild().getOutputDirectory() + "/tableDB.html";
                ContainerTag html = table().attr("align='center'")
                          .with(tr().with(th(h3("id"))).with(th(h3("photo"))));
                for (int id = 1; id <= rowsDB; id++) {
                    String selectSQL = String.format("select photo from savedPhotos where id=%d;", id);
                    try (PreparedStatement pstmt = conn.prepareStatement(selectSQL);
                         ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            String binary = "data:image/png;base64," +
                                      Base64.getEncoder().encodeToString(rs.getBytes("photo"));
                            html.with(tr().with(td(h3(String.valueOf(id)))).with(td(img().withSrc(binary))));
                        }
                    } catch (SQLException e) {
                        getLog().error(e.getMessage());
                    }
                }
                writeHtml(path, html);
            }
        } catch (SQLException e) {
            getLog().error(e.getMessage());
        }
    }

    private void writeHtml(String path, ContainerTag html) {
        try {
            getLog().info("try to write in file");
            Files.write(Paths.get(path), html.render().getBytes());
        } catch (IOException e) {
            getLog().error(e.getMessage());
        }
    }

    private int getRowsDB(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            ResultSet resultSet = stmt.executeQuery("select count(*) from savedPhotos;");
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            getLog().error(e.getMessage());
        }
        return 0;
    }
}