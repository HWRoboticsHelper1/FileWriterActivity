import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class MyFileWriter {
    private static void printFileSize(String... fileNames) {
        long totalSize = 0;
        for (String fileName : fileNames) {
            File file = new File(fileName);
            if (file.exists()) {
                totalSize += file.length();
            }
        }
        System.out.println("Total size of all files: " + totalSize + " bytes");
    }

    public static void main(String[] args) {
        String data = "Hello, World!";
        String fileName1 = "example.txt";
        String fileName2 = "example2.txt";
        String fileName3 = "example3.txt";
        String fileName4 = "example4.txt";
        String fileName5 = "example5.txt";

        // 1. Using FileWriter
        try (FileWriter writer = new FileWriter(fileName1)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Using BufferedWriter
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName2))) {
            bufferedWriter.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 3. Using FileOutputStream
        try (FileOutputStream outputStream = new FileOutputStream(fileName3)) {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 4. Using BufferedOutputStream
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileName4))) {
            bufferedOutputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5. Using Files (java.nio.file)
        try {
            Files.write(Paths.get(fileName5), data.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeSecretFile("secop fail :(");
        printFileSize(".mysecret.txt");
        writeSecretFile("yo");
        printFileSize(".mysecret.txt");
        writeSecretFile("The quick brown fox jumps over the lazy dog.");
        printFileSize(".mysecret.txt");
        writeInSecretFolder("We've been found!");

        printFileSize(".undercoverfolder/coolstuff.txt");
    }

    // Writes a message to hidden file named ".mysecret.txt"
    public static void writeSecretFile(String content) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(".mysecret.txt"))) {
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Writes a message to non hidden file named "coolstuff.txt" in the hidden folder ".undercoverfolder"
    public static void writeInSecretFolder(String content) {
        File directory = new File(".undercoverfolder");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = directory.getPath() + "/coolstuff.txt";
        System.out.println(filePath);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Prints out the size of the given file path in bytes
    private static void printFileSize(String fileName) {
        File f = new File(fileName);
        if (f.exists() && f.isFile()) {
            long fileSize = f.length();
            System.out.println("File size: " + fileSize + " bytes");
        } else {
            System.out.println("File does not exist");
        }
    }

    // A to string method... for some reason
    public String toString() {
        return "I'm a file writer, bum bum ba dum bum bum";
    }
}