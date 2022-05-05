package info.kgeorgiy.ja.pologov.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HelloUDPClient implements HelloClient {
    /**
     * Entry point of class.
     * Using the command line, it can run client with arguments.
     *
     * @param args array of [host, port, prefix, thread, requests] @see HelloUDPClient#run(String, int, String, int, int)
     */
    public static void main(String[] args) {
        if (args == null || args.length != 5) {
            System.err.println("error: incorrect arguments count");
        }
        assert args != null;
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String prefix = args[2];
        int threads = Integer.parseInt(args[3]);
        int requests = Integer.parseInt(args[4]);
        new HelloUDPClient().run(host, port, prefix, threads, requests);
    }

    /**
     * Runs Hello client.
     * This method should return when all requests completed.
     *
     * @param host     server host
     * @param port     server port
     * @param prefix   request prefix
     * @param threads  number of request threads
     * @param requests number of requests per thread.
     */
    @Override
    public void run(String host, int port, String prefix, int threads, int requests) {
        ExecutorService requestsPool = Executors.newFixedThreadPool(threads);
        InetSocketAddress address = new InetSocketAddress(host, port);

        for (int id = 0; id < threads; id++) {
            final int threadId = id;
            requestsPool.submit(() -> {
                try (DatagramSocket socket = new DatagramSocket()) {
                    for (int request = 0; request < requests; request++) {
                        handler(prefix, address, threadId, socket, request);
                    }
                } catch (SocketException e) {
                    System.err.println("error: creating or accessing a Socket");
                }
            });
        }
        shutdownAndAwaitTermination(requestsPool);
    }

    private void handler(String prefix, InetSocketAddress address, int threadId, DatagramSocket socket, int request)
            throws SocketException {
        String sent = prefix + threadId + "_" + request;

        DatagramPacket receivePacket = new DatagramPacket(new byte[socket.getReceiveBufferSize()],
                socket.getReceiveBufferSize(), address);
        DatagramPacket sendPacket = new DatagramPacket(sent.getBytes(StandardCharsets.UTF_8), sent.length(), address);

        socket.setSoTimeout(500);
        String received;
        do {
            try {
                socket.send(sendPacket);
                socket.receive(receivePacket);
            } catch (IOException ignored) {
                //ignored
            }
            received = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength(),
                    StandardCharsets.UTF_8);
        } while (!received.equals("Hello, " + sent));
    }

    /**
     * correct shutdown (with awaittermination)
     * from @see <a href="https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html">https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html</a>
     *
     * @param pool pool for shutdown
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
