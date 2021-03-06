package edu.austral.ingsis;

import java.io.File;
import java.util.function.Consumer;
import picocli.CommandLine;

public class CLI {

  public static void main(String[] args) {
    new CommandLine(new PrintScript()).execute(args);
  }

  public static void executeWithPrintAndErrorConsumers(
      File file, String version, Consumer<String> printConsumer, Consumer<String> errorConsumer) {
    new CommandLine(new PrintScript(printConsumer, errorConsumer, version, file)).execute();
  }
}
