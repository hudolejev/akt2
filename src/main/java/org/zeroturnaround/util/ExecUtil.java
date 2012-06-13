package org.zeroturnaround.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

public class ExecUtil {
  private static final Logger log = Logger.getLogger(ExecUtil.class.getName());
  
  public static String exec(String cmd) {
    CommandLine cmdLine = CommandLine.parse(cmd);
    DefaultExecutor exec = new DefaultExecutor();
    ExecuteStreamHandler handler = exec.getStreamHandler();

    ByteArrayOutputStream stdout = new ByteArrayOutputStream();
    PumpStreamHandler psh = new PumpStreamHandler(stdout);
    exec.setStreamHandler(psh);

    try {
      handler.setProcessOutputStream(System.in);
    }
    catch (IOException e) {
      log.debug("Unable to change stdout for command '"+cmd+"'", e);
    }

    try {
      exec.execute(cmdLine);
    }
    catch (Exception e) {
      log.debug("Unable to execute '"+cmd+"'", e);
    }
    return stdout.toString();
  }
}
