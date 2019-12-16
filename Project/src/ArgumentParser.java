import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses and stores command-line arguments into simple key, value pairs.
 *
 * @author Isaiah Jenkins
 * @author University of San Francisco
 * @version Fall 2019
 */
public class ArgumentParser {

	/**
	 * Stores command-line arguments in key = value pairs.
	 */
	private Map<String, String> map;

	/**
	 * Initializes this argument map.
	 */
	public ArgumentParser() {
		map = new HashMap<String, String>();
	}

	/**
	 * Initializes this argument map and then parsers the arguments into flag/value
	 * pairs where possible. Some flags may not have associated values. If a flag is
	 * repeated, its value is overwritten.
	 *
	 * @param args the command line arguments to parse
	 */
	public ArgumentParser(String[] args) {
		// DO NOT MODIFY; THIS METHOD IS PROVIDED FOR YOU
		this();
		parse(args);
	}

	/**
	 * Parses the arguments into flag/value pairs where possible. Some flags may not
	 * have associated values. If a flag is repeated, its value is overwritten.
	 *
	 * @param args the command line arguments to parse
	 */
	public void parse(String[] args) {
		String flagArg = "";
		String valueArg = "";

		for (int i = 0; i < args.length; i++) {
			flagArg = args[i];
			if (isFlag(flagArg)) {
				map.put(flagArg, null);
			}
			if (i < args.length - 1) {
				valueArg = args[i + 1];
				if (isValue(valueArg)) {
					map.put(flagArg, valueArg);
				}
			}
		}
	}

	/**
	 * Checks if map is empty.
	 *
	 * @return {@code true} if the map size is than one.
	 */
	public boolean isEmpty() {
		return map.size() < 1 ? true : false;
	}

	/**
	 * Determines whether the argument is a flag. Flags start with a dash "-"
	 * character, followed by at least one other character.
	 *
	 * @param arg the argument to test if its a flag
	 * @return {@code true} if the argument is a flag
	 *
	 * @see String#startsWith(String)
	 * @see String#length()
	 */
	public static boolean isFlag(String arg) {
		return arg != null && arg.length() > 1 && arg.startsWith("-");
	}

	/**
	 * Determines whether the argument is a value. Values do not start with a dash
	 * "-" character, and must consist of at least one character.
	 *
	 * @param arg the argument to test if its a value
	 * @return {@code true} if the argument is a value
	 *
	 * @see String#startsWith(String)
	 * @see String#length()
	 */
	public static boolean isValue(String arg) {
		return arg != null && arg.length() > 0 && !arg.equals("-") && !isFlag(arg);
	}

	/**
	 * Returns the number of unique flags.
	 *
	 * @return number of unique flags
	 */
	public int numFlags() {
		return map.size();
	}

	/**
	 * Determines whether the specified flag exists.
	 *
	 * @param flag the flag to search for
	 * @return {@code true} if the flag exists
	 */
	public boolean hasFlag(String flag) {
		return map.containsKey(flag);
	}

	/**
	 * Determines whether the specified flag is mapped to a non-null value.
	 *
	 * @param flag the flag to search for
	 * @return {@code true} if the flag is mapped to a non-null value
	 */
	public boolean hasValue(String flag) {
		return map.get(flag) != null ? true : false;
	}

	/**
	 * Returns the value to which the specified flag is mapped as a {@link String},
	 * or null if there is no mapping for the flag.
	 *
	 * @param flag the flag whose associated value is to be returned
	 * @return the value to which the specified flag is mapped, or {@code null} if
	 *         there is no mapping for the flag
	 */
	public String getString(String flag) {
		if (hasValue(flag)) {
			return map.get(flag);
		}
		return null;
	}

	/**
	 * Returns the value to which the specified flag is mapped as a {@link String},
	 * or the default value if there is no mapping for the flag.
	 *
	 * @param flag         the flag whose associated value is to be returned
	 * @param defaultValue the default value to return if there is no mapping for
	 *                     the flag
	 * @return the value to which the specified flag is mapped, or the default value
	 *         if there is no mapping for the flag
	 */
	public String getString(String flag, String defaultValue) {
		// DO NOT MODIFY; THIS METHOD IS PROVIDED FOR YOU
		String value = getString(flag);
		return value == null ? defaultValue : value;
	}

	/**
	 * Returns the value to which the specified flag is mapped as a {@link Path}, or
	 * {@code null} if the flag does not exist or has a null value.
	 *
	 * @param flag the flag whose associated value is to be returned
	 * @return the value to which the specified flag is mapped, or {@code null} if
	 *         the flag does not exist or has a null value
	 *
	 * @see Path#of(String, String...)
	 */
	public Path getPath(String flag) {
		if (hasValue(flag)) {
			return Path.of(getString(flag));
		}
		return null;
	}

	/**
	 * Returns the value the specified flag is mapped as a {@link Path}, or the
	 * default value if the flag does not exist or has a null value.
	 *
	 * @param flag         the flag whose associated value will be returned
	 * @param defaultValue the default value to return if there is no valid mapping
	 *                     for the flag
	 * @return the value the specified flag is mapped as a {@link Path}, or the
	 *         default value if there is no valid mapping for the flag
	 */
	public Path getPath(String flag, Path defaultValue) {
		// DO NOT MODIFY; THIS METHOD IS PROVIDED FOR YOU
		Path value = getPath(flag);
		return value == null ? defaultValue : value;
	}

	/**
	 * Returns toString of args map
	 * 
	 * @return toString of args map
	 */
	@Override
	public String toString() {
		// DO NOT MODIFY; THIS METHOD IS PROVIDED FOR YOU
		return map.toString();
	}
  
}

