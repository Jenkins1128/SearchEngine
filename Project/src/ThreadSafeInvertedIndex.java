import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Stores the Inverted Index
 * 
 * @author isaiahjenkins
 */
public class ThreadSafeInvertedIndex extends InvertedIndex {

	/**
	 * Read write lock member
	 */
	private final ReadWriteLock lock;

	/**
	 * Constructor for Threadsafe Inverted Index class.
	 */
	public ThreadSafeInvertedIndex() {
		super();
		lock = new ReadWriteLock();
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
	@Override
	public void add(String word, String location, int position) throws IOException {
		lock.writeLock().lock();
		try {
			super.add(word, location, position);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * Returns unmodifiable keySet of words in inverted index.
	 * 
	 * @return unmodifiable keySet of words in inverted index.
	 */
	@Override
	public Set<String> getWords() {
		lock.readLock().lock();
		try {
			return super.getWords();
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Returns unmodifiable positions of words in file.
	 * 
	 * @param word stemmed word for index.
	 * @return unmodifiable keySet of locations for stemmed word.
	 */
	@Override
	public Set<String> getLocations(String word) {
		lock.readLock().lock();
		try {
			return super.getLocations(word);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Returns unmodifiable positions of the partial words in file.
	 * 
	 * @param stemmedWord stemmed word for index.
	 * @return unmodifiable keySet of locations for stemmed word.
	 */
	@Override
	public Set<String> getLocationsPartial(String stemmedWord) {
		lock.readLock().lock();
		try {
			return super.getLocationsPartial(stemmedWord);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Returns unmodifiable positions of words in file.
	 * 
	 * 
	 * @param stemmedWord stemmed word for index.
	 * @param location    current location for word.
	 * @return unmodifiable set of positions of words in file.
	 */
	@Override
	public Set<Integer> getPositions(String stemmedWord, String location) {
		lock.readLock().lock();
		try {
			return super.getPositions(stemmedWord, location);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Returns true if stemmed word is in index.
	 * 
	 * @param word stemmed word for index.
	 * @return true if stemmed word is in index.
	 */
	@Override
	public boolean contains(String word) {
		lock.readLock().lock();
		try {
			return super.contains(word);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Returns true if stemmed word and location is in index.
	 * 
	 * @param word     stemmed word for index.
	 * @param location current location for stemmed word.
	 * @return true if stemmed word and location is in index.
	 */
	@Override
	public boolean contains(String word, String location) {
		lock.readLock().lock();
		try {
			return super.contains(word, location);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Returns true if stemmed word, location, and position is in index.
	 * 
	 * @param word     stemmed word for index.
	 * @param location current location for stemmed word.
	 * @param position of stemmed word in location.
	 * @return true if stemmed word, location, and position is in index.
	 */
	@Override
	public boolean contains(String word, String location, int position) {
		lock.readLock().lock();
		try {
			return super.contains(word, location, position);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Returns unmodifiable int of counts for a file.
	 * 
	 * @param file current file
	 * @return unmodifiable int of counts for a file.
	 */
	@Override
	public int getCounts(String file) {
		lock.readLock().lock();
		try {
			return super.getCounts(file);
		} finally {
			lock.readLock().unlock();
		}
	}

	/*
	 * Returns toString output of inverted index.
	 * 
	 * @return invertedIndex toString output of inverted index.
	 */
	@Override
	public String toString() {
		lock.readLock().lock();
		try {
			return super.toString();
		} finally {
			lock.readLock().unlock();
		}

	}

	/**
	 * Creates Json for the inverted index.
	 * 
	 * @param valueOfFlagIndex the path for index
	 * @throws IOException error for file
	 */
	public void createPrettyJson(Path valueOfFlagIndex) throws IOException {
		lock.readLock().lock();
		try {
			super.createPrettyJson(valueOfFlagIndex);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Creates Json for words counts.
	 * 
	 * @param valueOfFlagCounts the path for counts
	 * @throws IOException error for file
	 */
	public void createCountsJson(Path valueOfFlagCounts) throws IOException {
		lock.readLock().lock();
		try {
			super.createCountsJson(valueOfFlagCounts);
		} finally {
			lock.readLock().unlock();
		}
	}
	
	/**
	 * Partial search for index by line
	 * 
	 * @param queries stemmedLines as list
	 * @return list of query data for partial search
	 */
	@Override
	public List<QueryData> partialSearch(Collection<String> queries) {
		lock.readLock().lock();
		try {
			return super.partialSearch(queries);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Exact search for index by line
	 * 
	 * @param queries stemmedLines as list
	 * @return list of query data for exact search
	 */
	@Override
	public List<QueryData> exactSearch(Collection<String> queries) {
		lock.readLock().lock();
		try {
			return super.exactSearch(queries);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * Add all method to add all the positions and counts to inverted index.
	 * 
	 * @param localIndex local index when used in multithreaded builder index
	 */
	@Override
	public void addAll(InvertedIndex localIndex) {
		lock.writeLock().lock();
		try {
			super.addAll(localIndex);
		} finally {
			lock.writeLock().unlock();
		}
	}
}