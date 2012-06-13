package org.zeroturnaround.test;

import junit.framework.TestCase;

import org.zeroturnaround.Settings;

public class SettingsTests extends TestCase {
  public void testOpenSSLSimple() {
    String openSSLString = Settings.getOpenSSLExec();
    assertTrue(openSSLString!=null);
    assertTrue(openSSLString.contains("openssl"));
  }
}
