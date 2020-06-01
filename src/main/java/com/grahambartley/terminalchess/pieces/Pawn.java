package com.grahambartley.terminalchess.pieces;

import static java.lang.Math.abs;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;

public class Pawn extends Piece {
  private boolean hasMoved;

  public Pawn(boolean isWhite) {
    this.active = true;
    this.isWhite = isWhite;
    this.isAbleToJump = false;
    this.emoji = isWhite ? "♙" : "♟";
    this.hasMoved = false;
  }

  @Override
  public boolean isInvalidMove(Space currentSpace, Space proposedSpace, Board board) {
    if (super.isInvalidMove(currentSpace, proposedSpace, board)) {
      return true;
    }
    int currentHIndex = HorizontalSpaceIndex.getIndexByName(currentSpace.getH());
    int currentVIndex = VerticalSpaceIndex.getIndexByName(currentSpace.getV());
    int proposedHIndex = HorizontalSpaceIndex.getIndexByName(proposedSpace.getH());
    int proposedVIndex = VerticalSpaceIndex.getIndexByName(proposedSpace.getV());
    int hDiff = proposedHIndex - currentHIndex;
    int vDiff = proposedVIndex - currentVIndex;
    boolean isMovingForwardOne = vDiff == 0 && (isWhite ? hDiff == 1 : hDiff == -1);
    boolean isMovingDiagonallyForwardOne = abs(hDiff) == abs(vDiff) && (isWhite ? hDiff == 1 : hDiff == -1);
    boolean isMovingForwardTwoOnFirstMove = vDiff == 0 && !hasMoved && (isWhite ? hDiff == 2 : hDiff == -2);
    if (proposedSpace.hasPiece()) {
      return !isMovingDiagonallyForwardOne;
    }
    return !(isMovingForwardOne || isMovingForwardTwoOnFirstMove);
  }

  @Override
  public void move(Space currentSpace, Space proposedSpace) {
    super.move(currentSpace, proposedSpace);
    hasMoved = true;
  }
}