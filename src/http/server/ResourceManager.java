package http.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class manage resource manipulations
 *
 * @author Loïc DUBOIS-TERMOZ
 * @author Alexandre DUFOUR
 */
public class ResourceManager {

    /**
     * The path of the resources folder used by this HTTP server
     */
    public static final String RESOURCES_PATH = "resources/";

    /**
     * Gets and returns the path of the specified resource from the resource directory of this HTTP server
     *
     * @param resource the resource path relative to the resources directory of this HTTP server
     * @return the path of the specified resource
     */
    public static Path getResourcePath(String resource) {
        return Paths.get(RESOURCES_PATH, resource);
    }

    /**
     * Checks and returns "true" if the specified resource exists, "false" otherwise
     *
     * @param resource the resource path relative to the resources directory of this HTTP server
     * @return "true" if the specified resource exists, "false" otherwise
     */
    public static boolean isResourceExists(String resource) {
        Path resourcePath = getResourcePath(resource);
        File resourceFile = new File(resourcePath.toAbsolutePath().normalize().toString());
        return resourceFile.exists();
    }

    /**
     * Returns "true" if the specified resource is dynamic, "false" otherwise
     *
     * @param resource the targeted resource
     * @return "true" if the specified resource is dynamic, "false" otherwise
     */
    public static boolean isDynamicResource(String resource) {
        boolean isDynamicResource = false;
        if (isResourceExists(resource)) {
            Path path = getResourcePath(resource);
            isDynamicResource = path.startsWith(getResourcePath("dynamicFolder"));
        }
        return isDynamicResource;
    }

    /**
     * Gets and returns the file extension of the specified resource
     *
     * @param resource the targeted resource
     * @return the extension of the specified file resource
     */
    public static String getFileExtension(String resource) {
        return resource.substring(resource.indexOf(".") + 1);
    }

    /**
     * Reads and returns the content of the specified resource
     *
     * @param resource the resource path relative to the resources directory of this HTTP server
     * @return the content of the specified resource
     * @throws IOException if an I/O exception is raised during the reading process
     */
    public static byte[] readResource(String resource) throws IOException {
        return Files.readAllBytes(getResourcePath(resource));
    }

    /**
     * Creates the resource in the HTTP server with the specified content and returns "true" if the creation happen
     * successfully, "false" otherwise
     *
     * @param resource the resource path relative to the resources directory of this HTTP server
     * @param content  the content to insert into the created resource
     * @return "true" if the creation of the resource happen successfully, "false" otherwise
     * @throws IOException if an I/O exception is raised during the creation process
     */
    public static boolean createResource(String resource, String content) throws IOException {
        Path resourcePath = getResourcePath(resource);
        File resourceFile = new File(resourcePath.toAbsolutePath().normalize().toString());
        boolean isCreated = resourceFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(resourcePath.toAbsolutePath().normalize().toString()));
        bw.write(content);
        bw.flush();
        bw.close();
        return isCreated;
    }

    /**
     * Re-creates the resource in the HTTP server with the specified content and returns "true" if the creation happen
     * successfully, "false" otherwise
     *
     * @param resource the resource path relative to the resources directory of this HTTP server
     * @param content  the content to insert into the replaced resource
     * @return "true" if the creation of the resource happen successfully, "false" otherwise
     * @throws IOException if an I/O exception is raised during the re-creation process
     */
    public static boolean replaceResource(String resource, String content) throws IOException {
        deleteResource(resource);
        return createResource(resource, content);
    }

    /**
     * Append to the targeted resource the specified content
     *
     * @param resource the resource path relative to the resources directory of this HTTP server
     * @param content  the content to append to the targeted resource
     * @throws IOException if an I/O exception is raised during the append process
     */
    public static void appendResource(String resource, String content) throws IOException {
        Path resourcePath = getResourcePath(resource);
        BufferedWriter bw = new BufferedWriter(new FileWriter(resourcePath.toAbsolutePath().normalize().toString(), true));
        bw.write(content);
        bw.flush();
        bw.close();
    }

    /**
     * Deletes the specified resource and returns "true" if the action has been realized successfully, "false" otherwise
     *
     * @param resource the resource path relative to the resources directory of this HTTP server
     * @return "true" if the delete has been realized successfully, "false" otherwise
     */
    public static boolean deleteResource(String resource) {
        Path resourcePath = getResourcePath(resource);
        File resourceFile = new File(resourcePath.toAbsolutePath().normalize().toString());
        return resourceFile.delete();
    }
}
