package info.kgeorgiy.ja.pologov.i18;

public class Main {
    public static void main(String[] args) {
        if (args == null || args.length != 4) {
            System.err.println("Error: args length is not correct");
        }
        assert args != null;
        String inputLocal = args[0];
        String outputLocal = args[1];
        String inputPath = args[2];
        String outputPath = args[3];
        
    }
}
