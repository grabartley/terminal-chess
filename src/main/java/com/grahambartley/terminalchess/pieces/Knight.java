package com.grahambartley.terminalchess.pieces;

import static java.lang.Math.abs;

public class Knight extends Piece {
  public Knight(boolean isWhite) {
    this.isWhite = isWhite;
    this.isAbleToJump = true;
    this.emoji = isWhite ? "♘" : "♞";
  }

  @Override
  public boolean isValidMove(int hDiff, int vDiff, boolean isCapturing) {
    return (abs(hDiff) == 1 && abs(vDiff) == 2) || (abs(hDiff) == 2 && abs(vDiff) == 1);
  }
}