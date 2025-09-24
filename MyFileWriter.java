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
        try {
            System.out.println(hashFile(".undercoverfolder/coolstuff.txt"));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        testHashFile();
        testHashFileEmptyFiles();
        testHashFileLargeFiles();
        testHashFileSpecialChars();
        testHashNonExistant();
    }

    public static String hashFile(String filePath) throws FileNotFoundException {
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
            throw new FileNotFoundException("File doesn't exist");
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

    public static void testHashFile() {
        // Contains the message "Your fate is chosen..."
        String testFilePath = "PredeterminedTest.txt";
        String hashVal;
        try {
            hashVal = hashFile(testFilePath);
        } catch (Exception e) {
            return;
        }
        String expectedValue = "8d2aa27b229f4623d0b1509be943ac9da9b552d7e376624f8c32a7949915f8ec";
        assert hashVal.equals(expectedValue) : "Hash was not performed correctly";
        System.out.println("Hash was successful");
    }

    public static void testHashFileEmptyFiles() {
        // An empty file
        String testFilePath = "EmptyFile.txt";
        String hashVal;
        try {
            hashVal = hashFile(testFilePath);
        } catch (Exception e) {
            return;
        }
        String expectedValue = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        assert hashVal.equals(expectedValue) : "Hash did not handle empty case correctly";
        System.out.println("Empty file handled successfully");
    }

    public static void testHashFileLargeFiles() {
        // Definitely not just the great gatsby text file with the newlines replace by spaces
        String testFilePath = "LargeFile.txt";
        String hashVal;
        try {
            hashVal = hashFile(testFilePath);
        } catch (Exception e) {
            return;
        }
        String expectedValue = "341776eb9826df4fbf232baff22e22c18d4ebe49eccccc2ebbfa2cebe524bf6b";
        assert hashVal.equals(expectedValue) : "Hash did not handle empty case correctly";
        System.out.println("Large file handled successfully");
    }

    public static void testHashFileSpecialChars() {
        // Just 3 duck emojis
        String testFilePath = "SpecialCharsFile.txt";
        String hashVal;
        try {
            hashVal = hashFile(testFilePath);
        } catch (Exception e) {
            return;
        }
        String expectedValue = "20f498fed5aa49ada2aeae4a91c8acc06b8aabf3f1fd2bbf543a1e0ded065208";
        assert hashVal.equals(expectedValue) : "Hash did not handle special character correctly";
        System.out.println("Special characters handled successfully");
    }

    public static void testHashNonExistant() {
        String testFilePath = "NonexistantFile.txt";
        try {
            String hashVal = hashFile(testFilePath);
            System.out.println("Error not thrown properly, got the hash: " + hashVal);
        } catch (Exception e) {
            System.out.println("Error thrown properly");
        }
    }


}