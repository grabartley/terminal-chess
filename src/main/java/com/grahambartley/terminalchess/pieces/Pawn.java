package com.grahambartley.terminalchess.pieces;

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
    boolean isMovingForwardOne = isWhite ? proposedHIndex == currentHIndex + 1 : proposedHIndex == currentHIndex - 1;
    boolean isMovingLeftOrRightOne = proposedVIndex == currentVIndex + 1 || proposedVIndex == currentVIndex - 1;
    boolean isMovingForwardTwoOnFirstMove = !hasMoved && (isWhite ? proposedHIndex == currentHIndex + 2 : proposedHIndex == currentHIndex - 2);
    if (proposedSpace.hasPiece()) {
      return !(isMovingForwardOne && isMovingLeftOrRightOne);
    }
    return !(isMovingForwardOne || isMovingForwardTwoOnFirstMove);
  }

  @Override
  public void move(Space currentSpace, Space proposedSpace) {
    super.move(currentSpace, proposedSpace);
    hasMoved = true;
  }
}