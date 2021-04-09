package edu.austral.ingsis;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class FileReader {
  public static String getFileLines(String src) {
    InputStreamReader inputReader = null;
    try {
      inputReader = new InputStreamReader(new FileInputStream(src));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    assert inputReader != null;
    return new BufferedReader(inputReader).lines().collect(Collectors.joining("\n"));
  }
}
