package ca.uvic.seng330.assn3.devices;

import org.json.JSONObject;

public final class Camera extends Device {
  private boolean isOn;
  private boolean isRecording;
  private int diskPercentageUsed;
  private Thread cameraThread;
  private DataStream dataStream;

  /**
   * Constructor for camera that takes a hub.
   *
   * @param h the hub that this device is under
   */
  public Camera(Hub h) {
    super(h);

    isOn = false;
    isRecording = false;
    diskPercentageUsed = 0;

    // Set the data stream to generate data at 1/30 seconds in milliseconds.
    // This mimics actual camera hardware generating a frame at 30 FPS.
    dataStream = new DataStream(33);

    cameraThread = null;
    if (isOn) {
      cameraThread = new Thread(dataStream);
      cameraThread.start();
    }
  }

  /**
   * Constructor for camera that generates data at a specific rate.
   *
   * @param h the hub that owns the device
   * @param delay the delay in milliseconds between generating a frame of data.
   */
  public Camera(Hub h, int delay) {
    super(h);

    isOn = false;
    isRecording = false;
    diskPercentageUsed = 0;

    dataStream = new DataStream(delay);

    cameraThread = null;
    if (isOn) {
      cameraThread = new Thread(dataStream);
      cameraThread.start();
    }
  }

  /**
   * Make the camera start recording. Doesn't actually save the data due to time constraints.
   *
   * @throws ca.uvic.seng330.assn3.devices.CameraFullException when the diskSize reaches 100, it's
   *     actually disk percentage used
   */
  public void record() throws CameraFullException {
    if (diskPercentageUsed >= 100) {
      throw new CameraFullException("Camera is full and cannot record.");
    }

    // Use up disk size when a recording is started.
    isRecording = true;
    diskPercentageUsed++;

    alertHub("Camera recording started, diskSize: " + new Integer(diskPercentageUsed).toString());

    if (diskPercentageUsed == 100) {
      alertHub("Camera disk is now full.");
    }
  }

  /**
   * Get if the camera is recording or not.
   *
   * @return if the camera is recording or not
   */
  public boolean isRecording() {
    return isRecording;
  }

  public boolean getCondition() {
    return isOn;
  }

  /** Toggles the camera on and off, and starts and stops the camera hardware thread. */
  public void toggle() {
    isOn = !isOn;

    // If the camera is toggled on, start generating data, otherwise stop.
    if (isOn) {
      cameraThread = new Thread(dataStream);
      cameraThread.start();
    } else {
      cameraThread.interrupt();
      cameraThread = null;
    }
  }

  /**
   * Get the percentage of the disk used.
   *
   * @return the percentage of the disk used
   */
  public int getDiskPercentageUsed() {
    return diskPercentageUsed;
  }

  /**
   * Notify the camera of some message sent to it.
   *
   * @param jsonMessage the message as a JSONObject that's been sent
   */
  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    // If this was targeted then carry out the action if the action is applicable to this device.
    if (message.equals("record")) {
      try {
        record();
      } catch (CameraFullException e) {
        // Alert the hub with the sender as the target.
        alertHub(jsonMessage.getString("node_id"), "CameraFullException");
      }
    } else if (message.equals("isRecording")) {
      alertHub(
          jsonMessage.getString("node_id"), "isRecording", new Boolean(isRecording()).toString());
    } else if (message.equals("getDiskPercentageUsed")) {
      alertHub(
          jsonMessage.getString("node_id"),
          "getDiskPercentageUsed,",
          new Integer(diskPercentageUsed).toString());
    } else if (message.equals("stopRecording")) {
      if (isRecording) {
        isRecording = false;
      }
    } else if (message.equals("getCondition")) {
      alertHub(
          jsonMessage.getString("node_id"), "getCondition", new Boolean(getCondition()).toString());
    } else if (message.equals("toggle")) {
      toggle();
    } else if (message.equals("getData")) {
      if (cameraThread != null && isOn) {
        String data = dataStream.readNext();

        if (data == null) {
          return;
        }

        alertHub(jsonMessage.getString("node_id"), "getData", data);
      }
    }
  }
}
