package ca.uvic.seng330.assn3.devices;

public class DataStream implements Runnable {
  private String[] data = new String[16];
  private int readIndex = 0;
  private int writeIndex = 0;
  private int count = 0;
  private int delay;

  /**
   * Constructor for a data stream that generates data. The data must be read before it goes stale
   * or it will be overwritten when no more memory is available.
   *
   * @param delay the delay before generating new data
   */
  public DataStream(int delay) {
    this.delay = delay;

    data[writeIndex] = new String("Initial");
    ++writeIndex;
  }

  /** Method that gets invoked when run on a thread, part of the Runnable interface. */
  public void run() {
    while (!Thread.interrupted()) {
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        return;
      }
      writeNext();
    }
  }

  /**
   * Gets the oldest data from the stream. If caught up, it waits to acquire the data. In this case,
   * the oldest data is also the newest.
   *
   * @return the oldest data available as a string
   */
  public synchronized String readNext() {
    if (readIndex == writeIndex) {
      // Wait until write is ahead, return empty data if the read is canceled.
      try {
        wait();
      } catch (InterruptedException e) {
        return "";
      }
    }

    String next = data[readIndex];
    readIndex = (readIndex + 1) % data.length;
    return next;
  }

  private synchronized void writeNext() {
    // Overwrite the oldest data.
    data[writeIndex] = new String(Integer.toString(count));
    count++;
    writeIndex = (writeIndex + 1) % data.length;

    // Keep the read index ahead always so it takes the most stale data.
    if (readIndex == writeIndex) {
      readIndex = (readIndex + 1) % data.length;
    }

    // If this is the case, it's possible there is another thread trying to retrieve data that is
    // waiting.
    if (readIndex == writeIndex - 1) {
      notifyAll();
    }
  }
}
