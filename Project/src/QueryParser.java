import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Class that builds queries
 * 
 * @author isaiahjenkins
 */
public class QueryParser implements QueryParserInterface {

	/**
	 * Queries and results of inverted index
	 */
	private final TreeMap<String, List<InvertedIndex.QueryData>> queriesAndResults;

	/**
	 * The inverted index.
	 */
	private final InvertedIndex index;

	/**
	 * Final constant default to help create stemmer instances.
	 */
	public final static SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Constructor for query parser.
	 * 
	 * @param theIndex the inverted index
	 */
	public QueryParser(InvertedIndex theIndex) {
		queriesAndResults = new TreeMap<>();
		index = theIndex;
	}

	/**
	 * Builds query by line.
	 * 
	 * @param line      current line in file
	 * @param exactFlag boolean for exact or partial flag
	 */
	public void buildQuery(String line, boolean exactFlag) {
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

		if (queriesAndResults.containsKey(joined)) {
			return;
		}

		List<InvertedIndex.QueryData> queryData = index.search(queries, exactFlag);
		queriesAndResults.put(joined, queryData);
	}

	/**
	 * Creates json for words counts
	 * 
	 * @param output the path for counts
	 * @throws IOException exception for file error
	 */
	public void createQueryJson(Path output) throws IOException {
		SimpleJsonWriter.asQuery(queriesAndResults, output);
	}
}