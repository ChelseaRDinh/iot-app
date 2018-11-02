package ca.uvic.seng330.assn3.devices;

import ca.uvic.seng330.assn3.Client;
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

  public abstract void alert(Client c, String message);

  public abstract void alert(Device d, String message);

  public abstract void register(Client c) throws HubRegistrationException;

  public abstract void register(Device d) throws HubRegistrationException;

  public abstract void unregister(Client c) throws HubRegistrationException;

  public abstract void unregister(Device d) throws HubRegistrationException;
}
