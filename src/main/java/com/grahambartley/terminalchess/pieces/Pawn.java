package com.grahambartley.terminalchess.pieces;

import static java.lang.Math.abs;

import com.grahambartley.terminalchess.board.Space;

public class Pawn extends Piece {
  private boolean hasMoved;

  public Pawn(boolean isWhite) {
    this.isWhite = isWhite;
    this.isAbleToJump = false;
    this.emoji = isWhite ? "♙" : "♟";
    this.hasMoved = false;
  }

  @Override
  public boolean isValidMove(int hDiff, int vDiff, boolean isCapturing) {
    boolean isMovingDiagonallyForwardOne = abs(hDiff) == abs(vDiff) && (isWhite ? hDiff == 1 : hDiff == -1);
    boolean isMovingForwardOne = vDiff == 0 && (isWhite ? hDiff == 1 : hDiff == -1);
    boolean isMovingForwardTwoOnFirstMove = vDiff == 0 && !hasMoved && (isWhite ? hDiff == 2 : hDiff == -2);
    if (isCapturing) {
      return isMovingDiagonallyForwardOne;
    }
    return isMovingForwardOne || isMovingForwardTwoOnFirstMove;
  }

  @Override
  public void move(Space currentSpace, Space proposedSpace) {
    super.move(currentSpace, proposedSpace);
    hasMoved = true;
  }
}