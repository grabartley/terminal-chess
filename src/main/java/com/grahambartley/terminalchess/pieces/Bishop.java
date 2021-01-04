package com.grahambartley.terminalchess.pieces;

import static java.lang.Math.abs;

public class Bishop extends Piece {
  public Bishop(boolean isWhite) {
    this.isWhite = isWhite;
    this.isAbleToJump = false;
    this.emoji = isWhite ? "♗" : "♝";
  }

  @Override
  public boolean isValidMove(int hDiff, int vDiff, boolean isCapturing) {
    return abs(hDiff) == abs(vDiff);
  }
}