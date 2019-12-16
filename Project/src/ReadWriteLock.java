import java.util.ConcurrentModificationException;

/**
 * Maintains a pair of associated locks, one for read-only operations and one
 * for writing. The read lock may be held simultaneously by multiple reader
 * threads, so long as there are no writers. The write lock is exclusive, but
 * also tracks which thread holds the lock.
 */
public class ReadWriteLock {

	/** The lock used for reading. */
	private final Lock readerLock;

	/** The lock used for writing. */
	private final Lock writerLock;

	/**
	 * Number of readers member
	 */
	private int numReaders;

	/**
	 * Number of writers member
	 */
	private int numWriters;

	/**
	 * Lock object member
	 */
	private final Object lock;

	/**
	 * Initializes a new simple read/write lock.
	 */
	public ReadWriteLock() {
		readerLock = new ReadLock();
		writerLock = new WriteLock();
		lock = new Object();
		numReaders = 0;
		numWriters = 0;
	}

	/**
	 * Returns the reader lock.
	 *
	 * @return the reader lock
	 */
	public Lock readLock() {
		// NOTE: DO NOT MODIFY THIS METHOD
		return readerLock;
	}

	/**
	 * Returns the writer lock.
	 *
	 * @return the writer lock
	 */
	public Lock writeLock() {
		// NOTE: DO NOT MODIFY THIS METHOD
		return writerLock;
	}

	/**
	 * Determines whether the thread running this code and the other thread are in
	 * fact the same thread.
	 *
	 * @param other the other thread to compare
	 * @return true if the thread running this code and the other thread are not
	 *         null and have the same ID
	 *
	 * @see Thread#getId()
	 * @see Thread#currentThread()
	 */
	public static boolean sameThread(Thread other) {
		// NOTE: DO NOT MODIFY THIS METHOD
		return other != null && other.getId() == Thread.currentThread().getId();
	}

	/**
	 * Used to maintain simultaneous read operations.
	 */
	private class ReadLock implements Lock {

		/**
		 * Will wait until there are no active writers in the system, and then will
		 * increase the number of active readers.
		 */
		@Override
		public void lock() {
			synchronized (lock) {
				while (numWriters > 0) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				numReaders++;
			}
		}

		/**
		 * Will decrease the number of active readers, and notify any waiting threads if
		 * necessary.
		 */
		@Override
		public void unlock() {
			synchronized (lock) {
				numReaders--;
				if (numReaders == 0) {
					lock.notifyAll();
				}
			}
		}
	}

	/**
	 * Used to maintain exclusive write operations.
	 */
	private class WriteLock implements Lock {
		/**
		 * Will wait until there are no active readers or writers in the system, and
		 * then will increase the number of active writers and update which thread holds
		 * the write lock.
		 * 
		 */
		@Override
		public void lock() {
			synchronized (lock) {
				while (numReaders > 0 || numWriters > 0) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();

					}
				}
				Thread.currentThread();
				numWriters++;
			}
		}

		/**
		 * Will decrease the number of active writers, and notify any waiting threads if
		 * necessary. If unlock is called by a thread that does not hold the lock, then
		 * a {@link ConcurrentModificationException} is thrown.
		 *
		 * @see #sameThread(Thread)
		 */
		@Override
		public void unlock() {
			synchronized (lock) {
				numWriters--;
				lock.notifyAll();
			}
		}
	}
}