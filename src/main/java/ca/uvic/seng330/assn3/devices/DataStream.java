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
        resetDataStream();
        return;
      }
      writeNext();
    }

    resetDataStream();
  }

  /**
   * Gets the oldest data from the stream. If caught up, it waits to acquire the data. In this case,
   * the oldest data is also the newest.
   *
   * @return the oldest data available as a string
   */
  public String readNext() {
    synchronized (this) {
      if (readIndex == writeIndex) {
        // Wait until write is ahead, return empty data if the read is canceled.
        try {
          wait();
        } catch (InterruptedException e) {
          return "EMPTY";
        }
      }

      String next = data[readIndex];
      readIndex = (readIndex + 1) % data.length;
      return next;
    }
  }

  private void writeNext() {
    synchronized (this) {
      // Overwrite the oldest data. Add "th frame" because otherwise the JSON library will
      // automatically parse it to an integer. This is the dumbest functionality ever.
      data[writeIndex] = new String(Integer.toString(count) + "th frame");
      count++;

      // If this is the case, it's possible there is another thread trying to retrieve data that is
      // waiting.
      if (readIndex == writeIndex) {
        notifyAll();
      }

      writeIndex = (writeIndex + 1) % data.length;

      // Keep the read index ahead always so it takes the most stale data.
      if (readIndex == writeIndex) {
        readIndex = (readIndex + 1) % data.length;
      }
    }
  }

  private void resetDataStream() {
    int index = writeIndex - 1;
    if (index < 0) {
      index += 16;
    }

    String last = data[index % data.length];
    data = new String[16];
    data[0] = last;
    readIndex = 0;
    writeIndex = 1;
  }
}
