package com.grahambartley.terminalchess.constants;

import lombok.Getter;

@Getter
public enum State {
  MENU(),
  NEW_GAME(),
  PLAY(),
  CHECKMATE(),
  EXIT();

  public static State getByName(String name) {
    if (name.equalsIgnoreCase("M")) {
      return MENU;
    }
    if (name.equalsIgnoreCase("N")) {
      return NEW_GAME;
    }
    if (name.equalsIgnoreCase("P")) {
      return PLAY;
    }
    if (name.equalsIgnoreCase("C")) {
      return CHECKMATE;
    }
    if (name.equalsIgnoreCase("E")) {
      return EXIT;
    }

    return null;
  }
}