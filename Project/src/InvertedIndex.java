import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Stores the Inverted Index
 * 
 * @author isaiahjenkins
 */
public class InvertedIndex {
	/**
	 * Tree map for inverted index.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> invertedIndex;

	/**
	 * Tree map for files and counts.
	 */
	private final TreeMap<String, Integer> filesAndCounts;

	/**
	 * Constructor for Inverted Index class.
	 */
	public InvertedIndex() {
		invertedIndex = new TreeMap<>();
		filesAndCounts = new TreeMap<>();
	}

	/**
	 * Helper method to traverse through the directory and its subdirectories to
	 * retrieve the worded index.
	 * 
	 * @param word     stemmed word
	 * @param location current file
	 * @param position current word position in file

	 * @throws IOException error for file
	 */
	public void add(String word, String location, int position) throws IOException {
		// build index
		invertedIndex.putIfAbsent(word, new TreeMap<String, TreeSet<Integer>>());
		invertedIndex.get(word).putIfAbsent(location, new TreeSet<Integer>());
		boolean success = invertedIndex.get(word).get(location).add(position);

		// build counts, if position was added, increase count
		if (success) {
			int currentCountOfFile = filesAndCounts.getOrDefault(location, 0);
			filesAndCounts.put(location, currentCountOfFile + 1);
		}
	}

	/**
	 * Add all method to add all the positions and counts to inverted index.
	 * 
	 * @param localIndex local index when used in multithreaded builder index
	 */
	public void addAll(InvertedIndex localIndex) {
		// merge positions
		for (String word : localIndex.invertedIndex.keySet()) {
			if (invertedIndex.containsKey(word)) {
				for (String location : localIndex.invertedIndex.get(word).keySet()) {
					if (invertedIndex.get(word).containsKey(location)) {
						invertedIndex.get(word).get(location).addAll(localIndex.invertedIndex.get(word).get(location));
					} else {
						invertedIndex.get(word).put(location, localIndex.invertedIndex.get(word).get(location));
					}
				}
			} else {
				invertedIndex.put(word, localIndex.invertedIndex.get(word));
			}
		}

		// merge counts
		for (String location : localIndex.filesAndCounts.keySet()) {
			int currentCount = filesAndCounts.getOrDefault(location, 0);
			filesAndCounts.put(location, currentCount + localIndex.filesAndCounts.get(location));
		}
	}

	/**
	 * Returns unmodifiable keySet of words in inverted index.
	 * 
	 * @return unmodifiable keySet of words in inverted index.
	 */
	public Set<String> getWords() {
		return Collections.unmodifiableSet(invertedIndex.keySet());
	}

	/**
	 * Returns unmodifiable positions of words in file.
	 * 
	 * @param word stemmed word for index.
	 * @return unmodifiable keySet of locations for stemmed word.
	 */
	public Set<String> getLocations(String word) {
		if (contains(word)) {
			return Collections.unmodifiableSet(invertedIndex.get(word).keySet());
		}
		return Collections.emptySet();
	}

	/**
	 * Returns unmodifiable positions of the partial words in file.
	 * 
	 * @param stemmedWord stemmed word for index.
	 * @return unmodifiable keySet of locations for stemmed word.
	 */
	public Set<String> getLocationsPartial(String stemmedWord) {
		Set<String> allFiles = new TreeSet<>();
		for (String word : invertedIndex.keySet()) {
			if (word.startsWith(stemmedWord)) {
				if (contains(word)) {
					allFiles.addAll(invertedIndex.get(word).keySet());
				}
			}
		}
		return Collections.unmodifiableSet(allFiles);
	}

	/**
	 * Returns unmodifiable positions of words in file.
	 * 
	 * @param stemmedWord stemmed word for index.
	 * @param location    current location for word.
	 * @return unmodifiable set of positions of words in file.
	 */
	public Set<Integer> getPositions(String stemmedWord, String location) {
		if (contains(stemmedWord, location)) {
			return Collections.unmodifiableSet(invertedIndex.get(stemmedWord).get(location));
		}
		return Collections.emptySet();
	}

	/**
	 * Returns true if stemmed word is in index.
	 * 
	 * @param word stemmed word for index.
	 * @return true if stemmed word is in index.
	 */
	public boolean contains(String word) {
		return invertedIndex.containsKey(word);
	}

	/**
	 * Returns true if stemmed word and location is in index.
	 * 
	 * @param word     stemmed word for index.
	 * @param location current location for stemmed word.
	 * @return true if stemmed word and location is in index.
	 */
	public boolean contains(String word, String location) {
		if (contains(word)) {
			if (invertedIndex.get(word).containsKey(location)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if stemmed word, location, and position is in index.
	 * 
	 * @param word     stemmed word for index.
	 * @param location current location for stemmed word.
	 * @param position of stemmed word in location.
	 * @return true if stemmed word, location, and position is in index.
	 */
	public boolean contains(String word, String location, int position) {
		if (contains(word, location)) {
			if (invertedIndex.get(word).get(location).contains(position)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns unmodifiable int of counts for a file.
	 * 
	 * @param file current file
	 * @return unmodifiable int of counts for a file.
	 */
	public int getCounts(String file) {
		if (filesAndCounts.containsKey(file)) {
			return filesAndCounts.get(file);
		}
		return 0;
	}

	/**
	 * Creates Json for the inverted index.
	 * 
	 * @param valueOfFlagIndex the path for index
	 * @throws IOException error for file
	 */
	public void createPrettyJson(Path valueOfFlagIndex) throws IOException {
		SimpleJsonWriter.asInvertedIndex(invertedIndex, valueOfFlagIndex);
	}

	/**
	 * Creates Json for words counts.
	 * 
	 * @param valueOfFlagCounts the path for counts
	 * @throws IOException error for file
	 */
	public void createCountsJson(Path valueOfFlagCounts) throws IOException {
		SimpleJsonWriter.asObject(filesAndCounts, valueOfFlagCounts);
	}

	/*
	 * Returns toString output of inverted index.
	 * 
	 * @return invertedIndex toString output of inverted index.
	 */
	public String toString() {
		return invertedIndex.toString();
	}

	/**
	 * Partial search for index by line
	 * 
	 * @param queries stemmedLines as list
	 * @return list of query data for partial search
	 */
	public List<QueryData> partialSearch(Collection<String> queries) {
		ArrayList<QueryData> results = new ArrayList<>();
		Map<String, QueryData> lookup = new HashMap<>();

		for (String word : queries) {
			for (String wordStems : invertedIndex.tailMap(word).keySet()) {
				if (wordStems.startsWith(word)) {
					searchHelper(wordStems, results, lookup);
				} else {
					break;
				}
			}
		}

		Collections.sort(results);
		return results;
	}

	/**
	 * Exact search for index by line
	 * 
	 * @param queries stemmedLines as list
	 * @return list of query data for exact search
	 */
	public List<QueryData> exactSearch(Collection<String> queries) {
		ArrayList<QueryData> results = new ArrayList<>();
		Map<String, QueryData> lookup = new HashMap<>();

		for (String word : queries) {
			if (invertedIndex.containsKey(word)) {
				searchHelper(word, results, lookup);
			}
		}

		Collections.sort(results);
		return results;
	}

	/**
	 * Search helper for exact and partial search
	 * 
	 * @param word    current word or word stem in query
	 * @param results query data for current query
	 * @param lookup  map to update current query data
	 */
	private void searchHelper(String word, ArrayList<QueryData> results, Map<String, QueryData> lookup) {
		for (String location : invertedIndex.get(word).keySet()) {
			if (!lookup.containsKey(location)) {
				QueryData data = new QueryData(location);
				results.add(data);
				lookup.put(location, data);
			}

			lookup.get(location).update(word);
		}
	}

	/**
	 * Search for index by line
	 * 
	 * @param queries   stemmedLines as list
	 * @param exactFlag boolean for exact or partial query
	 * @return list of query data for exact or partial search
	 */
	public List<QueryData> search(Collection<String> queries, boolean exactFlag) {
		return exactFlag ? exactSearch(queries) : partialSearch(queries);
	}

	/**
	 * Query class for queried searches
	 */
	public class QueryData implements Comparable<QueryData> {

		/**
		 * location of queried search
		 */
		private final String where;
		/**
		 * count of queried search
		 */
		private int count;
		/**
		 * score of queried search
		 */
		private double score;

		/**
		 * Constructor for query class
		 * 
		 * @param location file to query
		 */
		public QueryData(String location) {
			this.count = 0;
			this.score = 0.00;
			this.where = location;
		}

		/**
		 * Return location of query search
		 * 
		 * @return location of query search
		 */
		public String getWhere() {
			return this.where;
		}

		/**
		 * Returns total matches of total matches
		 * 
		 * @return totals matches
		 */
		public int getCount() {
			return this.count;
		}

		/**
		 * Gets score
		 * 
		 * @return score
		 */
		public double getScore() {
			return this.score;
		}

		/**
		 * Updates the query data for count and score
		 * 
		 * @param word current word to query
		 */
		private void update(String word) {
			this.count += invertedIndex.get(word).get(where).size();
			this.score = (double) this.count / filesAndCounts.get(where);
		}

		/**
		 * Compare to function that compares and orders query data.
		 * 
		 * @param other compares values of query class
		 * @return compare value to order data
		 */
		@Override
		public int compareTo(QueryData other) {
			int comparedScore = Double.compare(getScore(), other.getScore());
			int comparedCount = Integer.compare(getCount(), other.getCount());
			int comparedWhere = getWhere().compareTo(other.getWhere());

			if (comparedScore != 0) {
				if (comparedScore > 0) {
					return -1;
				} else if (comparedScore < 0) {
					return 1;
				}

			} else {
				if (comparedCount != 0) {
					if (comparedCount > 0) {
						return -1;
					} else if (comparedCount < 0) {
						return 1;
					}
				}
				return comparedWhere;
			}
			return comparedWhere;
		}
	}
}