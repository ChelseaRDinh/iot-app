package ca.uvic.seng330.assn3;

public class Controller {
  protected AuthManager authManager;
  private ViewTransition transitionNotifier;

  public Controller(AuthManager authManager, ViewTransition transitionNotifier) {
    this.authManager = authManager;
    this.transitionNotifier = transitionNotifier;
  }

  protected void switchViews(Controller from, Views to, String token) {
    transitionNotifier.transition(from, to, token);
  }
}
