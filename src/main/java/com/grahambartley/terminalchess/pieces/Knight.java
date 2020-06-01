package com.grahambartley.terminalchess.pieces;

import static java.lang.Math.abs;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;

public class Knight extends Piece {
  public Knight(boolean isWhite) {
    this.active = true;
    this.isWhite = isWhite;
    this.isAbleToJump = true;
    this.emoji = isWhite ? "♘" : "♞";
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
    boolean isMovingInLShape = (abs(hDiff) == 1 && abs(vDiff) == 2) || (abs(hDiff) == 2 && abs(vDiff) == 1);
    return !isMovingInLShape;
  }
}