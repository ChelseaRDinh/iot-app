package ca.uvic.seng330.assn3.devices;

public class DataStream implements Runnable {
    private String[] data = new String[16];
    private int readIndex = 0;
    private int writeIndex = 0;
    private int count = 0;
    private int delay;

    public DataStream(int delay){
        this.delay = delay;

        data[writeIndex] = new String("Initial");
        ++writeIndex;
    }

    public void run() {
        while(!Thread.interrupted()) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                return;
            }
            writeNext();
        }
    }

    public synchronized String readNext() {
        if (readIndex == writeIndex) {
            //wait until write is ahead
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
    }
}
