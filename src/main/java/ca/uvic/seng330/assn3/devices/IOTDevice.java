package ca.uvic.seng330.assn3.devices;

import java.util.UUID;

// General interface since some of the tests depend on interfaces instead of
// class extension.
public interface IOTDevice {
  public void setStatus(Status s);

  public Status getStatus();

  public UUID getIdentifier();
}
