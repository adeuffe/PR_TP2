package http.server;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

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

    public static String readResource(String resource) throws FileNotFoundException {
        String dataList = "";
        Path resourcesPath = getResourcesPath();
        String resourcePath = Paths.get(resourcesPath.toAbsolutePath().normalize().toString(), resource).toAbsolutePath().normalize().toString();
        File resourceFile = new File(resourcePath);
        Scanner reader = new Scanner(resourceFile);
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            dataList = dataList.concat(data);
        }
        reader.close();
        return dataList;
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
