package ca.uvic.seng330.assn3;

public class Model {
  private final Token token;

  public Model(Token token) {
    this.token = token;
  }

  public Token getToken() {
    return new Token(token.getBytes());
  }
}
