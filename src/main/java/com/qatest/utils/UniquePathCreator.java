/**
 * 
 */
package com.qatest.utils;

/**
 * @author Divyang
 *
 */
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UniquePathCreator {

    /**
     * Creates a unique file or directory inside the given parent directory.
     * If the name exists, appends (1), (2), etc., until a unique name is found.
     *
     * @param parentDirPath Path to the parent directory (automatically creates parent directories if missing)
     * @param baseName      Desired name (without extension for files)
     * @param extension     File extension without dot (null for directories)
     * @param isDirectory   true to create a directory, false to create a file
     * @return Path to the created file or directory
     * @throws IOException if creation fails
     */
	public static Path createUniquePath(String parentDirPath, String baseName, String extension, boolean isDirectory)
            throws IOException {

        if (parentDirPath == null || baseName == null) {
            throw new IllegalArgumentException("Parent directory and base name must not be null.");
        }

        Path parentDir = Paths.get(parentDirPath);
        if (Files.notExists(parentDir)) {
            Files.createDirectories(parentDir); // Ensure parent directory exists
        }

        int counter = 1;
        while (true) {
            // 1. Generate the candidate path
            String name;
            if (counter == 1) {
                name = (extension != null && !isDirectory) ? baseName + "." + extension : baseName;
            } else {
                name = (extension != null && !isDirectory)
                        ? baseName + "_" + counter + "." + extension
                        : baseName + "_" + counter;
            }
            Path path = parentDir.resolve(name);

            // 2. Atomically attempt to create the file/directory
            try {
                if (isDirectory) {
                    return Files.createDirectory(path);
                } else {
                    return Files.createFile(path);
                }
            } catch (FileAlreadyExistsException e) {
                // 3. If someone beat us to it, increment and try again
                counter++;
            }
        }
    }
}