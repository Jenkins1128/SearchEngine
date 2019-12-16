import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses URL links from the anchor tags within HTML text.
 */
public class LinkParser {

	/**
	 * Removes the fragment component of a URL (if present), and properly encodes
	 * the query string (if necessary).
	 *
	 * @param url the url to clean
	 * @return cleaned url (or original url if any issues occurred)
	 */
	public static URL clean(URL url) {
		try {
			return new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
					url.getQuery(), null).toURL();
		} catch (MalformedURLException | URISyntaxException e) {
			return url;
		}
	}

	/**
	 * Returns a list of all the HTTP(S) links found in the href attribute of the
	 * anchor tags in the provided HTML. The links will be converted to absolute
	 * using the base URL and cleaned (removing fragments and encoding special
	 * characters as necessary).
	 *
	 * @param base the base url used to convert relative links to absolute3
	 * @param html the raw html associated with the base url
	 * @return cleaned list of all http(s) links in the order they were found
	 */
	public static ArrayList<URL> listLinks(URL base, String html) {
		ArrayList<URL> links = new ArrayList<URL>();
		URL absolute = null;
		String u = null;

		try {
			Pattern pattern = Pattern.compile("(?i)<a(?:[^<>]*?)href=\"([^\"]+?)\"");
			Matcher matcher;

			// remove white spaces from html
			html = String.join("", html.substring(0, 3), html.substring(2).replaceAll("\\s+", ""));
			matcher = pattern.matcher(html);

			while (matcher.find()) {
				// get everything within a tag and href link
				u = matcher.group();

				// manage cases and strip the quotes
				u = String.join("", u.substring(0, 8).toLowerCase(), u.substring(8));
				u = u.split("href=")[1];
				u = u.split("\"")[1];

				// check fragment
				if (u.contains("#")) {
					u = u.split("#")[0];
				}

				absolute = new URL(base, u);
				links.add(clean(absolute));
			}
		} catch (MalformedURLException | StringIndexOutOfBoundsException e) {

		}

		return links;
	}
}