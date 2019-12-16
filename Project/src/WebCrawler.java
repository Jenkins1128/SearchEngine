import java.io.IOException;
import java.net.URL;
import java.util.HashSet;

/**
 * Crawler class to search for links and build the index
 * 
 * @author isaiahjenkins
 *
 */
public class WebCrawler extends InvertedIndexBuilder {

	/**
	 * The inverted index.
	 */
	private final ThreadSafeInvertedIndex index;

	/**
	 * Set to track already visited urls
	 */
	HashSet<String> alreadyVisitedUrls = null;

	/**
	 * Work queue for web crawler
	 */
	private final WorkQueue tasks;

	/**
	 * Max limit of pages to crawl
	 */
	private int limit;

	/**
	 * First url to crawl
	 */
	private String seedURL;

	/**
	 * @param index   thread safe index
	 * @param seedURL first url to crawl
	 * @param limit   max limit of pages to crawl
	 * @param queue   work queue
	 */
	public WebCrawler(ThreadSafeInvertedIndex index, String seedURL, int limit, WorkQueue queue) {
		super(index);
		this.index = index;
		this.tasks = queue;
		this.limit = limit;
		this.seedURL = seedURL;
		alreadyVisitedUrls = new HashSet<>();
		alreadyVisitedUrls.add(seedURL);
	}

	/**
	 * BFS to traverse through all URL links the seed URL and build inverted index.
	 * 
	 * @throws IOException error for file
	 */
	public void build() throws IOException {
		startbuildIndexTask(seedURL);
		tasks.finish();
	}

	/**
	 * Creates the first task and gives it to the work queue.
	 *
	 * @param url passed url to index
	 */
	private void startbuildIndexTask(String url) {
		tasks.execute(new BuildIndexTask(url));
	}

	/**
	 * The non-static task class for building index.
	 */
	private class BuildIndexTask implements Runnable {
		/** Passed path to build index */
		private final String currentURL;

		/**
		 * html of url
		 */
		private String htmlOfUrl;
		
		/**
		 * Initializes this task.
		 *
		 * @param url passed path for index
		 */
		public BuildIndexTask(String url) {
			this.currentURL = url;
			this.htmlOfUrl = null;
		}

		@Override
		public void run() {
			try {
				htmlOfUrl = HtmlFetcher.fetch(currentURL, 3);

				if (htmlOfUrl != null) {
					for (URL urlsToCrawal : LinkParser.listLinks(new URL(currentURL), htmlOfUrl)) {
						synchronized (alreadyVisitedUrls) {
							if (!alreadyVisitedUrls.contains(urlsToCrawal.toString())) {
								if (alreadyVisitedUrls.size() >= limit) {
									break;
								}
								alreadyVisitedUrls.add(urlsToCrawal.toString());
								tasks.execute(new BuildIndexTask(urlsToCrawal.toString()));

							}
						}
					}
				}


				InvertedIndex localIndex = new InvertedIndex();
				addFile(currentURL, HtmlCleaner.stripHtml(htmlOfUrl), localIndex);
				index.addAll(localIndex);
			} catch (IOException e) {
				System.out.println("Cannot read url");
			}
		}
	}
}