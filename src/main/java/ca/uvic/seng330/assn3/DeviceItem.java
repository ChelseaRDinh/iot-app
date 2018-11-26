package ca.uvic.seng330.assn3;

import javafx.beans.property.SimpleStringProperty;

public class DeviceItem {
  private SimpleStringProperty deviceType;
  private SimpleStringProperty deviceUUID;
  private SimpleStringProperty deviceOwner;
  /**
   * The constructor for a deviceItem class.
   * The deviceItem is simply to add a device to the admin device management console
   * In string format.
   * @param deviceType the type of device (Camera, Thermostat, Lightbulb, etc.)
   * @param deviceUUID the UUID of the device.
   * @param deviceOwner the username of the device owner.
   */
  public DeviceItem(String deviceType, String deviceUUID, String deviceOwner) {
    this.deviceType = new SimpleStringProperty(deviceType);
    this.deviceUUID = new SimpleStringProperty(deviceUUID);
    this.deviceOwner = new SimpleStringProperty(deviceOwner);
  }
  
  /**
   *
   * @return the type of the device.
   */
  public String getType() {
    return deviceType.get();
  }

  /**
   *
   * @return the device's UUID
   */
  public String getUUID() {
    return deviceUUID.get();
  }

  /**
   * @return the username of the owner of the device
   */
  public String getOwner() {
    return deviceOwner.get();
  }

  /**
   *
   * @param s the type to set the device
   */
  public void setType(String s) {
    deviceType.set(s);
  }

  /**
   * @param s the UUID of the device to set it to
   */
  public void setUUID(String s) {
    deviceUUID.set(s);
  }

  /**
   * @param s the username of the new device owner
   */
  public void setOwner(String s) {
    deviceOwner.set(s);
  }
}
