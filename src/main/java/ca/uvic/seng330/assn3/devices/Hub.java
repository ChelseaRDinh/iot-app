package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.Client;
import ca.uvic.seng330.assn3.JSONMessaging;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Hub extends Mediator {
  private HashMap<UUID, Client> clients;
  private HashMap<UUID, Device> devices;
  private final Logger logger;

  private enum LogSeverity {
    DEBUG,
    INFO,
    ERROR
  }

  /** Default constructor for the hub. */
  public Hub() {
    logger = LoggerFactory.getLogger(Hub.class);
    clients = new HashMap<UUID, Client>();
    devices = new HashMap<UUID, Device>();

    log(LogSeverity.DEBUG, "Hub created.");
  }

  /**
   * Gets all of the UUIDs of devices or clients with a given type (object.getClass().toString()).
   *
   * @param type the type of the class as a string
   * @return a list of UUIDs, which may be empty if there are no objects of the given type
   */
  @Override
  public List<UUID> getUUIDOfType(String type) {
    ArrayList<UUID> uuids = new ArrayList<UUID>();

    for (HashMap.Entry<UUID, Device> entry : devices.entrySet()) {
      if (entry.getValue().getClass().getName().equals(type)) {
        uuids.add(entry.getKey());
      }
    }

    // If UUIDs were found in devices, we definitely won't find any in clients.
    if (uuids.size() > 0) {
      return uuids;
    }

    for (HashMap.Entry<UUID, Client> entry : clients.entrySet()) {
      if (entry.getValue().getClass().getName().equals(type)) {
        uuids.add(entry.getKey());
      }
    }

    return uuids;
  }

  /**
   * Notifies the targets of a message with the message as a JSONObject.
   *
   * @param message the JSONMessaging message to send, with or without targets
   */
  @Override
  public void alert(JSONMessaging message) {
    List<UUID> targets = message.getTargets();

    if (message.getMessage().equals("objectInFront")
        || message.getMessage().equals("nothingInFront")) {
      processMessage(message.invoke());
      return;
    }

    // If there was no target, forward message to every object.
    if (targets.size() == 0) {
      // If the message was sent from a client, forward it to devices, otherwise to clients.
      if (message.getStatus().equals("client")) {
        for (Device d : devices.values()) {
          d.notify(message.invoke());
        }
      } else {
        for (Client c : clients.values()) {
          c.notify(message.invoke());
        }
      }

      return;
    }

    // Invoke the message for valid targets.
    for (UUID target : targets) {
      if (devices.containsKey(target)) {
        devices.get(target).notify(message.invoke());
      } else if (clients.containsKey(target)) {
        clients.get(target).notify(message.invoke());
      }
    }
  }

  /**
   * Registers a client with the hub.
   *
   * @pre c is a non-null client.
   * @param c the client to register
   * @throws ca.uvic.seng330.assn3.devices.HubRegistrationException throws when client is already
   *     registered or c is null
   */
  @Override
  public void register(Client c) throws HubRegistrationException {
    if (c == null) {
      log(LogSeverity.ERROR, "Null client registration.");
      throw new HubRegistrationException("Client is null.");
    }

    if (devices.containsKey(c.getIdentifier())) {
      log(LogSeverity.ERROR, "Client that was already registered was registered.");
      throw new HubRegistrationException("Failed to register client to hub.");
    }

    clients.put(c.getIdentifier(), c);

    log(LogSeverity.INFO, "Client registered UUID: " + c.getIdentifier().toString());
    log(LogSeverity.DEBUG, "Total clients" + new Integer(clients.size()) + ".");
  }

  /**
   * Registers a device with the hub.
   *
   * @pre d is a non-null device.
   * @param d the device to register
   * @throws ca.uvic.seng330.assn3.devices.HubRegistrationException throws when device is already
   *     registered or d is null
   */
  @Override
  public void register(Device d) throws HubRegistrationException {
    if (d == null) {
      log(LogSeverity.ERROR, "Null device registration.");
      throw new HubRegistrationException("Device is null.");
    }

    if (devices.containsKey(d.getIdentifier())) {
      log(LogSeverity.ERROR, "Device that was already registered was registered.");
      throw new HubRegistrationException("Failed to register device to hub.");
    }

    devices.put(d.getIdentifier(), d);

    log(LogSeverity.INFO, "Device registered UUID: " + d.getIdentifier().toString());
    log(LogSeverity.DEBUG, "Total devices" + new Integer(devices.size()) + ".");
  }

  /**
   * Unregisters a client from the hub.
   *
   * @pre c is a non-null client.
   * @param c the client to unregister
   * @throws ca.uvic.seng330.assn3.devices.HubRegistrationException throws when client isn't
   *     registered or c is null
   */
  @Override
  public void unregister(Client c) throws HubRegistrationException {
    if (c == null) {
      log(LogSeverity.ERROR, "Null client unregistration.");
      throw new HubRegistrationException("Client is null.");
    }

    if (!devices.containsKey(c.getIdentifier())) {
      log(LogSeverity.ERROR, "Client that wasn't registered was unregistered.");
      throw new HubRegistrationException("Failed to unregister client to hub.");
    }

    devices.remove(c.getIdentifier());

    log(LogSeverity.INFO, "Client unregistered UUID: " + c.getIdentifier().toString());
    log(LogSeverity.DEBUG, "Total clients" + new Integer(clients.size()) + ".");
  }

  /**
   * Unregisters a device from the hub.
   *
   * @pre d is a non-null device.
   * @param d the device to unregister
   * @throws ca.uvic.seng330.assn3.devices.HubRegistrationException throws when device isn't
   *     registered or d is null
   */
  @Override
  public void unregister(Device d) throws HubRegistrationException {
    if (d == null) {
      log(LogSeverity.ERROR, "Null device unregistration.");
      throw new HubRegistrationException("Device is null.");
    }

    if (!devices.containsKey(d.getIdentifier())) {
      log(LogSeverity.ERROR, "Device that wasn't registered was unregistered.");
      throw new HubRegistrationException("Failed to unregister device to hub.");
    }

    devices.remove(d.getIdentifier());

    log(LogSeverity.INFO, "Device unregistered UUID: " + d.getIdentifier().toString());
    log(LogSeverity.DEBUG, "Total devices" + new Integer(devices.size()) + ".");
  }

  /** Starts all the devices. */
  public void startup() {
    for (Device d : devices.values()) {
      d.startup();
    }

    setStatus(Status.NORMAL);

    log(LogSeverity.INFO, "Started " + new Integer(devices.size()).toString() + " devices.");
  }

  /** Shuts down all the devices. */
  public void shutdown() {
    for (Device d : devices.values()) {
      d.shutdown();
    }

    setStatus(Status.OFF);

    log(LogSeverity.INFO, "Shut down " + new Integer(devices.size()).toString() + " devices.");
  }

  /**
   * Gets the hash map of clients.
   *
   * @return a deep copy of the hash map
   */
  public HashMap<UUID, Client> getClients() {
    return new HashMap<UUID, Client>(clients);
  }

  /**
   * Gets the hash map of devices.
   *
   * @return a deep copy of the hash map
   */
  public HashMap<UUID, Device> getDevices() {
    return new HashMap<UUID, Device>(devices);
  }

  /**
   * Process a given message to the hub. For example, the camera notifies the hub of an object being
   * in front or not. Each camera is linked to a lightbulb at the same index, if there is one.
   *
   * @param jsonMessage the message sent to the hub
   */
  private void processMessage(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");
    UUID sender = UUID.fromString(jsonMessage.getString("node_id"));

    if (message.equals("objectInFront")) {
      int index = getIndexOfSenderCamera(sender);
      List<UUID> lightbulbs = getUUIDOfType(Lightbulb.class.getName());

      if (index == -1 || index > lightbulbs.size()) {
        return;
      }

      JSONMessaging on = new JSONMessaging(devices.get(sender), "turnOn");
      devices.get(lightbulbs.get(index)).notify(on.invoke());
    } else if (message.equals("nothingInFront")) {
      int index = getIndexOfSenderCamera(sender);
      List<UUID> lightbulbs = getUUIDOfType(Lightbulb.class.getName());

      if (index == -1 || index > lightbulbs.size()) {
        return;
      }

      JSONMessaging on = new JSONMessaging(devices.get(sender), "turnOff");
      devices.get(lightbulbs.get(index)).notify(on.invoke());
    }
  }

  private int getIndexOfSenderCamera(UUID sender) {
    List<UUID> cameras = getUUIDOfType(Camera.class.getName());

    int index = 0;
    for (UUID camera : cameras) {
      if (sender.equals(camera)) {
        return index;
      }
      ++index;
    }

    return -1;
  }

  /**
   * Logs a given message at a desired severity level.
   *
   * @param severity the severity at which to log (see LogSeverity enum for possible values)
   * @param message the message to log
   */
  private void log(LogSeverity severity, String message) {
    switch (severity) {
      case DEBUG:
        logger.debug(message);
        break;
      case INFO:
        logger.info(message);
        break;
      case ERROR:
        logger.error(message);
        break;
      default:
        logger.debug(message);
        logger.debug(
            "Previous message logged as debug but severity wasn't handled in switch statement.");
    }
  }
}
