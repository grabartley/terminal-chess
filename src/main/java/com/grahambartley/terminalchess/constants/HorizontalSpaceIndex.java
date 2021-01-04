package com.grahambartley.terminalchess.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HorizontalSpaceIndex {
  One(0, "1"),
  Two(1, "2"),
  Three(2, "3"),
  Four(3, "4"),
  Five(4, "5"),
  Six(5, "6"),
  Seven(6, "7"),
  Eight(7, "8");

  private final int index;
  private final String name;

  public static String getNameByIndex(int index) {
    for (HorizontalSpaceIndex e : values()) {
      if (e.getIndex() == index) {
        return e.getName();
      }
    }
    return null;
  }

  public static Integer getIndexByName(String name) {
    for (HorizontalSpaceIndex e : values()) {
      if (e.getName().equals(name)) {
        return e.getIndex();
      }
    }
    return null;
  }
}