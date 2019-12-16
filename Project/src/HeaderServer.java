import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import opennlp.tools.stemmer.Stemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer;

/**
 * Creates a web server to allow users to fetch HTTP headers for a URL.
 */
public class HeaderServer {

	/**
	 * server instance
	 */
	private final Server server;

	/** List to store queried urls */
	private static List<String> queries;

	/**
	 * Tree map for inverted index.
	 */
	private static ThreadSafeInvertedIndex index;

	/**
	 * Port number
	 */
	private int port;

	/**
	 * Final constant default to help create stemmer instances.
	 */
	public final static SnowballStemmer.ALGORITHM DEFAULT = SnowballStemmer.ALGORITHM.ENGLISH;

	/**
	 * Starts a Jetty server on port 8080, and maps /check requests to the
	 * {@link HeaderServlet}.
	 * 
	 * @param port          port number
	 * @param invertedIndex crawled index for webpage
	 * @throws Exception exception for header server
	 */
	public HeaderServer(int port, ThreadSafeInvertedIndex invertedIndex) throws Exception {
		this.server = new Server(port);
		this.port = port;
		index = invertedIndex;
		queries = new ArrayList<>();
	}

	/**
	 * Starts the server
	 * 
	 * @throws Exception server connection errors
	 */
	public void start() throws Exception {
		ServerConnector connector = new ServerConnector(server);
		connector.setHost("localhost");
		connector.setPort(port);

		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(HeaderServlet.class, "/");
		server.addConnector(connector);
		server.setHandler(handler);
		server.start();
		server.join();
	}

	/**
	 * Servlet to GET handle requests to /check.
	 */
	public static class HeaderServlet extends HttpServlet {

		/**
		 * serial version id
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Displays a form where users can enter a URL to check. When the button is
		 * pressed, submits the URL back to /check as a GET request.
		 *
		 * If a URL was included as a parameter in the GET request, fetch and display
		 * the HTTP headers of that URL.
		 */
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			// log.info("MessageServlet ID " + this.hashCode() + " handling GET request.");

			PrintWriter out = response.getWriter();
			out.printf("<html>%n%n");
			out.printf("<head><title>%s</title></head>%n", "Search");
			out.printf("<body>%n");

			out.printf("<h1>Results</h1>%n%n");
			// Keep in mind multiple threads may access at once
			int i = 1;
			if (queries == null || queries.isEmpty()) {
				out.printf("<p>No search results.</p>%n");
			} else {
				for (String url : queries) {
					out.printf("<p>%d. <a href=\"%s\">%s</a></p>%n", i, url, url);
					i++;
				}
			}

			out.printf("<h1>Search</h1>%n%n");
			printForm(request, response);
			out.printf("<p>This request was handled by thread %s. Updated at %s</p>%n",
					Thread.currentThread().getName(), getDate());
			out.printf("%n</body>%n");
			out.printf("</html>%n");
			response.setStatus(HttpServletResponse.SC_OK);
		}

		/**
		 * Outputs the HTML form for submitting new messages. The parameter names used
		 * in the form should match the names used by the servlet!
		 *
		 * @param request  the HTTP request
		 * @param response the HTTP response
		 * @throws IOException error for file
		 */
		private static void printForm(HttpServletRequest request, HttpServletResponse response) throws IOException {
			PrintWriter out = response.getWriter();
			out.printf("<form method=\"post\" action=\"%s\">%n", request.getServletPath());
			out.printf("<table cellspacing=\"0\" cellpadding=\"2\"%n");
			out.printf("<tr>%n");
			out.printf("\t<td nowrap>Query</td>");
			out.printf("\t%n<td>%n ");
			out.printf("\t\t<input type=\"text\" name=\"query\" maxlength=\"100\" size=\"60\">%n");
			out.printf("\t</td>%n");
			out.printf("</tr>%n");
			out.printf("</table>%n");
			out.printf("<p><input type=\"submit\" value=\"Search\"></p>\n%n");
			out.printf("</form>\n%n");
		}

		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			Stemmer stemmer = new SnowballStemmer(DEFAULT);

			queries.clear();
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);

			String query = request.getParameter("query");

			query = query == null ? "" : query;

			// Avoid XSS attacks using Apache Commons Text
			query = StringEscapeUtils.escapeHtml4(query);
			
			HashSet<String> stemmed = new HashSet<>(); 
			
			for (String queriedWord : TextParser.parse(query)) {
				queriedWord = stemmer.stem(queriedWord).toString();
				stemmed.add(queriedWord);
			}
			
			var results = index.partialSearch(stemmed);
			queries.clear();
			for (var result : results) {
				queries.add(result.getWhere());
			}

			response.setStatus(HttpServletResponse.SC_OK);
			response.sendRedirect(request.getServletPath());
		}

		/**
		 * Returns the date and time in a long format. For example: "12:00 am on
		 * Saturday, January 01 2000".
		 *
		 * @return current date and time
		 */
		private static String getDate() {
			String format = "hh:mm a 'on' EEEE, MMMM dd yyyy";
			DateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(new Date());
		}
	}
}