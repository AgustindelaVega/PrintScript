package edu.austral.ingsis;

import java.util.function.Consumer;
import picocli.CommandLine;

public class PrintScript {

  public static void main(String[] args) {
    new CommandLine(new CLI()).execute(args);
  }

  public static void executeWithPrintAndErrorConsumers(
      String[] args, Consumer<String> printConsumer, Consumer<String> errorConsumer) {
    new CommandLine(new CLI(printConsumer, errorConsumer)).execute(args);
  }
}
