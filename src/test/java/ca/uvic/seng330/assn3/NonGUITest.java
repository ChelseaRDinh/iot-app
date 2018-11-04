package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import ca.uvic.seng330.assn3.Examples.AndroidClient;
import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Device;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.HubRegistrationException;
import ca.uvic.seng330.assn3.devices.Lightbulb;
import ca.uvic.seng330.assn3.devices.SmartPlug;
import ca.uvic.seng330.assn3.devices.Thermostat;
import java.util.ArrayList;
import org.junit.Test;

public class NonGUITest {
  @Test
  public void applicationExists() {
    IOTApplication app = new IOTApplication();
  }

  @Test
  public void testObjectsExist() {
    Hub mediator = new Hub();
    Camera c1 = new Camera(mediator);
    Thermostat t1 = new Thermostat(mediator);
    Lightbulb l1 = new Lightbulb(mediator);
    SmartPlug s1 = new SmartPlug(mediator);
  }

  @Test
  public void testObjectsHaveUUID() {
    ArrayList<Device> devices = new ArrayList<Device>();
    Hub mediator = new Hub();

    for (int i = 0; i < 1000; i++) {
      devices.add(new Camera(mediator));
      devices.add(new Thermostat(mediator));
      devices.add(new Lightbulb(mediator));
      devices.add(new SmartPlug(mediator));
    }

    for (int i = 0; i < devices.size(); i++) {
      for (int j = i + 1; j < devices.size(); j++) {
        assertNotEquals(devices.get(i).getIdentifier(), devices.get(j).getIdentifier());
      }
    }
  }

  // Test that all the android client sending message functionality works and that
  // lightbulb sending message functionality also works.
  @Test
  public void testAlerts() {
    Hub h = new Hub();
    Lightbulb l = new Lightbulb(h);
    AndroidClient c = new AndroidClient(h);

    try {
      h.register(l);
      h.register(c);
    } catch (HubRegistrationException e) {
      assertEquals(true, false); // fail the test.
    }

    assertEquals(c.getLightbulbConditions().containsKey(l.getIdentifier()), false);

    boolean before = l.getCondition();
    c.toggleLightbulb(l.getIdentifier());
    boolean after = l.getCondition();

    Lightbulb l2 = (Lightbulb) h.getDevices().get(l.getIdentifier());
    assertNotEquals(before, l2.getCondition());

    assertNotEquals(before, after);

    before = after;
    c.toggleAllLightbulbs();
    after = l.getCondition();

    assertNotEquals(before, after);

    before = after;
    c.retrieveLightbulbCondition(l.getIdentifier());

    assertEquals(c.getLightbulbConditions().containsKey(l.getIdentifier()), true);

    after = c.getLightbulbConditions().get(l.getIdentifier());

    assertEquals(before, after);

    l.toggle();
    c.retrieveLightbulbCondition(l.getIdentifier());
    after = c.getLightbulbConditions().get(l.getIdentifier());

    assertNotEquals(before, after);
  }
}
