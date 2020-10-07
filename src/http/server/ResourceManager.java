package http.server;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceManager {

    public static Path getResourcePath(String resource) {
        return Paths.get("resources", resource);
    }

    public static boolean deleteResource(String resource) {
        Path resourcePath = getResourcePath(resource);
        File resourceFile = new File(resourcePath.toAbsolutePath().normalize().toString());
        return resourceFile.delete();
    }
}
