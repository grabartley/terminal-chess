package com.grahambartley.terminalchess.pieces;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;

public class Rook extends Piece {
  public Rook(boolean isWhite) {
    this.active = true;
    this.isWhite = isWhite;
    this.isAbleToJump = false;
    this.emoji = isWhite ? "♖" : "♜";
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
    boolean isMovingHorizontally = hDiff == 0 && vDiff != 0;
    boolean isMovingVertically = vDiff == 0 && hDiff != 0;
    return !(isMovingHorizontally || isMovingVertically);
  }
}