import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyFileWriter {
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
        System.out.println(hashFile(".undercoverfolder/coolstuff.txt"));
    }

    public static String hashFile(String filePath) {
        File f = new File(filePath);
        String contents = "";
        String line = "";
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(f))) {
            line = bufferedReader.readLine();
            while (line != null) {
                contents += line;
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] hashVal = new byte[0];
        try {
            hashVal = getSHA(contents);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String hexaHash = toHexString(hashVal);

        return hexaHash;
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    // Writes a message to hidden file named ".mysecret.txt"
    public static void writeSecretFile(String content) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(".mysecret.txt"))) {
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Writes a message to non hidden file named "coolstuff.txt" in the hidden
    // folder ".undercoverfolder"
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