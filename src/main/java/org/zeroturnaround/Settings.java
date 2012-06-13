package org.zeroturnaround;

import org.apache.log4j.Logger;
import org.zeroturnaround.util.ExecUtil;

public class Settings {
  private static final Logger log = Logger.getLogger(Settings.class.getName());

  private static String openSSLExec;

  public static String getOpenSSLExec() {
    if (openSSLExec == null) {
      initSSL();
    }
    return openSSLExec;
  }

  private static void initSSL() {
    String line = "which openssl";
    openSSLExec = ExecUtil.exec(line);
    if (openSSLExec == null) {
      throw new RuntimeException("Unable to find OpenSSL");
    }
  }
}
