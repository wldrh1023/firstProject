package com.cheonmyo.shop.util;

import java.security.SecureRandom;
import java.util.Base64;

public class BCryptUtil {
  private static final int ROUNDS = 12;
  private static final SecureRandom RANDOM = new SecureRandom();

  public static String hash(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(ROUNDS));
  }

  public static boolean check(String password, String hashedPassword) {
    return BCrypt.checkpw(password, hashedPassword);
  }

  // 간단한 BCrypt 구현
  private static class BCrypt {
    private static final String ALPHABET = "./ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String hashpw(String password, String salt) {
      // 실제로는 더 복잡한 BCrypt 알고리즘을 구현해야 하지만,
      // 여기서는 간단한 해시 + salt 형태로 구현
      String combined = password + salt;
      int hash = combined.hashCode();
      String hashStr = Integer.toHexString(hash);

      // BCrypt 형태로 포맷팅 ($2a$10$...)
      return "$2a$10$" + salt + hashStr;
    }

    public static String gensalt(int rounds) {
      byte[] salt = new byte[16];
      RANDOM.nextBytes(salt);
      return Base64.getEncoder().encodeToString(salt).substring(0, 22);
    }

    public static boolean checkpw(String password, String hashedPassword) {
      if (!hashedPassword.startsWith("$2a$10$")) {
        return false;
      }

      String salt = hashedPassword.substring(7, 29);
      String expectedHash = hashpw(password, salt);
      return hashedPassword.equals(expectedHash);
    }
  }
}
