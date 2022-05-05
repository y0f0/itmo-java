package info.kgeorgiy.ja.pologov.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloUDPServer implements HelloServer {
    private DatagramSocket socket;
    private ExecutorService pool;

    /**
     * Starts a new Hello server.
     * This method should return immediately.
     *
     * @param port    server port.
     * @param threads number of working threads.
     */
    @Override
    public void start(int port, int threads) {
        pool = Executors.newFixedThreadPool(threads);
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ignored) {
        }
        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    while (!socket.isClosed()) {
                        handler();
                    }
                } catch (IOException ignored) {
                }
            });
        }
    }

    private void handler() throws IOException {
        DatagramPacket receivePacket = new DatagramPacket(new byte[socket.getReceiveBufferSize()],
                socket.getReceiveBufferSize());
        socket.receive(receivePacket);
        String received = new String(receivePacket.getData(), receivePacket.getOffset(),
                receivePacket.getLength(), StandardCharsets.UTF_8);

        String sent = "Hello, " + received;
        DatagramPacket sendPacket = new DatagramPacket(sent.getBytes(StandardCharsets.UTF_8),
                sent.length(), receivePacket.getSocketAddress());
        socket.send(sendPacket);
    }

    /**
     * Stops server and deallocates all resources.
     */
    @Override
    public void close() {
        socket.close();
        Utils.shutdownAndAwaitTermination(pool);
    }

    private static final int DEFAULT_THREADS = 1;
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("error: args length is not correct");
        }
        assert args != null;
        // :fixed: в константы
        int port = DEFAULT_PORT;
        int threads = DEFAULT_THREADS;
        try {
            port = Integer.parseInt(args[0]);
            threads = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("error: args is not correct");
        }

        try (HelloUDPServer server = new HelloUDPServer()) {
            server.start(port, threads);
        }
    }
}
