package ca.uvic.seng330.assn3.devices.camera;

import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.MasterHub;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.JSONObject;

public class CameraModel extends Model {
  private List<UUID> cameras;
  private Map<UUID, SimpleBooleanProperty> cameraIsRecording;
  private Map<UUID, SimpleBooleanProperty> cameraConditions;

  public CameraModel(Token token, MasterHub h) {
    super(token, h);

    cameraIsRecording = new HashMap<UUID, SimpleBooleanProperty>();
    cameraConditions = new HashMap<UUID, SimpleBooleanProperty>();

    cameras = getUUIDOfType(Camera.class.getName());

    for (UUID camera : cameras) {
      cameraIsRecording.put(camera, new SimpleBooleanProperty());
      cameraConditions.put(camera, new SimpleBooleanProperty());
      sendMessageToDevice(Command.CAMERA_IS_RECORDING, camera);
      sendMessageToDevice(Command.CAMERA_GET_CONDITION, camera);
    }
  }

  public int getCameraCount() {
    return cameras.size();
  }

  public final SimpleBooleanProperty cameraIsRecordingPropertyAt(int index) {
    return cameraIsRecording.get(cameras.get(index));
  }

  public Boolean getCameraIsRecordingAt(int index) {
    return cameraIsRecording.get(cameras.get(index)).get();
  }

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

  public final SimpleBooleanProperty cameraConditionPropertyAt(int index) {
    return cameraConditions.get(cameras.get(index));
  }

  public Boolean getCameraConditionAt(int index) {
    return cameraConditions.get(cameras.get(index)).get();
  }

  public void setCameraConditionAt(int index, Boolean value) {
    if (getCameraConditionAt(index) != value) {
      cameraConditions.get(cameras.get(index)).set(value);
      sendMessageToDevice(Command.CAMERA_TOGGLE, cameras.get(index));
    }
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

      cameraConditions.get(sender).set(condition);
    }
  }
}
