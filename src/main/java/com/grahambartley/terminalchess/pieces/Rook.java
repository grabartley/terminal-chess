package com.grahambartley.terminalchess.pieces;

public class Rook extends Piece {
  public Rook(boolean isWhite) {
    this.isWhite = isWhite;
    this.isAbleToJump = false;
    this.emoji = isWhite ? "♖" : "♜";
  }

  @Override
  public boolean isInvalidMove(int hDiff, int vDiff, boolean isCapturing) {
    boolean isMovingHorizontally = hDiff == 0 && vDiff != 0;
    boolean isMovingVertically = vDiff == 0 && hDiff != 0;
    return !(isMovingHorizontally || isMovingVertically);
  }
}