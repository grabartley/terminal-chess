package com.grahambartley.terminalchess.pieces;

public class Queen extends Piece {
  public Queen(boolean isWhite) {
    this.active = true;
    this.isWhite = isWhite;
    this.isAbleToJump = false;
    this.emoji = isWhite ? "♕" : "♛";
  }

  @Override
  public boolean isInvalidMove(int hDiff, int vDiff, boolean isCapturing) {
    return false;
  }
}