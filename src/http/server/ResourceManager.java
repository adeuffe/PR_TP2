package http.server;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ResourceManager {

    public static Path getResourcePath(String resource) {
        return Paths.get("resources", resource);
    }

    public static boolean deleteResource(String resource) {
        Path resourcePath = getResourcePath(resource);
        File resourceFile = new File(resourcePath.toAbsolutePath().normalize().toString());
        return resourceFile.delete();
    }

    public static boolean isResourceExists(String resource) {
        Path resourcePath = getResourcePath(resource);
        File resourceFile = new File(resourcePath.toAbsolutePath().normalize().toString());
        return resourceFile.exists();
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
}
