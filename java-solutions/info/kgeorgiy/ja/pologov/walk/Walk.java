package info.kgeorgiy.ja.pologov.walk;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

public class Walk {
    public static void main(String[] args) {
        if (args == null) {
            System.err.println("Error: arguments are null");
        } else if (args.length < 2) {
            System.err.println("Error: wrong number of arguments");
        } else if (args[0] == null) {
            System.err.println("Error: first argument is null");
        } else if (args[1] == null) {
            System.err.println("Error: second argument is null");
        } else {
            try {
                crawlFiles(args[0], args[1]);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void crawlFiles(String input, String output) throws WalkException {
        Path inputPath, outputPath;
        try {
            inputPath = Path.of(input);
        } catch (InvalidPathException e) {
            throw new WalkException("Error: invalid input file path");
        }
        try {
            outputPath = Path.of(input);
        } catch (InvalidPathException e) {
            throw new WalkException("Error: invalid output file path");
        }
        if (Files.notExists(inputPath)) {
            throw new WalkException("Error: input file not exists");
        }
        if (outputPath.getParent() != null && Files.notExists(outputPath.getParent()))  {
            try {
                Files.createDirectories(outputPath.getParent());
            } catch (IOException e) {
                throw new WalkException("Error: can't create directories before output file");
            }
        }
        try (
                BufferedReader reader = new BufferedReader(new FileReader(input));
                BufferedWriter writer = new BufferedWriter(new FileWriter(output))
        ) {
            String path;
            while ((path = reader.readLine()) != null) {
                try {
                    byte[] bytes = Files.readAllBytes(Path.of(path));
                    String hash = hash(bytes);
                    writeHash(writer, path, hash);
                } catch (Exception e) {
                    writeHash(writer, path, String.join("", Collections.nCopies(40, "0")));
                }
            }
        } catch (SecurityException | IOException e) {
            throw new WalkException("Error: can't open input/output file");
        }
    }

    private static void writeHash(BufferedWriter writer, String path, String hash) {
        try {
            writer.write(hash + " " + path + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String hash(final byte[] bytes) {
        try {
            final byte[] hash = MessageDigest.getInstance("SHA-1").digest(bytes);
            return String.format("%0" + (hash.length << 1) + "x", new BigInteger(1, hash));
        } catch (final NoSuchAlgorithmException e) {
            throw new AssertionError("Digest error: " + e.getMessage(), e);
        }
    }
}
