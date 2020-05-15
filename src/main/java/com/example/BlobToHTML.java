package com.example;

import j2html.tags.ContainerTag;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            getLog().info("try to get resource path");
            String path = project.getBuild().getTestSourceDirectory() + "/tableDB.html";
            ContainerTag html = Base64Creator.createHtmlTable();
            ReportTable.writeHtmlPhotoReport(conn, html);
            HtmlFileCreator.saveHtml(path, html);
            Database.dropDBTable(conn);
        } catch (SQLException e) {
            getLog().error(e.getMessage());
        }
    }
}