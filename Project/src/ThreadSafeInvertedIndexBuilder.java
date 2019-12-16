import java.io.IOException;
import java.nio.file.Path;

/**
 * Builds the Inverted Index.
 * 
 * @author isaiahjenkins
 */
public class ThreadSafeInvertedIndexBuilder extends InvertedIndexBuilder {

	/**
	 * The inverted index.
	 */
	private final ThreadSafeInvertedIndex index;

	/** The work queue that will handle all of the tasks. */
	private final WorkQueue tasks;

	/**
	 * Constructor for index instance.
	 * 
	 * @param index to build invert
	 * @param queue work queue
	 */
	public ThreadSafeInvertedIndexBuilder(ThreadSafeInvertedIndex index, WorkQueue queue) {
		super(index);
		this.index = index;
		this.tasks = queue;
	}

	/**
	 * Creates a work queue for building inverted index.
	 * 
	 * @param start      passed start path for index
	 * @throws IOException error for file
	 */
	@Override
	public void build(Path start) throws IOException {
		super.build(start);
		tasks.finish();
	}

	/**
	 * Add files of index words to inverted index based on location
	 * 
	 * @param location the path to index
	 * @throws IOException exception for file error
	 */
	public void addFile(Path location) throws IOException {
		tasks.execute(new AddToIndexTask(location));
	}

	/**
	 * The non-static task class for building index.
	 */
	private class AddToIndexTask implements Runnable {
		/** Passed path to build index */
		private final Path path;

		/**
		 * Initializes this task.
		 *
		 * @param path passed path for index
		 */
		public AddToIndexTask(Path path) {
			this.path = path;
		}

		@Override
		public void run() {
			try {
				InvertedIndex localIndex = new InvertedIndex();
				addFile(path, localIndex);
				index.addAll(localIndex);
			} catch (IOException e) {
				System.out.println("Unable to build index from the path: " + e.toString());
			}
		}
	}
}