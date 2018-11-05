package ca.uvic.seng330.assn3;

import static org.junit.Assert.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import ca.uvic.seng330.assn3.devices.devicesViews.LightbulbView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.matcher.control.TextInputControlMatchers;

public class LightbulbViewTest extends ApplicationTest {
    
    @Override
    public void start(Stage primaryStage) {
        LightbulbView view = new LightbulbView();
        Scene scene = new Scene(view.asParent(), 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
      }
    
      @Test
      public void testToggle() {
          //when:
          clickOn(".button");

          //then:
      }
}