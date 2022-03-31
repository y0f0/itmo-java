package info.kgeorgiy.ja.pologov.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * Class implementing {@link JarImpler} and {@link Impler}
 * Class can create a class and their jar archives
 *
 * @author Nikita Pologov (nikita.pologov1@gmail.com)
 * @see JarImpler
 * @see Impler
 */
public class Implementor implements JarImpler, Impler {
    /**
     * default constructor
     */
    public Implementor() {
    }

    /**
     * Entry point of class.
     * Using the command line, it can group the implementation of an interface into a specified path.
     * As well as a jar archive of this implementation.
     *
     * @param args is array of arguments
     *             it two arguments execute {@link Impler}
     *             it three arguments execute {@link JarImpler}
     *             else print in err errors
     */
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
                if (args.length == 2) {
                    implementor.implement(Class.forName(args[0]), Paths.get(args[1]));
                } else {
                    implementor.implementJar(Class.forName(args[1]), Paths.get(args[2]));
                }
            } catch (ImplerException e) {
                System.err.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                System.err.println("Class not found");
            }
        }
    }

    /**
     * Implements class by interface and path.
     * Generates class, which implements interface token. And writes that class into root path.
     *
     * @param token the interface to be implemented
     * @param root  the path in which the generated file should lie
     * @throws ImplerException if:
     *                         <ul>
     *                             <li>token is null</li>
     *                             <li>token is not interface</li>
     *                             <li>token is private interface</li>
     *                             <li>can't create output dir for root</li>
     *                             <li>can't open class for writing</li>
     *                         </ul>
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (token == null || !token.isInterface() || Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Error: toke is not a interface");
        }

        Path path = getPath(token, root, "Impl.java");
        // :fixed: getParentFile() can return null
        if (path.getParent() != null) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new ImplerException("Error: can not create output dirs", e);
            }
        }

        try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            bufferedWriter.write(generateCode(token));
        } catch (IOException e) {
            throw new ImplerException("Error: can't open class for writing");
        }
    }

    /**
     * Generate the path to the filename file passing through root implementing token.
     *
     * @param token    the class that needs to be implemented
     * @param root     the path in which this class should be
     * @param fileName the file name of that class with extension
     * @return the path in which the generated class lies
     */
    private Path getPath(Class<?> token, Path root, String fileName) {
        return Path.of(
                root + File.separator + token.getPackage().getName().replace(".", File.separator)
                        + File.separator + token.getSimpleName() + fileName);
    }

    /**
     * Generate code of token.
     *
     * @param token the class to implement
     * @return java code
     */
    private String generateCode(Class<?> token) {
        return String.join(
                System.lineSeparator(),
                generatePackage(token),
                generateClassDeclaration(token),
                generateMethods(token),
                "}"
        );
    }

    /**
     * Generate code of all methods.
     *
     * @param token the class whose methods need to be implemented
     * @return java code of all methods
     */
    private String generateMethods(Class<?> token) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Method method : token.getMethods()) {
            stringBuilder.append(generateMethod(method));
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }

    /**
     * Generate code of one method.
     *
     * @param method that method which is need to implement
     * @return java code of that method
     */
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

    /**
     * Generate code of method definition.
     *
     * @param method method to be defined
     * @return java code of method definition
     */
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

    /**
     * Checks if that method can not be implemented.
     *
     * @param method method being tested
     * @return true if this method can not be implemented
     */
    private boolean cannotBeImplemented(Method method) {
        return Modifier.isPrivate(method.getModifiers()) || Modifier.isStatic(method.getModifiers()) ||
                method.isDefault();
    }

    /**
     * Generate all exceptions which throws.
     *
     * @param method method that throws exceptions
     * @return if that method throw something generates java code
     * else returns ""
     */
    private String generateThrows(Method method) {
        if (method.getExceptionTypes().length == 0) {
            return "";
        }
        return "throws " +
                Arrays.stream(method.getExceptionTypes())
                        .map(Class::getCanonicalName)
                        .collect(Collectors.joining(", "));
    }

    /**
     * validate that modifier is not transient and abstract.
     *
     * @param modifiers given modifiers
     * @return good modifiers is not {@link Modifier#TRANSIENT} and {@link Modifier#ABSTRACT}
     */
    private int validateModifies(int modifiers) {
        return modifiers & ~Modifier.TRANSIENT & ~Modifier.ABSTRACT;
    }

    /**
     * Gets method parameters with canonical type and name.
     *
     * @param method the method to take parameters from
     * @return Generated string: type arg0, type arg1, etc
     */
    private String getMethodsParams(Method method) {
        return Arrays.stream(method.getParameters())
                .map(p -> p.getType().getCanonicalName() + " " + p.getName())
                .collect(Collectors.joining(", "));
    }

    /**
     * Generate method declaration.
     *
     * @param method which declaration need to generate
     * @return string of java code method declaration
     */
    private String generateMethodDeclaration(Method method) {
        return "return " + generateDefaultValue(method.getReturnType()) + ";";
    }

    /**
     * Generate default value of return type for methods.
     *
     * @param returnType type which need to get default method
     * @return string of default type of that return type
     */
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

    /**
     * Generate java code of class declaration.
     *
     * @param token the interface to use to generate a class declaration
     * @return string of class declaration which implement token interface
     */
    private String generateClassDeclaration(Class<?> token) {
        return String.join(" ", "public class", token.getSimpleName() + "Impl", "implements",
                token.getCanonicalName(), "{");
    }

    /**
     * Generate java code of package name.
     *
     * @param token the interface to use to generate a package name
     * @return string of that package name
     */
    private String generatePackage(Class<?> token) {
        if (token.getPackage() != null) {
            return String.join(" ", "package", token.getPackageName(), ";");
        }
        return "";
    }

    /**
     * Implements class by interface and path in jar file.
     * Generates a class that implements the interface token.
     * And will write this jar archive of this class to the root path.
     *
     * @param token   the interface to be implemented
     * @param jarFile the path in which the generated jar archive should lie
     * @throws ImplerException if:
     *                         <ul>
     *                             <li>token is null</li>
     *                             <li>token is not interface</li>
     *                             <li>token is private interface</li>
     *                             <li>can't compile jar archive</li>
     *                             <li>can't open class for writing or can't find path</li>
     *                         </ul>
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        if (token == null || !token.isInterface() || Modifier.isPrivate(token.getModifiers())) {
            throw new ImplerException("Error: toke is not a interface");
        }

        Path path = jarFile.getParent();
        if (jarFile.getParent() == null) {
            path = Path.of("");
        }
        implement(token, path);
        Path compilePath = null;
        try {
            compilePath = Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI());
        } catch (URISyntaxException e) {
            throw new ImplerException("Error: can not find compile path");
        }
        compile(token, compilePath, path);
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
            String p = token.getPackageName().replace('.', '/') + '/' + token.getSimpleName()
                    + "Impl.class";
            jos.putNextEntry(new ZipEntry(p));
            Files.copy(getPath(token, path, "Impl.class"), jos);
        } catch (IOException e) {
            throw new ImplerException("Error: can not create jar");
        }
    }

    /**
     * Compile jar file.
     *
     * @param token       the interface to be implemented
     * @param compilePath path where jar file will be lie
     * @param path        path where generate java class was lie
     * @throws ImplerException if
     */
    private void compile(Class<?> token, Path compilePath, Path path) throws ImplerException {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final String[] args = {"-cp", compilePath.toString(), getPath(token, path, "Impl.java").toString()};

        final int exitCode = compiler.run(null, null, null, args);
        if (exitCode != 0) {
            throw new ImplerException("Error: can not compiler class");
        }
    }
}

