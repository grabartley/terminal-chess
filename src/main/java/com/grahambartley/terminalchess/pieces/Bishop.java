package com.grahambartley.terminalchess.pieces;

import static java.lang.Math.abs;

public class Bishop extends Piece {
  public Bishop(boolean isWhite) {
    this.active = true;
    this.isWhite = isWhite;
    this.isAbleToJump = false;
    this.emoji = isWhite ? "♗" : "♝";
  }

  @Override
  public boolean isInvalidMove(int hDiff, int vDiff, boolean isCapturing) {
    boolean isMovingDiagonally = abs(hDiff) == abs(vDiff);
    return !isMovingDiagonally;
  }
}