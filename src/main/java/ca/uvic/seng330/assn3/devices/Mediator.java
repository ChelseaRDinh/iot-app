package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.Client;
import ca.uvic.seng330.assn3.JSONMessaging;
import java.util.List;
import java.util.UUID;

public abstract class Mediator {
  protected UUID uuid;
  protected Status status;

  public Mediator() {
    uuid = UUID.randomUUID();
    status = Status.NORMAL;
  }

  public final void setStatus(Status s) {
    status = s;
  }

  public final Status getStatus() {
    return status;
  }

  public final UUID getIdentifier() {
    return uuid;
  }

  public abstract String getDeviceClassName(UUID device);

  public abstract String getDeviceStatus(UUID device);

  public abstract boolean doesDeviceExist(UUID device);

  public abstract Device getDeviceByUUID(UUID uuid);

  public abstract List<UUID> getUUIDOfType(String type);

  public abstract List<UUID> getUUIDOfType(String type, boolean ignoreStatus);

  public abstract void alert(JSONMessaging message);

  public abstract void register(Client c) throws HubRegistrationException;

  public abstract void register(Device d) throws HubRegistrationException;

  public abstract void unregister(Client c) throws HubRegistrationException;

  public abstract void unregister(Device d) throws HubRegistrationException;
}
