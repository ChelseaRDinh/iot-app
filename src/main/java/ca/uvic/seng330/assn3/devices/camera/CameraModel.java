package ca.uvic.seng330.assn3.devices.camera;

import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.DataRetriever;
import ca.uvic.seng330.assn3.devices.MasterHub;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.json.JSONObject;

public class CameraModel extends Model {
  // Must be final since modifying while threads are running could cause crashes/unexpected
  // behavior.
  private final List<UUID> cameras;
  private final Map<UUID, SimpleBooleanProperty> cameraIsRecording;
  private final Map<UUID, SimpleBooleanProperty> cameraConditions;
  private final Map<UUID, SimpleStringProperty> cameraData;
  private final Map<UUID, Thread> cameraThreads;
  private final Map<UUID, DataRetriever> dataRetrievers;

  /**
   * Constructor for the model for the Camera management UI.
   *
   * @param token the token of the current user
   * @param h the master hub containing all the hubs which may have devices registered to
   */
  public CameraModel(Token token, MasterHub h) {
    super(token, h);

    cameraIsRecording = new HashMap<UUID, SimpleBooleanProperty>();
    cameraConditions = new HashMap<UUID, SimpleBooleanProperty>();
    cameraData = new HashMap<UUID, SimpleStringProperty>();
    cameraThreads = new HashMap<UUID, Thread>();
    dataRetrievers = new HashMap<UUID, DataRetriever>();

    cameras = getUUIDOfType(Camera.class.getName());

    for (UUID camera : cameras) {
      cameraIsRecording.put(camera, new SimpleBooleanProperty());
      cameraConditions.put(camera, new SimpleBooleanProperty());
      cameraData.put(camera, new SimpleStringProperty());

      // Camera threads aren't created unless a camera is on.
      cameraThreads.put(camera, null);
      // Create a data retriever to operate on the camera's string property with a delay of 1/30
      // seconds in milliseconds.
      dataRetrievers.put(
          camera,
          new DataRetriever(token, h, camera, Command.CAMERA_GET_DATA, cameraData.get(camera), 33));

      sendMessageToDevice(Command.CAMERA_IS_RECORDING, camera);
      sendMessageToDevice(Command.CAMERA_GET_CONDITION, camera);
    }
  }

  public int getCameraCount() {
    return cameras.size();
  }

  /**
   * Gets the property of if the camera is recording at a given index in the model.
   *
   * @param index the index of the camera in the model
   * @return the property of if the camera is recording
   */
  public final SimpleBooleanProperty cameraIsRecordingPropertyAt(int index) {
    return cameraIsRecording.get(cameras.get(index));
  }

  /**
   * Gets whether or not the camera is recording at a given index in the model.
   *
   * @param index the index of the camera in the model
   * @return true if the camera is recording, false otherwise
   */
  public Boolean getCameraIsRecordingAt(int index) {
    return cameraIsRecording.get(cameras.get(index)).get();
  }

  /**
   * Sets whether or not the camera is recording at a given index in the model.
   *
   * @param index the index of the camera in the model
   * @param value true to start the camera recording if it isn't already, false to stop the
   *     recording if it is recording
   */
  public void setCameraIsRecordingAt(int index, Boolean value) {
    if (getCameraIsRecordingAt(index) != value) {
      cameraIsRecording.get(cameras.get(index)).set(value);

      if (value) {
        sendMessageToDevice(Command.CAMERA_RECORD, cameras.get(index), value.toString());
      } else {
        sendMessageToDevice(Command.CAMERA_STOP_RECORD, cameras.get(index), value.toString());
      }
    }
  }

  /**
   * Gets the property of the camera's condition at a given index in the model.
   *
   * @param index the index of the camera in the model
   * @return the property of the camera's condition
   */
  public final SimpleBooleanProperty cameraConditionPropertyAt(int index) {
    return cameraConditions.get(cameras.get(index));
  }

  /**
   * Gets the camera condition at a given index in the model.
   *
   * @param index the index of the camera in the model
   * @return true if the camera is on, false otherwise
   */
  public Boolean getCameraConditionAt(int index) {
    return cameraConditions.get(cameras.get(index)).get();
  }

  /**
   * Sets the camera condition at a given index in the model.
   *
   * @param index the index of the camera in the model
   * @param value the condition to set the camera to
   */
  public void setCameraConditionAt(int index, Boolean value) {
    UUID camera = cameras.get(index);

    if (getCameraConditionAt(index) != value) {
      if (value) {
        cameraThreads.replace(camera, new Thread(dataRetrievers.get(camera)));
        cameraThreads.get(camera).start();
      }

      cameraConditions.get(camera).set(value);
      sendMessageToDevice(Command.CAMERA_TOGGLE, camera);
    }
  }

  /**
   * Gets the property of if the camera is recording at a given index in the model.
   *
   * @param index the index of the camera in the model
   * @return the property of if the camera is recording
   */
  public final SimpleStringProperty cameraDataPropertyAt(int index) {
    return cameraData.get(cameras.get(index));
  }

  @Override
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    // Try to find a match from the commands that this model handles.
    String match = "";
    String[] commandsToCheck = {
      CommandsToMessages.get(Command.CAMERA_IS_RECORDING),
      CommandsToMessages.get(Command.CAMERA_GET_CONDITION),
    };

    for (String check : commandsToCheck) {
      if (!message.equals(check)) {
        continue;
      }

      match = check;
      break;
    }

    if (match.equals(CommandsToMessages.get(Command.CAMERA_IS_RECORDING))) {
      Boolean isRecording = jsonMessage.getBoolean("data");

      UUID sender = UUID.fromString(jsonMessage.getString("node_id"));

      // Check to see that this UUID has a condition tracked. If not, return.
      if (!cameraIsRecording.containsKey(sender)) {
        return;
      }

      cameraIsRecording.get(sender).set(isRecording);
    }

    if (match.equals(CommandsToMessages.get(Command.CAMERA_GET_CONDITION))) {
      Boolean condition = jsonMessage.getBoolean("data");

      UUID sender = UUID.fromString(jsonMessage.getString("node_id"));

      // Check to see that this UUID has a condition tracked. If not, return.
      if (!cameraConditions.containsKey(sender)) {
        return;
      }

      setCameraCondition(sender, condition);
    }
  }

  private void setCameraCondition(UUID camera, Boolean value) {
    if (cameraConditions.get(camera).get() != value) {
      if (value) {
        cameraThreads.replace(camera, new Thread(dataRetrievers.get(camera)));
        cameraThreads.get(camera).start();
      }

      cameraConditions.get(camera).set(value);
    }
  }
}
