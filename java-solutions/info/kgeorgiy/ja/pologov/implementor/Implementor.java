package info.kgeorgiy.ja.pologov.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Implementor implements Impler {
    public Implementor() {
    }

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
            Implementor implementor = new Implementor();
            try {
                implementor.implement(Class.forName(args[0]), Paths.get(args[1]));
            } catch (ImplerException e) {
                System.err.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found");
            }
        }
    }

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (token == null || !token.isInterface() || Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Error: toke is not a interface");
        }

        Path path = Path.of(
                root + File.separator + token.getPackage().getName().replace(".", File.separator)
                        + File.separator + token.getSimpleName() + "Impl.java");
        // :fixed: getParentFile() can return null
        if (path.getParent() != null) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new ImplerException(e.toString());
            }
        }

        try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            bufferedWriter.write(generateCode(token));
        } catch (IOException e) {
            throw new ImplerException("Error: can't open class for writing");
        }
    }

    private String generateCode(Class<?> token) {
        return String.join(
                System.lineSeparator(),
                generatePackage(token),
                generateClassDeclaration(token),
                generateMethods(token),
                "}"
        );
    }

    private String generateMethods(Class<?> token) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Method method : token.getMethods()) {
            stringBuilder.append(generateMethod(method));
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    private String generateMethod(Method method) {
        if (cannotBeImplemented(method)) {
            return "";
        }

        return String.join(
                " ",
                generateMethodDefinition(method),
                generateMethodDeclaration(method),
                "}"
        );
    }

    private String generateMethodDefinition(Method method) {
        // :fixed: private return type and method params
        return String.join(
                " ",
                Modifier.toString(validateModifies(method.getModifiers())),
                method.getReturnType().getCanonicalName(),
                method.getName() + "(" + getMethodsParams(method) + ")",
                // :fixed: add exceptions
                generateThrows(method),
                "{"
        );
    }

    private boolean cannotBeImplemented(Method method) {
        return Modifier.isPrivate(method.getModifiers()) || Modifier.isStatic(method.getModifiers()) ||
                method.isDefault();
    }

    private String generateThrows(Method method) {
        if (method.getExceptionTypes().length == 0) {
            return "";
        }
        return "throws " +
                Arrays.stream(method.getExceptionTypes())
                .map(Class::getCanonicalName)
                .collect(Collectors.joining(", "));
    }

    private int validateModifies(int modifiers) {
        return modifiers & ~Modifier.TRANSIENT & ~Modifier.ABSTRACT;
    }

    private String getMethodsParams(Method method) {
        return Arrays.stream(method.getParameters())
                .map(p -> p.getType().getCanonicalName() + " " + p.getName())
                .collect(Collectors.joining(", "));
    }

    private String generateMethodDeclaration(Method method) {
        return "return " + generateDefaultValue(method.getReturnType()) + ";";
    }

    private String generateDefaultValue(Class<?> returnType) {
        if (returnType.equals(void.class)) {
            return "";
        }
        if (returnType.equals(boolean.class)) {
            return "false";
        }
        if (returnType.isPrimitive()) {
            return "0";
        }
        return "null";
    }

    private String generateClassDeclaration(Class<?> token) {
        return String.join(" ", "public class", token.getSimpleName() + "Impl", "implements",
                token.getCanonicalName(), "{");
    }

    private String generatePackage(Class<?> token) {
        if (token.getPackage() != null) {
            return String.join(" ", "package", token.getPackageName(), ";");
        }
        return "";
    }
}

