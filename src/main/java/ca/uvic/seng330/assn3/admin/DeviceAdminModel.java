package ca.uvic.seng330.assn3.admin;

import ca.uvic.seng330.assn3.DeviceItem;
import ca.uvic.seng330.assn3.Model;
import ca.uvic.seng330.assn3.Token;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Device;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import ca.uvic.seng330.assn3.devices.Thermostat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.beans.property.SimpleBooleanProperty;
import org.json.JSONObject;
import java.util.*;

public class DeviceAdminModel extends Model {
  private Thread getDevices;

  private List<UUID> devices;
  private List<DeviceItem> deviceItems;
  private List<String> devicesSupported;
  private MasterHub h;

  private SimpleBooleanProperty isReady;

  /**
   * Constructor for the model of the device admin UI.
   *
   * @param token the user's token
   * @param h the hub containing all the hubs
   */
  public DeviceAdminModel(Token token, MasterHub h) {
    super(token, h);

    isReady = new SimpleBooleanProperty(false);
    devices = new ArrayList<UUID>();
	deviceItems = new ArrayList<DeviceItem>();
	this.h = h;

    devicesSupported = new ArrayList<String>();
    devicesSupported.add(Camera.class.getName());
    devicesSupported.add(Lightbulb.class.getName());
	devicesSupported.add(SmartPlug.class.getName());
	devicesSupported.add(Thermostat.class.getName());
	

    Runnable task =
        new Runnable() {
          @Override
          public void run() {
            retrieveDeviceInfo();
            isReady.set(true);
          }
        };

    getDevices = new Thread(task);
    getDevices.start();
  }

  /** Stops getting all the devices if the work is no longer needed. */
  public void stopGettingDevices() {
    if (getDevices.isAlive()) {
      getDevices.interrupt();
    }
  }

  /**
   * Gets a list of classes that the device admin model supports adding.
   *
   * @return list of strings that you can pass into addNewDevice
   */
  public List<String> getAddableClasses() {
    return new ArrayList<String>(devicesSupported);
  }

  /**
   * Property on if the model is initialized and ready.
   *
   * @return property on if the model is ready
   */
  public SimpleBooleanProperty isReadyProperty() {
    return isReady;
  }

  /**
   * Removes a device from the hub by UUID.
   *
   * @param device the UUID of the device to remove
   * @return true if the device was removed, false if otherwise
   */
  public boolean removeByUUID(UUID device) {
    return removeDeviceByUUID(device);
  }

  /**
   * Adds a new device to the admin hub by class name.
   *
   * @param className the name of the class to add
   * @return the device item for the newly added device, null if the model isn't ready, the device
   *     name isn't supported, or device registration fails
   */
  public DeviceItem addNewDevice(String className) {
    if (!isReady.get() || !devicesSupported.contains(className)) {
      return null;
    }

    Device newDevice = null;
    if (className.equals(Camera.class.getName())) {
      newDevice = new Camera(getHub());
    } else if (className.equals(Lightbulb.class.getName())) {
      newDevice = new Lightbulb(getHub());
    } else if (className.equals(SmartPlug.class.getName())) {
      newDevice = new SmartPlug(getHub());
    } else if (className.equals(Thermostat.class.getName())) {
      newDevice = new Thermostat(getHub());
    } else {
      return null;
    }

    UUID newDeviceUUID = registerNewDevice(newDevice);
    if (newDeviceUUID == null) {
      return null;
    }

    devices.add(newDeviceUUID);

    DeviceItem newDeviceItem = getDeviceItemFromUUID(newDeviceUUID);
    deviceItems.add(newDeviceItem);

    return newDeviceItem;
  }

  /**
   * Get the number of devices that the model has info on.
   *
   * @return -1 if the model isn't ready, the number of devices the model has info on otherwise.
   */
  public int getDeviceCount() {
    if (!isReady.get()) {
      return -1;
    }

    return devices.size();
  }

  /*
  * Get HashSet of all usernames.
  */
  public Set<String> getAllUsernames() {
	  return h.getAllUsernames();
  } 

  /**
   * Gets the device item at a given index.
   *
   * @param index the index to retrieve the device item at
   * @return the device item at a given index, null if the model isn't ready
   */
  public DeviceItem getDeviceItemAt(int index) {
    if (!isReady.get()) {
      return null;
    }

    return deviceItems.get(index);
  }

  private void retrieveDeviceInfo() {
    for (String className : devicesSupported) {
      if (Thread.interrupted()) {
        return;
      }

      devices.addAll(getUUIDOfType(className));
    }

    for (UUID device : devices) {
      if (Thread.interrupted()) {
        return;
      }

      // Simulate it taking a reasonable amount of time to load.
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        return;
      }

      deviceItems.add(getDeviceItemFromUUID(device));
    }
  }

  private DeviceItem getDeviceItemFromUUID(UUID device) {
    return new DeviceItem(
        getDeviceClassName(device),
        device.toString(),
        getDeviceStatus(device),
        getDeviceOwner(device));
  }

  @Override
  public void notify(JSONObject message) {}
}
