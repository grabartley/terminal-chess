package com.grahambartley.terminalchess.constants;

import lombok.Getter;

@Getter
public enum State {
  MENU(),
  PLAY(),
  EXIT();

  public static State getByName(String name) {
    if (name.equalsIgnoreCase("M")) {
      return MENU;
    }
    if (name.equalsIgnoreCase("P")) {
      return PLAY;
    }
    if (name.equalsIgnoreCase("E")) {
      return EXIT;
    }

    return null;
  }
}