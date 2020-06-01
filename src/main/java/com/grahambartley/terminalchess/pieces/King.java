package com.grahambartley.terminalchess.pieces;

import static java.lang.Math.abs;

public class King extends Piece {
  public King(boolean isWhite) {
    this.active = true;
    this.isWhite = isWhite;
    this.isAbleToJump = false;
    this.emoji = isWhite ? "♔" : "♚";
  }

  @Override
  public boolean isInvalidMove(int hDiff, int vDiff, boolean isCapturing) {
    boolean isMovingOneSquareInAnyDirection = 
      (abs(hDiff) == 1 && abs(vDiff) == 1) ||
      (hDiff == 0 && abs(vDiff) == 1) ||
      (vDiff == 0 && abs(hDiff) == 1);
    return !isMovingOneSquareInAnyDirection;
  }
}