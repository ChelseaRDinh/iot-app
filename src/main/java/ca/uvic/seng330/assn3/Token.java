package ca.uvic.seng330.assn3;

public final class Token {
  private byte[] contents;

  Token(byte[] contents) {
    this.contents = contents;
  }

  public byte[] getBytes() {
    return contents.clone();
  }

  public String toString() {
    return new String(contents);
  }
}
