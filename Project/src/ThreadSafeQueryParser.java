import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Builds the Inverted Index.
 * 
 * @author isaiahjenkins
 */
public class ThreadSafeQueryParser implements QueryParserInterface {

	/**
	 * The inverted index.
	 */
	private final ThreadSafeInvertedIndex index;

	/**
	 * WorkQueue member for multithreading
	 */
	private final WorkQueue tasks;
	/**
	 * Queries and results of inverted index
	 */
	private final TreeMap<String, List<InvertedIndex.QueryData>> queriesAndResults;

	/**
	 * Final constant default to help create stemmer instances.
	 */
	public final static SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Constructor for index instance.
	 * 
	 * @param index to build invert
	 * @param queue work queue for thread
	 */
	public ThreadSafeQueryParser(ThreadSafeInvertedIndex index, WorkQueue queue) {
		this.index = index;
		this.tasks = queue;
		queriesAndResults = new TreeMap<>();
	}
	
	/**
	 * Creates a work queue for building inverted index.
	 * 
	 * @param start     passed path for index
	 * @param exactFlag determines whether query is exact or partial
	 * @throws IOException error for file
	 */
	@Override
	public void buildQuery(Path start, boolean exactFlag) throws IOException {
		QueryParserInterface.super.buildQuery(start, exactFlag);
		tasks.finish();
	}

		/**
	 * Creates the first task and gives it to the work queue.
	 * 
	 * @param line      current line of file
	 * @param exactFlag flag for whether this is a exact or partial query
	 */
	@Override
	public void buildQuery(String line, boolean exactFlag) throws IOException {
		tasks.execute(new AddQueriesTask(line, exactFlag));
	}

	/**
	 * The non-static task class for building exact queries.
	 */
	private class AddQueriesTask implements Runnable {
		/** Passed path for exact query */
		private String line;
		/** boolean to determine whether query is exact or partial */
		private boolean exactFlag;

		/**
		 * Initializes this task.
		 * 
		 * @param line      current line in file
		 * @param exactFlag flag for whether query is exact or partial
		 */
		public AddQueriesTask(String line, boolean exactFlag) {
			this.line = line;
			this.exactFlag = exactFlag;
		}

		@Override
		public void run() {
			Stemmer stemmer = new SnowballStemmer(DEFAULT);

			TreeSet<String> queries = new TreeSet<>();

			for (String word : TextParser.parse(line)) {
				word = stemmer.stem(word).toString();
				queries.add(word);
			}

			if (queries.isEmpty()) {
				return;
			}

			String joined = String.join(" ", queries);

			synchronized (queriesAndResults) {
				if (queriesAndResults.containsKey(joined)) {
					return;
				}
			}

			List<InvertedIndex.QueryData> queryData = index.search(queries, exactFlag);

			synchronized (queriesAndResults) {
				queriesAndResults.put(joined, queryData);
			}
		}
	}

	/**
	 * Creates json for words counts
	 * 
	 * @param output the path for counts
	 * @throws IOException exception for file error
	 */
	@Override
	public void createQueryJson(Path output) throws IOException {
		SimpleJsonWriter.asQuery(queriesAndResults, output);
	}
}