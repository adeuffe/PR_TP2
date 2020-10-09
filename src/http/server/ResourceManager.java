package http.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceManager {

    public static final String RESOURCES_PATH = "resources/";

    public static Path getResourcesPath() {
        return Paths.get(RESOURCES_PATH);
    }

    public static Path getResourcePath(String resource) {
        return Paths.get("resources", resource);
    }

    public static boolean isResourceExists(String resource) {
        Path resourcePath = getResourcePath(resource);
        File resourceFile = new File(resourcePath.toAbsolutePath().normalize().toString());
        return resourceFile.exists();
    }

    public static byte[] readResource(String resource) throws IOException {
        return Files.readAllBytes(getResourcePath(resource));
    }

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

    public static boolean replaceResource(String resource, String content) throws IOException {
        deleteResource(resource);
        return createResource(resource, content);
    }

    public static void appendResource(String resource, String content) throws IOException {
        Path resourcePath = getResourcePath(resource);
        BufferedWriter bw = new BufferedWriter(new FileWriter(resourcePath.toAbsolutePath().normalize().toString(), true));
        bw.write(content);
        bw.flush();
        bw.close();
    }

    public static boolean deleteResource(String resource) {
        Path resourcePath = getResourcePath(resource);
        File resourceFile = new File(resourcePath.toAbsolutePath().normalize().toString());
        return resourceFile.delete();
    }
}
