package info.kgeorgiy.ja.pologov.walk;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
            outputPath = Path.of(output);
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
                BufferedReader reader = Files.newBufferedReader(inputPath);
                BufferedWriter writer = Files.newBufferedWriter(outputPath)
        ) {
            String pathStr;
            while ((pathStr = reader.readLine()) != null) {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                Path path = null;
                try {
                    path = Path.of(pathStr);
                } catch (InvalidPathException e) {
                    writeHash(writer, pathStr, "0000000000000000000000000000000000000000");
                }

                if (path != null) {
                    try (InputStream inputStream = new DigestInputStream(Files.newInputStream(path), digest)) {
                        byte[] bytes = new byte[1024];
                        while (inputStream.read(bytes) > 0) ;
                        //:fixed: Exception
                        writeHash(writer, pathStr, bytesToHexString(digest.digest()));
                    } catch (IOException e) {
                        writeHash(writer, pathStr, "0000000000000000000000000000000000000000");
                    }
                }
            }
        } catch (WalkException | SecurityException | IOException | NoSuchAlgorithmException e) {
            throw new WalkException("Error: can't open input/output file");
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int value = b & 0xFF;
            if (value < 16) {
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
