package com.grahambartley.terminalchess.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VerticalSpaceIndex {
  A(0, "A"),
  B(1, "B"),
  C(2, "C"),
  D(3, "D"),
  E(4, "E"),
  F(5, "F"),
  G(6, "G"),
  H(7, "H");

  private final int index;
  private final String name;

  public static String getNameByIndex(int index) {
    for (VerticalSpaceIndex e : values()) {
      if (e.getIndex() == index) {
        return e.getName();
      }
    }
    return null;
  }

  public static Integer getIndexByName(String name) {
    for (VerticalSpaceIndex e : values()) {
      if (e.getName().equalsIgnoreCase(name)) {
        return e.getIndex();
      }
    }
    return null;
  }
}