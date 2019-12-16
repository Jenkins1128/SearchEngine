import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

/**
 * Class responsible for running this project based on the provided command-line
 * arguments. See the README for details.
 *
 * @author Isaiah Jenkins
 * @author University of San Francisco
 * @version Fall 2019
 */
public class Driver {

	/**
	 * Initializes the classes necessary based on the provided command-line
	 * arguments. This includes (but is not limited to) how to build or search an
	 * inverted index.
	 *
	 * @param args flag/value pairs used to start this program
	 */
	public static void main(String[] args) {
		// store initial start time
		Instant start = Instant.now();
		ArgumentParser argParser = new ArgumentParser(args);
		InvertedIndex index = null;
		InvertedIndexBuilder indexBuilder = null;
		QueryParserInterface queryParser = null;
		WorkQueue queue = null;
		WebCrawler crawler = null;
		HeaderServer server = null;
		
		// Build inverted index and queries by multithreading
		if (argParser.hasFlag("-threads") || argParser.hasFlag("-url") || argParser.hasFlag("-port")) {
			try {
				int numThreads = Integer.parseInt(argParser.getString("-threads", "5"));

				if (numThreads < 1) {
					numThreads = 5;
				}

				queue = new WorkQueue(numThreads);
				ThreadSafeInvertedIndex threadSafe = new ThreadSafeInvertedIndex();
				index = threadSafe;
				indexBuilder = new ThreadSafeInvertedIndexBuilder(threadSafe, queue);
				queryParser = new ThreadSafeQueryParser(threadSafe, queue);

				// create work queue to build inverted index from web pages and crawl
				if (argParser.hasFlag("-url")) {
					String seedURL = argParser.getString("-url");
					int limit = Integer.parseInt(argParser.getString("-limit", "50"));
					crawler = new WebCrawler(threadSafe, seedURL, limit, queue);
					crawler.build();
				}

				if (argParser.hasFlag("-port")) {
					int port = Integer.parseInt(argParser.getString("-port", "8080"));
					server = new HeaderServer(port, threadSafe);
					server.start();
				}

			} catch (NumberFormatException e) {
				System.out.println("Thread number not a number: " + argParser.getString("-threads"));
			} catch (IOException e) {
				System.out.println("Unable to build index from the path: " + e.toString());
			} catch (Exception e) {
				System.out.println("An error occurred.");
			}
		} else {
			index = new InvertedIndex();
			indexBuilder = new InvertedIndexBuilder(index);
			queryParser = new QueryParser(index);
		}

		// create work queue to build inverted index
		if (argParser.hasFlag("-path")) {
			Path path = argParser.getPath("-path");
			try {
				if (path != null) {
					indexBuilder.build(path);
				}
			} catch (IOException | NullPointerException e) {
				System.out.println("Unable to build index from the path: " + path.toString());
			}
		}
    
		// output index
		if (argParser.hasFlag("-index")) {
			Path output = argParser.getPath("-index", Path.of("index.json"));

			try {
				index.createPrettyJson(output);
			} catch (IOException | NullPointerException e) {
				System.out.println("Unable to create json from the path: " + output.toString());
			}
		}

		// output counts
		if (argParser.hasFlag("-counts")) {
			Path output = argParser.getPath("-counts", Path.of("counts.json"));

			try {
				index.createCountsJson(output);
			} catch (IOException | NullPointerException e) {
				System.out.println("Unable to create json from the path: " + output.toString());
			}
		}

		// create work queue to build queues
		if (argParser.hasFlag("-query")) {
			Path queryPath = argParser.getPath("-query");
			boolean exactFlag = argParser.hasFlag("-exact");

			try {
				if (queryPath != null) {
					queryParser.buildQuery(queryPath, exactFlag);
				}
			} catch (IOException | NullPointerException e) {
				System.out.println("Unable to build query from the path: " + queryPath.toString());
			}
		}

		// creates json for exact or partial queries
		if (argParser.hasFlag("-results")) {
			Path output = argParser.getPath("-results", Path.of("results.json"));
			try {
				queryParser.createQueryJson(output);
			} catch (IOException | NullPointerException e) {
				System.out.println("Unable to create json from the path: " + output.toString());
			}
		}

		if (queue != null) {
			queue.shutdown();
		}

		// calculate time elapsed and output
		Duration elapsed = Duration.between(start, Instant.now());
		double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
		System.out.printf("Elapsed: %f seconds%n", seconds);
	}
}