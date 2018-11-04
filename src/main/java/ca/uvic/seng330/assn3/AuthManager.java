package ca.uvic.seng330.assn3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AuthManager {
  MessageDigest sha256;
  Cipher encrypt;
  Cipher decrypt;

  private final String privateKey = "secretlol1234567";
  private final String initializationVector = "supersecretIV890";
  private final String userDatabasePath = "userdb.txt";
  private HashMap<String, String> users = new HashMap<String, String>();

  public AuthManager()
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
          InvalidAlgorithmParameterException {

    /* String password = "admin";
    byte[] hash = sha256.digest(password.getBytes(StandardCharsets.UTF_8));
    String hashedString = new String(hash, StandardCharsets.UTF_8);
    users.put("admin", hashedString);
    saveDatabase();
    users.clear(); */

    loadCrypto();
    loadDatabase();
  }

  /**
   * Gets a token for use in auth transactions
   *
   * @param username the username of the user
   * @param password the password of the user
   * @return a non-empty token if the username and password pair is valid.
   */
  public String getToken(String username, String password) {
    // If there's no such user, return empty token.
    if (!users.containsKey(username)) {
      return "";
    }

    byte[] hash = sha256.digest(password.getBytes(StandardCharsets.UTF_8));
    String hashedString = new String(hash, StandardCharsets.UTF_8);

    // If the entered password isn't the same as the user's password, return empty token.
    if (!users.get(username).equals(hashedString)) {
      return "";
    }

    String combination = username + "," + hashedString;

    try {
      byte[] encrypted = encrypt.doFinal(combination.getBytes());
      String token = new String(encrypted, StandardCharsets.UTF_8);

      return token;
    } catch (Exception e) {
      return "";
    }
  }

  public boolean isValidToken(String token) {
    String decryptedToken = getDecryptedText(token);
    String[] tokenParts = decryptedToken.split(",");

    return getTokenPartValidity(tokenParts);
  }

  public boolean isAdminToken(String token) {
    String decryptedToken = getDecryptedText(token);
    String[] tokenParts = decryptedToken.split(",");

    if (!getTokenPartValidity(tokenParts)) {
      return false;
    }

    return tokenParts[0].equals("admin");
  }

  private String getDecryptedText(String text) {
    try {
      byte[] decrypted = decrypt.doFinal(text.getBytes());
      String decryptedText = new String(decrypted, StandardCharsets.UTF_8);

      return decryptedText;
    } catch (Exception e) {
      return "";
    }
  }

  private boolean getTokenPartValidity(String[] tokenParts) {
    if (tokenParts.length != 2) {
      return false;
    }

    if (!users.containsKey(tokenParts[0])) {
      return false;
    }

    if (!users.get(tokenParts[0]).equals(tokenParts[1])) {
      return false;
    }

    return true;
  }

  private void loadCrypto()
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
          InvalidAlgorithmParameterException {
    Key key = new SecretKeySpec(privateKey.getBytes(), "AES");
    IvParameterSpec iv = new IvParameterSpec(initializationVector.getBytes());

    // Load the algorithms used, they shouldn't throw, but could because "NO/TYPE/SafetyLol".
    sha256 = MessageDigest.getInstance("SHA-256");
    encrypt = Cipher.getInstance("AES/CFB/NoPadding");
    decrypt = Cipher.getInstance("AES/CFB/NoPadding");

    encrypt.init(Cipher.ENCRYPT_MODE, key, iv);
    decrypt.init(Cipher.DECRYPT_MODE, key, iv);
  }

  private void loadDatabase() {
    try {
      File file = new File(userDatabasePath);
      Scanner fileInput = new Scanner(file, StandardCharsets.UTF_8.name());

      while (fileInput.hasNextLine()) {
        String username = fileInput.nextLine();

        if (!fileInput.hasNextLine()) {
          break;
        }

        String password = fileInput.nextLine();

        users.put(username, password);
      }

      fileInput.close();
    } catch (Exception e) {
      return;
    }
  }

  private void saveDatabase() {
    File file = new File(userDatabasePath);
    try {
      file.delete();
    } catch (Exception e) {
    }

    // Try to create the database file, return if it fails.
    try {
      file.createNewFile();
    } catch (Exception e) {
      return;
    }

    try {
      OutputStreamWriter out =
          new OutputStreamWriter(new FileOutputStream(userDatabasePath), StandardCharsets.UTF_8);

      for (HashMap.Entry<String, String> entry : users.entrySet()) {
        String username = entry.getKey();
        String passwordHash = entry.getValue();

        out.write(username);
        out.write("\n");
        out.write(passwordHash);
      }

      out.close();
    } catch (Exception e) {
      return;
    }
  }
}
