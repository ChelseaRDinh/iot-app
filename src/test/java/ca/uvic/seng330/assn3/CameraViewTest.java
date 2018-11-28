package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import ca.uvic.seng330.assn3.devices.Camera;
import ca.uvic.seng330.assn3.devices.Hub;
import ca.uvic.seng330.assn3.devices.MasterHub;
import ca.uvic.seng330.assn3.devices.camera.CameraController;
import ca.uvic.seng330.assn3.devices.camera.CameraModel;
import ca.uvic.seng330.assn3.devices.camera.CameraView;
import javafx.scene.Scene;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

// Tests covered: 5
public class CameraViewTest extends ApplicationTest {
  private AuthManager authManager;
  private MasterHub allHubs;
  private Scene scene;
  private Stage primaryStage;
  private Camera camera;
  CameraModel cameraModel;

  @Override
  public void start(Stage primaryStage) {
    Hub allDevices = new Hub();
    camera = new Camera(allDevices);

    try {
      authManager = new AuthManager();

      allDevices.register(camera);
    } catch (Exception e) {
      return;
    }

    allHubs = new MasterHub(authManager, authManager.getUsers(), allDevices);

    this.primaryStage = primaryStage;

    Token authToken = authManager.getToken("admin", "admin");

    cameraModel = new CameraModel(authToken, allHubs);
    CameraController cameraController =
        new CameraController(
            cameraModel,
            authManager,
            (from, to, token) -> {
              this.transition(from, to, token);
            });
    CameraView cameraView = new CameraView(cameraController, cameraModel);
    scene = new Scene(cameraView.asParent(), 960, 480);
    this.primaryStage.setScene(scene);
    this.primaryStage.show();
    cameraModel.stopAllPlayers();
  }

  public void transition(Controller from, Views to, Token token) {}

  // GIVEN a camera WHEN I click "Start" on the Client camera control THEN the camera turns on and I
  // see the data from the Camera
  // GIVEN a functioning camera And I am able to see the data from the Camera WHEN I click on record
  // on the Client Camera control THEN my Camera starts recording
  // GIVEN a functioning Camera is recording WHEN I click on stop recording on the Client Camera
  // control THEN my Camera stops recording
  // GIVEN a functioning camera WHEN I click "Turn Off" on the Client camera control THEN the camera
  // shuts down and I do not see the data from the Camera
  // GIVEN I want to see a Camera device WHEN That Camera has integrated video streaming THEN I can
  // see the video's live feed
  @Test
  public void testToggle() {
    // when I click ON then the camera turns on and I see the feed:
    clickOn("ON");

    // sleep since retrieval takes time
    sleep(5000);
    // then: I can see the video's live feed
    assertEquals(cameraModel.getMediaPlayer(0).getStatus().equals(Status.PLAYING), true);

    // given: functioning camera that is not full
    assertNotEquals(camera.getDiskPercentageUsed(), 100);

    boolean before = camera.isRecording();
    // when: click record
    clickOn("Record");
    // it records
    assertNotEquals(before, camera.isRecording());

    // given: camera is recording
    before = camera.isRecording();
    // when: click stop
    clickOn("Stop");
    assertNotEquals(before, camera.isRecording());

    clickOn("OFF");
    clickOn("NO FEED");
  }
}
