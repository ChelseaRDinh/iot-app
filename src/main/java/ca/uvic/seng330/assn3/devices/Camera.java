package ca.uvic.seng330.assn3.devices;

import org.json.JSONObject;

public final class Camera extends Device {
  private boolean isOn;
  private boolean isRecording;
  private int diskPercentageUsed;

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
  }

  /**
   * Make the camera start recording.
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

  public void toggle() {
    isOn = !isOn;
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
    }
  }
}
