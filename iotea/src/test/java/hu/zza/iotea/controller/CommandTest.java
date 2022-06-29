package hu.zza.iotea.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hu.zza.iotea.model.Command;
import org.junit.jupiter.api.Test;

class CommandTest {
  private final Command commandA = new Command(111, "A", "TemplateA", "NoteA");
  private final Command commandB = new Command(111, "B", "TemplateB", "NoteB");

  @Test
  void equals() {
    assertEquals(commandA, commandB);
  }
}
