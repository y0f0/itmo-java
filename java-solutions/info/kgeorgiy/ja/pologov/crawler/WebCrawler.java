package info.kgeorgiy.ja.pologov.crawler;

import info.kgeorgiy.ja.pologov.hello.HelloUDPClient;
import info.kgeorgiy.ja.pologov.hello.Utils;
import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Thread-safe WebCrawler class that recursively crawls sites.
 * <p>
 * Implementing {@link Crawler}
 *
 * @author Nikita Pologov (nikita.pologov1@gmail.com)
 * @see Crawler
 */
public class WebCrawler implements Crawler {
    private final Downloader downloader;
    private final ExecutorService downloadsPool;
    private final ExecutorService extractorsPool;

    /**
     * Creates two ExecutorService for downloads and extractors pages, also saves the downloader.
     * <p>
     * For parallelization, it is allowed to create up to downloads + extractors of auxiliary streams.
     *
     * @param downloader  allows you to download pages and extract links from them.
     * @param downloaders the maximum number of simultaneously downloaded pages.
     * @param extractors  the maximum number of pages from which links are extracted at the same time.
     * @param perHost     maximum number of pages loaded simultaneously from one host (for easy version: perHost >= downloaders)
     * @see Downloader
     */
    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.downloadsPool = Executors.newFixedThreadPool(downloaders);
        this.extractorsPool = Executors.newFixedThreadPool(extractors);
    }

    /**
     * Entry point of class.
     * Using the command line, it can group the run web crawling.
     *
     * @param args are arguments of program [url, depth, downloads, extractors, perHost].
     * @see WebCrawler#WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost)
     */
    public static void main(String[] args) {
        if (args == null || args.length < 1 || args.length > 5) {
            System.err.println("Error: invalid parameters count.");
            return;
        }
        String url = args[0];
        int depth = getIfExist(1, args);
        int downloads = getIfExist(2, args);
        int extractors = getIfExist(3, args);
        int perHost = getIfExist(4, args);
        Downloader cachingDownloader = null;
        try {
            cachingDownloader = new CachingDownloader();
        } catch (IOException e) {
            System.err.println("Error: can't create downloader " + e.getMessage());
        }
        try (WebCrawler webCrawler = new WebCrawler(cachingDownloader, downloads, extractors, perHost)) {
            webCrawler.download(url, depth).getDownloaded().forEach(System.out::println);
        }
    }

    /**
     * Validate main function parameter.
     *
     * @param i    index of parameter
     * @param args arguments from {@link WebCrawler#main(String[])}.
     * @return i if i parameter exist else 1 (default parameter).
     */
    private static int getIfExist(int i, String[] args) {
        if (i >= args.length || i < 0) {
            return 1;
        }
        return Integer.parseInt(args[i]);
    }

    /**
     * An internal class for recursive crawling of a site page.
     */
    class DownloaderLinksRecursive {
        private final Set<String> downloaded = Collections.synchronizedSet(new HashSet<>());
        private final Set<String> extracted = Collections.synchronizedSet(new HashSet<>());
        private final Map<String, IOException> errors = Collections.synchronizedMap(new HashMap<>());
        private final Queue<String> queueLinks = new ConcurrentLinkedQueue<>();

        /**
         * Downloads website up to specified depth.
         *
         * @param link  start <a href="http://tools.ietf.org/html/rfc3986">URL</a>.
         * @param depth download depth.
         * @return download result.
         * @see Result
         */
        public Result run(String link, int depth) {
            extracted.add(link);
            queueLinks.add(link);

            while (!queueLinks.isEmpty()) {
                int linksCount = queueLinks.size();

                Phaser phaser = new Phaser(1);
                for (int __ = 0; __ < linksCount; __++) {
                    downloadLink(depth, phaser);
                }
                phaser.arriveAndAwaitAdvance();

                depth--;
            }
            return new Result(new ArrayList<>(downloaded), errors);
        }

        /**
         * Downloads link from {@link Downloader} and run
         * {@link DownloaderLinksRecursive#extractLinks(Document, String, Phaser)} foreach links.
         *
         * @param depth  download depth.
         * @param phaser phaser for barrier. notNull
         */
        private void downloadLink(int depth, Phaser phaser) {
            phaser.register();
            downloadsPool.submit(() -> {
                String documentLink = queueLinks.poll();
                try {
                    Document document = downloader.download(documentLink);
                    downloaded.add(documentLink);
                    if (depth <= 1) {
                        return;
                    }
                    extractLinks(document, documentLink, phaser);
                } catch (IOException exception) {
                    errors.put(documentLink, exception);
                } finally {
                    phaser.arrive();
                }
            });
        }

        /**
         * Extract all links from document and add inner links in queue.
         *
         * @param document     for extracting.
         * @param documentLink document link (for saving errors).
         * @param phaser       phaser for barrier. notNull
         */
        private void extractLinks(Document document, String documentLink, Phaser phaser) {
            phaser.register();
            extractorsPool.submit(() -> {
                try {
                    document.extractLinks().stream()
                            .filter(extracted::add)
                            .forEach(queueLinks::add);
                } catch (IOException exception) {
                    errors.put(documentLink, exception);
                } finally {
                    phaser.arrive();
                }
            });
        }

    }

    /**
     * Downloads website up to specified depth.
     *
     * @param url   start <a href="http://tools.ietf.org/html/rfc3986">URL</a>.
     * @param depth download depth.
     * @return download result.
     */
    @Override
    public Result download(String url, int depth) {
        return new DownloaderLinksRecursive().run(url, depth);
    }

    /**
     * Closes this web-crawler, relinquishing any allocated resources.
     */
    @Override
    public void close() {
        shutdownAndAwaitTermination(downloadsPool);
        shutdownAndAwaitTermination(extractorsPool);
    }

    void shutdownAndAwaitTermination(ExecutorService pool) { //from javadoc
        Utils.shutdownAndAwaitTermination(pool);
    }
}
