import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Outputs several simple data structures in "pretty" JSON format where newlines
 * are used to separate elements and nested elements are indented.
 *
 *
 * @author Isaiah Jenkins
 * @author University of San Francisco
 * @version Fall 2019
 */
public class SimpleJsonWriter {
	/**
	 * Decimal formatter for scores.
	 */
	public static DecimalFormat FORMATTER = new DecimalFormat("0.00000000");

	/**
	 * Writes the elements as a pretty JSON array.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException exception for file error
	 */
	public static void asArray(Collection<Integer> elements, Writer writer, int level) throws IOException {
		Iterator<Integer> itr = elements.iterator();
		writer.write("[");

		if (!elements.isEmpty()) {
			writer.write("\n");
			indent(itr.next(), writer, level + 1);

			while (itr.hasNext()) {
				writer.write(",");
				indent("\n", writer, level + 1);
				indent(itr.next(), writer, level + 1);
			}
			writer.write("\n");
		}

		indent("]", writer, level);
		writer.flush();
	}

	/**
	 * Writes the elements as a pretty JSON array to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException exception for file error
	 *
	 * @see #asArray(Collection, Writer, int)
	 */
	public static void asArray(Collection<Integer> elements, Path path) throws IOException {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asArray(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON array.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asArray(Collection, Writer, int)
	 */
	public static String asArray(Collection<Integer> elements) {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try {
			StringWriter writer = new StringWriter();
			asArray(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a pretty JSON object.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException exception for file error
	 */
	public static void asObject(Map<String, Integer> elements, Writer writer, int level) throws IOException {
		Iterator<Entry<String, Integer>> itr = elements.entrySet().iterator();
		Entry<String, Integer> currElement = null;
		writer.write("{");
		writer.write("\n");

		if (itr.hasNext()) {
			currElement = itr.next();
			indent('"' + currElement.getKey() + '"' + ": " + currElement.getValue(), writer, level);
		}

		while (itr.hasNext()) {
			currElement = itr.next();
			writer.write(",");
			indent("\n", writer, level + 1);
			indent('"' + currElement.getKey() + '"' + ": " + currElement.getValue(), writer, level);
		}
    
		writer.write("\n");
		indent("}", writer, level);
		writer.flush();
	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException exception for file error
	 *
	 * @see #asObject(Map, Writer, int)
	 */
	public static void asObject(Map<String, Integer> elements, Path path) throws IOException {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asObject(Map, Writer, int)
	 */
	public static String asObject(Map<String, Integer> elements) {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try {
			StringWriter writer = new StringWriter();
			asObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the elements as a nested pretty JSON object. The generic notation used
	 * allows this method to be used for any type of map with any type of nested
	 * collection of integer objects.
	 *
	 * @param elements the elements to write
	 * @param writer   the writer to use
	 * @param level    the initial indent level
	 * @throws IOException exception for file error
	 */
	public static void asNestedObject(Map<String, ? extends Collection<Integer>> elements, Writer writer, int level)
			throws IOException {
		Iterator<String> itr = elements.keySet().iterator();
		String key = null;
		writer.write("{\n");
		indent(writer, 1);

		if (itr.hasNext()) {
			key = itr.next();
			quote(key, writer, 1);
			writer.write(": ");
			asArray(elements.get(key), writer, level);
		}

		while (itr.hasNext()) {
			key = itr.next();
			writer.write(",\n");
			quote(key, writer, 1);
			writer.write(": ");
			asArray(elements.get(key), writer, level);
		}

		writer.write("\n");
		indent(writer, 1);
		writer.write("}");
	}

	/**
	 * Writes the elements as a nested pretty JSON object to file.
	 *
	 * @param elements the elements to write
	 * @param path     the file path to use
	 * @throws IOException exception for file error
	 *
	 * @see #asNestedObject(Map, Writer, int)
	 */
	public static void asNestedObject(Map<String, ? extends Collection<Integer>> elements, Path path)
			throws IOException {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			asNestedObject(elements, writer, 0);
		}
	}

	/**
	 * Returns the elements as a nested pretty JSON object.
	 *
	 * @param elements the elements to use
	 * @return a {@link String} containing the elements in pretty JSON format
	 *
	 * @see #asNestedObject(Map, Writer, int)
	 */
	public static String asNestedObject(Map<String, ? extends Collection<Integer>> elements) {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		try {
			StringWriter writer = new StringWriter();
			asNestedObject(elements, writer, 0);
			return writer.toString();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Writes the invertedIndex as a nested pretty JSON object.
	 *
	 * @param invertedIndex the elements to write
	 * @param writer        the writer to use
	 * @param level         the initial indent level
	 * @throws IOException exception for file error
	 */
	public static void asInvertedIndex(TreeMap<String, TreeMap<String, TreeSet<Integer>>> invertedIndex, Writer writer,
			int level) throws IOException {
		Iterator<Entry<String, TreeMap<String, TreeSet<Integer>>>> itr = invertedIndex.entrySet().iterator();
		String stemmedWord = null;
		writer.write("{\n");
		indent(writer, 1);

		if (itr.hasNext()) {
			stemmedWord = itr.next().getKey();
			quote(stemmedWord, writer, 1);
			writer.write(": ");
			asNestedObject(invertedIndex.get(stemmedWord), writer, level);
		}

		while (itr.hasNext()) {
			stemmedWord = itr.next().getKey();
			writer.write(",\n");
			quote(stemmedWord, writer, 1);
			writer.write(": ");
			asNestedObject(invertedIndex.get(stemmedWord), writer, level);
		}

		writer.write("\n");
		indent(writer, 1);
		writer.write("}");
		writer.flush();
	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param invertedIndex the elements to write
	 * @param path          the file path to use
	 * @throws IOException exception for file error
	 *
	 * @see #asObject(Map, Writer, int)
	 */
	public static void asInvertedIndex(TreeMap<String, TreeMap<String, TreeSet<Integer>>> invertedIndex, Path path)
			throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			// StringWriter writer = new StringWriter();
			asInvertedIndex(invertedIndex, writer, 0);
		}

	}

	/**
	 * Writes the elements as a nested pretty JSON object.
	 *
	 * @param arrayList the elements to write
	 * @param writer    the writer to use
	 * @param level     the initial indent level
	 * @throws IOException exception for file error
	 */
	public static void asNestedQueries(List<InvertedIndex.QueryData> arrayList, Writer writer, int level)
			throws IOException {
		Iterator<InvertedIndex.QueryData> itr = arrayList.iterator();
		InvertedIndex.QueryData instance = null;
		writer.write("[\n");

		if (itr.hasNext()) {

			instance = itr.next();
			indent(writer, 2);
			writer.write("{\n");
			quote("where", writer, level + 3);
			writer.write(": ");
			quote(instance.getWhere(), writer);
			writer.write(",\n");
			quote("count", writer, level + 3);
			writer.write(": ");
			writer.write(Integer.toString(instance.getCount()));
			writer.write(",\n");
			quote("score", writer, level + 3);
			writer.write(": ");
			writer.write(FORMATTER.format(instance.getScore()));
			writer.write("\n");
			indent(writer, 2);
			writer.write("}");

		}

		while (itr.hasNext()) {
			instance = itr.next();
			writer.write(",\n");
			indent(writer, 2);

			writer.write("{\n");
			quote("where", writer, level + 3);
			writer.write(": ");
			quote(instance.getWhere(), writer);
			writer.write(",\n");
			quote("count", writer, level + 3);
			writer.write(": ");
			writer.write(Integer.toString(instance.getCount()));
			writer.write(",\n");
			quote("score", writer, level + 3);
			writer.write(": ");
			writer.write(FORMATTER.format(instance.getScore()));
			writer.write("\n");
			indent(writer, 2);
			writer.write("}");
		}

		indent(writer, 1);
		if (instance != null) {
			writer.write("\n");
			indent(writer, 1);
		}
		writer.write("]");
		writer.flush();
	}

	/**
	 * Writes the invertedIndex as a nested pretty JSON object.
	 *
	 * @param queriesAndResults the elements to write
	 * @param writer            the writer to use
	 * @param level             the initial indent level
	 * @throws IOException exception for file error
	 */

	public static void asQuery(TreeMap<String, List<InvertedIndex.QueryData>> queriesAndResults, Writer writer,
			int level)
			throws IOException {

		Iterator<Entry<String, List<InvertedIndex.QueryData>>> itr = queriesAndResults.entrySet().iterator();
		String stemmedWord = null;
		writer.write("{\n");
		indent(writer, 1);

		if (itr.hasNext()) {
			stemmedWord = itr.next().getKey();
			quote(stemmedWord, writer);
			writer.write(": ");
			asNestedQueries(queriesAndResults.get(stemmedWord), writer, level);
		}

		while (itr.hasNext()) {
			stemmedWord = itr.next().getKey();
			writer.write(",\n");
			quote(stemmedWord, writer, 1);
			writer.write(": ");
			asNestedQueries(queriesAndResults.get(stemmedWord), writer, level);
		}

		writer.write("\n");
		writer.write("}");
		writer.flush();
	}

	/**
	 * Writes the elements as a pretty JSON object to file.
	 *
	 * @param queriesAndResults the elements to write
	 * @param path              the file path to use
	 * @throws IOException exception for file error
	 *
	 * @see #asObject(Map, Writer, int)
	 */

	public static void asQuery(TreeMap<String, List<InvertedIndex.QueryData>> queriesAndResults, Path path)
			throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			// StringWriter writer = new StringWriter();
			asQuery(queriesAndResults, writer, 0);
		}

	}

	/**
	 * Writes the {@code \t} tab symbol by the number of times specified.
	 *
	 * @param writer the writer to use
	 * @param times  the number of times to write a tab symbol
	 * @throws IOException exception for file error
	 */
	public static void indent(Writer writer, int times) throws IOException {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		for (int i = 0; i < times; i++) {
			writer.write('\t');
		}
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException exception for file error
	 *
	 * @see #indent(String, Writer, int)
	 * @see #indent(Writer, int)
	 */
	public static void indent(Integer element, Writer writer, int times) throws IOException {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		indent(element.toString(), writer, times);
	}

	/**
	 * Indents and then writes the element.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException exception for file error
	 *
	 * @see #indent(Writer, int)
	 */
	public static void indent(String element, Writer writer, int times) throws IOException {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		indent(writer, times);
		writer.write(element);
	}

	/**
	 * Writes the element surrounded by {@code " "} quotation marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @throws IOException exception for file error
	 */
	public static void quote(String element, Writer writer) throws IOException {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		writer.write('"');
		writer.write(element);
		writer.write('"');
	}

	/**
	 * Indents and then writes the element surrounded by {@code " "} quotation
	 * marks.
	 *
	 * @param element the element to write
	 * @param writer  the writer to use
	 * @param times   the number of times to indent
	 * @throws IOException exception for file error
	 *
	 * @see #indent(Writer, int)
	 * @see #quote(String, Writer)
	 */
	public static void quote(String element, Writer writer, int times) throws IOException {
		// THIS CODE IS PROVIDED FOR YOU; DO NOT MODIFY
		indent(writer, times);
		quote(element, writer);
	}
}