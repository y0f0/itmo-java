package info.kgeorgiy.ja.pologov.walk;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Formatter;

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
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                try (InputStream inputStream = new DigestInputStream(new FileInputStream(path), digest)) {
                    byte[] bytes = new byte[1024];
                    while (inputStream.read(bytes) > 0);
                    //:fixed: Exception
                    writeHash(writer, path, bytesToHexString(digest.digest()));
                } catch (IOException e) {
                    writeHash(writer, path, "0000000000000000000000000000000000000000");
                }
            }
        } catch (SecurityException | IOException | NoSuchAlgorithmException e) {
            throw new WalkException("Error: can't open input/output file");
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int value = b & 0xFF;
            if (value < 16) {
                // if value less than 16, then it's hex String will be only
                // one character, so we need to append a character of '0'
                sb.append("0");
            }
            sb.append(Integer.toHexString(value));
        }
        return sb.toString();
    }

    private static void writeHash(BufferedWriter writer, String path, String hash) throws WalkException {
        try {
            writer.write(hash + " " + path);
            writer.newLine();
        } catch (IOException e) {
            // :NOTE: Описать, что случилось
            throw new WalkException("can't write output file");
        }
    }
}
