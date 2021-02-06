package com.grahambartley.terminalchess.pieces;

import static java.lang.Math.abs;

public class Queen extends Piece {
  public Queen(boolean isWhite) {
    this.isWhite = isWhite;
    this.isAbleToJump = false;
    this.emoji = isWhite ? "♕" : "♛";
  }

  @Override
  public boolean isValidMove(int hDiff, int vDiff, boolean isCapturing) {
    boolean isMovingHorizontally = hDiff == 0 && vDiff != 0;
    boolean isMovingVertically = vDiff == 0 && hDiff != 0;
    boolean isMovingDiagonally = hDiff != 0 && abs(hDiff) == abs(vDiff);
    return isMovingHorizontally || isMovingVertically || isMovingDiagonally;
  }
}