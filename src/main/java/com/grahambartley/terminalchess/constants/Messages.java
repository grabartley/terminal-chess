package com.grahambartley.terminalchess.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Messages {
  INVALID_MOVE_ERROR("Invalid move");

  private final String text;
}