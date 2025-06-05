import java.net.*;
import java.io.*;
import java.nio.file.*;

public class MavenWrapperDownloader {
    public static void main(String[] args) throws Exception {
        String url = "https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.9.4/maven-wrapper-3.9.4.jar";
        String jarPath = ".mvn/wrapper/maven-wrapper.jar";
        Path path = Paths.get(jarPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            try (InputStream in = URI.create(url).toURL().openStream()) {
                Files.copy(in, path);
            }
        }
    }
}
