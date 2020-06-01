package com.grahambartley.terminalchess.pieces;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Piece {
  public boolean active;
  public boolean isWhite;
  public boolean isAbleToJump;
  public String emoji;

  public boolean isInvalidMove(Space currentSpace, Space proposedSpace, Board board) {
    boolean capturingYourOwnPiece = proposedSpace.hasPiece() && proposedSpace.getPiece().isWhite == isWhite;
    boolean pathIsBlockedAndCantJump = !isAbleToJump && !board.isPathClear(currentSpace, proposedSpace);
    if (capturingYourOwnPiece || pathIsBlockedAndCantJump) {
      return true;
    }
    int currentHIndex = HorizontalSpaceIndex.getIndexByName(currentSpace.getH());
    int currentVIndex = VerticalSpaceIndex.getIndexByName(currentSpace.getV());
    int proposedHIndex = HorizontalSpaceIndex.getIndexByName(proposedSpace.getH());
    int proposedVIndex = VerticalSpaceIndex.getIndexByName(proposedSpace.getV());
    int hDiff = proposedHIndex - currentHIndex;
    int vDiff = proposedVIndex - currentVIndex;

    return isInvalidMove(hDiff, vDiff, proposedSpace.hasPiece());
  }

  public abstract boolean isInvalidMove(int hDiff, int vDiff, boolean isCapturing);

  public void move(Space currentSpace, Space proposedSpace) {
    currentSpace.setPiece(null);
    proposedSpace.setPiece(this);
  }
}