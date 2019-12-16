import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Builds the Inverted Index.
 * 
 * @author isaiahjenkins
 */
public class InvertedIndexBuilder {
	/**
	 * Index to build index instance.
	 */
	private final InvertedIndex index;

	/**
	 * Final constant default to help create stemmer instances.
	 */
	public final static SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Constructor for index instance.
	 * 
	 * @param index to build invert
	 */
	public InvertedIndexBuilder(InvertedIndex index) {
		this.index = index;
	}

	/**
	 * Add files of index words to inverted index based on location
	 * 
	 * @param location the path to index
	 * @throws IOException exception for file error
	 */
	public void addFile(Path location) throws IOException {
		addFile(location, this.index);
	}
	/**
	 * Add files of index words to inverted index
	 * 
	 * @param location location for indexing
	 * @param index    inverted index instance
	 * @throws IOException exception for file error
	 */
	public static void addFile(Path location, InvertedIndex index) throws IOException {
		Stemmer stemmer = new SnowballStemmer(DEFAULT);
		int position = 1;
		try (BufferedReader reader = Files.newBufferedReader(location, StandardCharsets.UTF_8);) {
			String line = null;
			String locationString = location.toString();
			while ((line = reader.readLine()) != null) {
				for (String word : TextParser.parse(line)) {
					word = stemmer.stem(word).toString();
					index.add(word, locationString, position);
					position++;
				}
			}
		}
	}	

	/**
	 * Custom addFile to read through cleaned html stem and add to index
	 * 
	 * @param urlLocationString location for indexing
	 * @param html              of URL
	 * @param index             inverted index instance
	 * @throws IOException exception for file error
	 */
	public static void addFile(String urlLocationString, String html, InvertedIndex index) throws IOException {
		Stemmer stemmer = new SnowballStemmer(DEFAULT);
		int position = 1;

		for (String word : TextParser.parse(html)) {
			word = stemmer.stem(word).toString();
			index.add(word, urlLocationString, position);
			position++;
		}
	}

	/**
	 * Helper method to traverse through the directory and its subdirectories to
	 * retrieve the worded index.
	 * 
	 * @param start the path to traverse
	 * @throws IOException exception for file error
	 */
	private void traverseDirectoryForIndex(Path start) throws IOException {
		if (Files.isDirectory(start)) {
			try (DirectoryStream<Path> listing = Files.newDirectoryStream(start)) {
				for (Path path : listing) {
					traverseDirectoryForIndex(path);
				}
			}
		} else {
			if (isTextFile(start)) {
				addFile(start);
			}
		}
	}

	/**
	 * Traverses through the directory and its subdirectories, going through all the
	 * paths to retrieve the index words and positions.
	 * 
	 * @param start the initial path to traverse
	 * @throws IOException exception for file error
	 */
	public void build(Path start) throws IOException {
		traverseDirectoryForIndex(start);
	}

	/**
	 * Helper method to check if file is a text file.
	 * 
	 * 
	 * @param path to check if it is a text file.
	 * @return {@code true} if the file is a text file.
	 */
	public static boolean isTextFile(Path path) {
		String name = path.toString().toLowerCase();
		return Files.isRegularFile(path) && (name.endsWith(".txt") || name.endsWith(".text"));
	}
}