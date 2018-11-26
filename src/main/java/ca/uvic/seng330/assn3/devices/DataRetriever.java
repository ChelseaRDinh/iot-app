package ca.uvic.seng330.assn3.devices;

import java.util.UUID;

import org.json.JSONObject;

import javafx.beans.property.SimpleStringProperty;
import ca.uvic.seng330.assn3.Command;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;

public class DataRetriever extends Model implements Runnable {
    UUID device;
    Command retrieveCommand;
    SimpleStringProperty stringProperty;
    int delay;

    public DataRetriever(Token token, MasterHub h, UUID device, Command retrieveCommand, SimpleStringProperty stringProperty, int delay) {
        super(token, h);
        this.device = device;
        this.retrieveCommand = retrieveCommand;
        this.stringProperty = stringProperty;
        this.delay = delay;
    }

    public void run() {
        while(!Thread.interrupted()) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                return;
            }
            sendMessageToDevice(retrieveCommand, device);
        }
    }

    public void notify(JSONObject jsonMessage) {
        String message = jsonMessage.getString("payload");

        if (message.equals(CommandsToMessages.get(retrieveCommand))){
            String data = jsonMessage.getString("data");

            stringProperty.set(data);
        }
    }
}