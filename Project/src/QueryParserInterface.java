import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Interface for Query Parser
 * 
 * @author isaiahjenkins
 *
 */
interface QueryParserInterface {

	/**
	 * Traverses through the directory and its subdirectories and queries the files
	 * 
	 * @param start     the initial path to traverse
	 * @param exactFlag boolean for exact flag
	 * @throws IOException exception for file error
	 */
	public default void buildQuery(Path start, boolean exactFlag) throws IOException {
		try (BufferedReader reader = Files.newBufferedReader(start, StandardCharsets.UTF_8);) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				buildQuery(line, exactFlag);
			}
		}
	}

	/**
	 * Abstract build query method
	 * 
	 * @param line      current line
	 * @param exactFlag boolean for partial/exact queries
	 * @throws IOException error for file
	 */
	public abstract void buildQuery(String line, boolean exactFlag) throws IOException;

	/**
	 * Abstract query json method
	 * 
	 * @param output file for json output
	 * @throws IOException error for file
	 */
	public abstract void createQueryJson(Path output) throws IOException;
}