package ca.uvic.seng330.assn3.devices;

import org.json.JSONObject;

public final class Camera extends Device {
  private boolean isRecording;

  // Actually is the percentage used but the assignment spec doesn't explicitly state that and the
  // test implies it despite it being called diskSize implying that it indicates bytes of storage.
  private int diskSize;

  /**
   * Constructor for camera that takes a hub.
   *
   * @param h the hub that this device is under
   */
  public Camera(Hub h) {
    super(h);

    isRecording = false;
    diskSize = 0;
  }

  /**
   * Make the camera start recording.
   *
   * @throws ca.uvic.seng330.assn3.devices.CameraFullException when the diskSize reaches 100, it's
   *     actually disk percentage used
   */
  public void record() throws CameraFullException {
    if (diskSize >= 100) {
      throw new CameraFullException("Camera is full and cannot record.");
    }

    // Use up disk size when a recording is started.
    isRecording = true;
    diskSize++;

    alertHub("Camera recording started, diskSize: " + new Integer(diskSize).toString());

    if (diskSize == 100) {
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

  /**
   * Get the percentage of the disk used.
   *
   * @return the percentage of the disk used
   */
  public int getDiskSize() {
    return diskSize;
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
        alertHub(Hub.targetJSONMessage(jsonMessage.getString("node_id"), "CameraFullException"));
      }
    } else if (message.equals("isRecording")) {
      alertHub(
          Hub.targetJSONMessage(
              jsonMessage.getString("node_id"),
              "isRecording," + new Boolean(isRecording()).toString()));
    } else if (message.equals("getDiskSize")) {
      alertHub(
          Hub.targetJSONMessage(
              jsonMessage.getString("node_id"), "getDiskSize," + new Integer(diskSize).toString()));
    }
  }
}
