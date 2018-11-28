package ca.uvic.seng330.assn3.devices.camera;

import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.MasterHub;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.json.JSONObject;

public class CameraModel extends Model {
  private List<UUID> cameras;
  private Map<UUID, SimpleBooleanProperty> cameraIsRecording;
  private Map<UUID, SimpleBooleanProperty> cameraConditions;
  private List<Media> cameraMedia;
  private List<MediaPlayer> cameraMediaPlayers;

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

    cameraMedia = new ArrayList<Media>();
    cameraMediaPlayers = new ArrayList<MediaPlayer>();

    cameras = getUUIDOfType(Camera.class.getName());

    for (UUID camera : cameras) {
      Media media = new Media(getRandomURI());
      MediaPlayer player = new MediaPlayer(media);
      player.setOnEndOfMedia(
          new Runnable() {
            public void run() {
              player.seek(Duration.ZERO);
            }
          });

      cameraMedia.add(media);
      cameraMediaPlayers.add(player);

      cameraIsRecording.put(camera, new SimpleBooleanProperty());
      cameraConditions.put(camera, new SimpleBooleanProperty());
      sendMessageToDevice(Command.CAMERA_IS_RECORDING, camera);
      sendMessageToDevice(Command.CAMERA_GET_CONDITION, camera);
    }
  }

  /** Stops all of the video players. */
  public void stopAllPlayers() {
    for (MediaPlayer player : cameraMediaPlayers) {
      player.stop();
    }
  }

  public int getCameraCount() {
    return cameras.size();
  }

  public MediaPlayer getMediaPlayer(int index) {
    return cameraMediaPlayers.get(index);
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
    if (getCameraConditionAt(index) != value) {
      if (value) {
        cameraMediaPlayers.get(index).play();
      } else {
        cameraMediaPlayers.get(index).pause();
      }

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

      int index;
      for (index = 0; index < cameras.size(); index++) {
        if (cameras.get(index).equals(sender)) {
          break;
        }
      }
      if (condition) {
        cameraMediaPlayers.get(index).play();
      } else {
        cameraMediaPlayers.get(index).pause();
      }
    }
  }

  private String getRandomURI() {
    return "http://techslides.com/demos/sample-videos/small.mp4";
  }
}
