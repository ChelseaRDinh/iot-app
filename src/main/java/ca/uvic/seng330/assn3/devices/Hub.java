package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.Client;
import ca.uvic.seng330.assn3.JSONMessaging;
import java.util.HashMap;
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
   * Alerts all the clients with a message from a device.
   *
   * @pre d isn't null
   * @param d the device sending the message
   * @param message the message the device wishes to send to the clients.
   */
  @Override
  public void alert(Device d, String message) {
    assert d != null;

    JSONMessaging jsonMessager = new JSONMessaging(d, message);
    JSONObject json = jsonMessager.invoke();

    // If the device has a target client then attempt to target it.
    if (json.has("target_id")) {
      UUID target = UUID.fromString(json.getString("target_id"));

      // Check to see if the target exists. If not tell the client that it doesn't exist.
      if (clients.containsKey(target)) {
        log(LogSeverity.INFO, "Device sent message and it was forwarded to the target client.");
        clients.get(target).notify(json);
      } else {
        log(LogSeverity.INFO, "Device sent message and its target was invalid.");
        JSONMessaging jsonReply = new JSONMessaging(d, "InvalidObject");
        d.notify(jsonReply.invoke());
      }
    } else {
      for (Client c : clients.values()) {
        c.notify(jsonMessager.invoke());
      }

      log(LogSeverity.INFO, "Device sent message and it was forwarded to the clients.");
      log(LogSeverity.DEBUG, "Device message that was sent: " + message);
      log(LogSeverity.DEBUG, "Total clients notified: " + new Integer(clients.size()) + ".");
    }
  }

  /**
   * Alerts all the devices with a message from a client.
   *
   * @pre c isn't null
   * @param c the client sending the message
   * @param message the message the client wishes to send to the devices.
   */
  @Override
  public void alert(Client c, String message) {
    assert c != null;

    JSONMessaging jsonMessager = new JSONMessaging(c, message);
    JSONObject json = jsonMessager.invoke();

    // If the device has a target client then attempt to target it.
    if (json.has("target_id")) {
      UUID target = UUID.fromString(json.getString("target_id"));

      // Check to see if the target exists. If not tell the client that it doesn't exist.
      if (devices.containsKey(target)) {
        log(LogSeverity.INFO, "Client sent message and it was forwarded to the target device.");
        devices.get(target).notify(json);
      } else {
        log(LogSeverity.INFO, "Client sent message and its target was invalid.");
        JSONMessaging jsonReply = new JSONMessaging(c, "InvalidObject");
        c.notify(jsonReply.invoke());
      }
    } else {
      for (Device d : devices.values()) {
        d.notify(jsonMessager.invoke());
      }

      log(LogSeverity.INFO, "Client sent message and it was forwarded to the devices.");
      log(LogSeverity.DEBUG, "Client message that was sent: " + message);
      log(LogSeverity.DEBUG, "Total devices notified: " + new Integer(devices.size()) + ".");
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

  /**
   * Adds a UUID target to a JSON message and returns it as the payload string.
   *
   * @pre no colons in the message
   * @param target the UUID as a string of the intended target
   * @param message the message to send (ie function call)
   * @return payload string including the target (if the target wasn't null)
   */
  public static String targetJSONMessage(String target, String message) {
    assert message.contains(":") == false;

    // If there isn't a target just return the message.
    if (target == null) {
      return message;
    }

    return message + ", target_id: " + target;
  }
}
