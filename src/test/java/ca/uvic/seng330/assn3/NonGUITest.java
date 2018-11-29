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
import java.util.List;
import java.util.UUID;
import org.json.JSONObject;
import org.junit.Test;

// Tests covered: 3
public class NonGUITest extends Client {
  boolean gotNotified = false;

  public NonGUITest() {
    super(null);
  }

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
    c.sendMessageToDevice(Command.LIGHTBULB_TOGGLE, l.getIdentifier());
    boolean after = l.getCondition();

    Lightbulb l2 = (Lightbulb) h.getDevices().get(l.getIdentifier());
    assertNotEquals(before, l2.getCondition());

    assertNotEquals(before, after);

    before = after;
    c.sendMessageToAllDevices(Command.LIGHTBULB_TOGGLE);
    after = l.getCondition();

    assertNotEquals(before, after);

    before = after;
    c.sendMessageToDevice(Command.LIGHTBULB_GET_CONDITION, l.getIdentifier());

    assertEquals(c.getLightbulbConditions().containsKey(l.getIdentifier()), true);

    after = c.getLightbulbConditions().get(l.getIdentifier());

    assertEquals(before, after);

    l.toggle();
    c.sendMessageToDevice(Command.LIGHTBULB_GET_CONDITION, l.getIdentifier());
    after = c.getLightbulbConditions().get(l.getIdentifier());

    assertNotEquals(before, after);
  }

  @Test
  public void testMultipleTargetAlerts() {
    Hub h = new Hub();

    Lightbulb l1 = new Lightbulb(h);
    Lightbulb l2 = new Lightbulb(h);
    Lightbulb l3 = new Lightbulb(h);

    final boolean before = l1.getCondition();

    AndroidClient c = new AndroidClient(h);

    try {
      h.register(l1);
      h.register(l2);
      h.register(l3);
      h.register(c);
    } catch (HubRegistrationException e) {
      assertEquals(false, true);
    }

    List<UUID> lights = new ArrayList<UUID>();
    lights.add(l1.getIdentifier());
    lights.add(l2.getIdentifier());

    // Test toggling 2 lights in a target list.
    c.sendMessageToDevices(Command.LIGHTBULB_TOGGLE, lights);

    // Test retrieving the lightbulb condition in a target list.
    c.sendMessageToDevices(Command.LIGHTBULB_GET_CONDITION, lights);
    assertEquals(c.getLightbulbConditions().get(l1.getIdentifier()), !before);
    assertEquals(c.getLightbulbConditions().get(l2.getIdentifier()), !before);

    // Test retrieving all the lightbulb conditions via a target list.
    lights.add(l3.getIdentifier());
    c.sendMessageToDevices(Command.LIGHTBULB_GET_CONDITION, lights);
    assertEquals(c.getLightbulbConditions().get(l1.getIdentifier()), !before);
    assertEquals(c.getLightbulbConditions().get(l2.getIdentifier()), !before);
    assertEquals(c.getLightbulbConditions().get(l3.getIdentifier()), before);
  }

  @Test
  public void testGetUUIDOfType() {
    // Create objects.
    Hub h = new Hub();

    Lightbulb l1 = new Lightbulb(h);
    Lightbulb l2 = new Lightbulb(h);
    Lightbulb l3 = new Lightbulb(h);

    SmartPlug s1 = new SmartPlug(h);
    SmartPlug s2 = new SmartPlug(h);
    SmartPlug s3 = new SmartPlug(h);

    AndroidClient c1 = new AndroidClient(h);
    AndroidClient c2 = new AndroidClient(h);

    // Register objects with the hub for filtering.
    try {
      h.register(l1);
      h.register(l2);
      h.register(l3);

      h.register(s1);
      h.register(s2);
      h.register(s3);

      h.register(c1);
      h.register(c2);
    } catch (HubRegistrationException e) {
      assertEquals(false, true);
    }

    // Get the lists of UUIDs from the hub.
    final List<UUID> r1 = h.getUUIDOfType(l1.getClass().getName());
    final List<UUID> r2 = h.getUUIDOfType(s1.getClass().getName());
    final List<UUID> r3 = h.getUUIDOfType(c1.getClass().getName());
    final List<UUID> r4 = h.getUUIDOfType(h.getClass().getName());
    final List<UUID> r5 = h.getUUIDOfType("type");
    final List<UUID> r6 = h.getUUIDOfType("");
    final List<UUID> r7 = h.getUUIDOfType(null);

    // Check to see that the result lists contain the UUIDs that they should (if any) and their
    // size.
    assertEquals(r1.contains(l1.getIdentifier()), true);
    assertEquals(r1.contains(l2.getIdentifier()), true);
    assertEquals(r1.contains(l3.getIdentifier()), true);
    assertEquals(r1.size(), 3);

    assertEquals(r2.contains(s1.getIdentifier()), true);
    assertEquals(r2.contains(s2.getIdentifier()), true);
    assertEquals(r2.contains(s3.getIdentifier()), true);
    assertEquals(r2.size(), 3);

    assertEquals(r3.contains(c1.getIdentifier()), true);
    assertEquals(r3.contains(c2.getIdentifier()), true);
    assertEquals(r3.size(), 2);

    assertEquals(r4.size(), 0);

    assertEquals(r5.size(), 0);

    assertEquals(r6.size(), 0);

    assertEquals(r7.size(), 0);
  }

  @Test
  public void testAuthManager() {
    try {
      AuthManager auth = new AuthManager();

      // Test admin token.
      Token token = auth.getToken("admin", "admin");
      assertEquals(auth.isValidToken(token), true);
      assertEquals(auth.isAdminToken(token), true);

      // Test user token.
      token = auth.getToken("user", "user");
      assertEquals(auth.isValidToken(token), true);
      assertEquals(auth.isAdminToken(token), false);

      // Test invalid token.
      token = auth.getToken("user", "wrong");
      assertEquals(auth.isValidToken(token), false);
      assertEquals(auth.isAdminToken(token), false);

      // Test empty token.
      token = auth.getToken("", "");
      assertEquals(auth.isValidToken(token), false);

      // Test empty password, and valid password required for admin tokens.
      token = auth.getToken("admin", "");
      assertEquals(auth.isValidToken(token), false);
      assertEquals(auth.isAdminToken(token), false);
    } catch (Exception e) {
      assertEquals(false, true);
    }
  }

  /**
   * GIVEN the Camera is functional WHEN the camera detects an object in front of it THEN my camera
   * notifies Hub about the activity.
   *
   * <p>GIVEN a functional light bulb WHEN there is no one in the room (Camera notifies hub) THEN it
   * should turn off and should notify Hub about the activity.
   *
   * <p>GIVEN a non-functional light bulb WHEN someone enters the room (Camera notifies hub) THEN it
   * should turn on and should notify Hub about the activity.
   */
  @Test
  public void testCameraNotify() {
    Hub h = new Hub();
    Camera c = new Camera(h);
    Lightbulb b = new Lightbulb(h);

    try {
      h.register(c);
      h.register(b);
      h.register(this);
    } catch (Exception e) {
      assertEquals(false, true);
    }

    // given: a non-functional lightbulb
    if (b.getCondition()) {
      b.toggle();
    }

    // given: the camera is on and detects an object in front
    if (!c.getCondition()) {
      c.toggle();
    }
    c.seeObjectInFront();

    // then: the lightbulb in the room should turn on
    // Implies that the camera notified the hub in order to pass the messages.
    // Got notified implies that the hub was notified by the lightbulb of the activity, as the
    // lightbulb sends a message to the clients.
    assertEquals(b.getCondition(), true);
    assertEquals(gotNotified, true);

    gotNotified = false;

    // given: the camera is on and detects nothing in front
    c.seeNothingInFront();

    // then: the lightbulb in the room should turn off
    // Implies that the camera notified the hub in order to pass the messages.
    // Got notified implies that the hub was notified by the lightbulb of the activity, as the
    // lightbulb sends a message to the clients.
    assertEquals(b.getCondition(), false);
    assertEquals(gotNotified, true);

    // Check that nothing happens when the camera is off.
    c.toggle();
    c.seeObjectInFront();
    assertEquals(b.getCondition(), false);

    // Set the lightbulb to on now for the next test.
    b.toggle();

    // Check that nothing happens again when the camera is off.
    c.seeNothingInFront();
    assertEquals(b.getCondition(), true);
  }

  /**
   * Gets messages back from a hub.
   *
   * @param jsonMessage the json object sent by the hub
   */
  public void notify(JSONObject jsonMessage) {
    String message = jsonMessage.getString("payload");

    if (message.equals(CommandsToMessages.get(Command.LIGHTBULB_CONDITION_CHANGED))) {
      gotNotified = true;
    }
  }
}
