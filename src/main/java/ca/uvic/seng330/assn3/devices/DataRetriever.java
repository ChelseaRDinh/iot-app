package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import java.util.UUID;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import org.json.JSONObject;

public class DataRetriever extends Model implements Runnable {
  UUID device;
  Command retrieveCommand;
  SimpleStringProperty stringProperty;
  int delay;

  /**
   * Constructor for a data retriever that retrieves data from a device at a specific rate.
   *
   * @param token the token of the current user
   * @param h the master hub containing the hubs of all users
   * @param device the device to retrieve data from
   * @param retrieveCommand the command to send to the device to retrieve data
   * @param stringProperty the property to set to the data from the device
   * @param delay the delay before retrieving new data
   */
  public DataRetriever(
      Token token,
      MasterHub h,
      UUID device,
      Command retrieveCommand,
      SimpleStringProperty stringProperty,
      int delay) {
    super(token, h);
    this.device = device;
    this.retrieveCommand = retrieveCommand;
    this.stringProperty = stringProperty;
    this.delay = delay;
  }

  /** Method that gets invoked when run on a thread, part of the Runnable interface. */
  public void run() {
    while (!Thread.interrupted()) {
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        return;
      }
      sendMessageToDevice(retrieveCommand, device);
    }
  }

  /** Gets the result of the data retrieve request. */
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    if (message.equals(CommandsToMessages.get(retrieveCommand))) {
      final String data = jsonMessage.getString("data");

      // Make sure that the property set gets run on the UI thread.
      Platform.runLater(
          new Runnable() {
            @Override
            public void run() {
              stringProperty.set(data);
            }
          });
    }
  }
}
