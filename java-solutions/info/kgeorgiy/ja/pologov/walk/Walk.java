package info.kgeorgiy.ja.pologov.walk;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Walk {
    public static void main(final String[] args) {
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
            } catch (final Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static void crawlFiles(final String input, final String output) throws WalkException {
        // :NOTE: Функция
        final Path inputPath;
        try {
            inputPath = Path.of(input);
        } catch (final InvalidPathException e) {
            throw new WalkException("Error: invalid input file path");
        }

        final Path outputPath;
        try {
            outputPath = Path.of(output);
        } catch (final InvalidPathException e) {
            throw new WalkException("Error: invalid output file path");
        }

//        if (Files.notExists(inputPath)) {
//            throw new WalkException("Error: input file not exists");
//        }
        // :NOTE: getParent x3
        if (outputPath.getParent() != null && Files.notExists(outputPath.getParent()))  {
            try {
                Files.createDirectories(outputPath.getParent());
            } catch (final IOException e) {
                // :NOTE: Не повод
                throw new WalkException("Error: can't create directories before output file");
            }
        }

        // :NOTE: Кодировки
        try (
                final BufferedReader reader = Files.newBufferedReader(inputPath);
                final BufferedWriter writer = Files.newBufferedWriter(outputPath)
        ) {
            String pathStr;
            while ((pathStr = reader.readLine()) != null) {
                // :NOTE: Переиспользовать
                final MessageDigest digest = MessageDigest.getInstance("SHA-1");
                try (final InputStream inputStream = new DigestInputStream(Files.newInputStream(Path.of(pathStr)), digest)) {
                    // :NOTE: Переиспользовать
                    final byte[] bytes = new byte[1024];
                    while (inputStream.read(bytes) > 0);
                    //:fixed: Exception
                    writeHash(writer, pathStr, bytesToHexString(digest.digest()));
                } catch (final InvalidPathException | IOException e) {
                    writeHash(writer, pathStr, "0000000000000000000000000000000000000000");
                }
            }
        } catch (final WalkException | SecurityException | IOException | NoSuchAlgorithmException e) {
            // :NOTE: Что и когда произошло
            throw new WalkException("Error: can't open input/output file");
        }
    }

    public static String bytesToHexString(final byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (final byte b : bytes) {
            final int value = b & 0xFF;
            if (value < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(value));
        }
        return sb.toString();
    }

    private static void writeHash(final BufferedWriter writer, final String path, final String hash) throws WalkException {
        try {
            writer.write(hash + " " + path);
            writer.newLine();
        } catch (final IOException e) {
            // :fixed: Описать, что случилось
            throw new WalkException("can't write output file");
        }
    }
}
