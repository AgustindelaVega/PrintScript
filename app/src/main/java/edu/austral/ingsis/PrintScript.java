package edu.austral.ingsis;

import picocli.CommandLine;

public class PrintScript {

  public static void main(String[] args) {
    new CommandLine(new CLI()).execute(args);
  }
}
