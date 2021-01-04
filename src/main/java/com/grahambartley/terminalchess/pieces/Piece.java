package com.grahambartley.terminalchess.pieces;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;

import lombok.Getter;
import lombok.Setter;

import static java.util.Objects.isNull;

@Getter
@Setter
public abstract class Piece {
  public boolean isWhite;
  public boolean isAbleToJump;
  public String emoji;

  public boolean isValidMove(Space currentSpace, Space proposedSpace, Board board) {
    boolean capturingYourOwnPiece = proposedSpace.hasPiece() && proposedSpace.getPiece().isWhite == isWhite;
    boolean pathIsBlockedAndCantJump = !isAbleToJump && !board.isPathClear(currentSpace, proposedSpace);
    if (capturingYourOwnPiece || pathIsBlockedAndCantJump) {
      return true;
    }
    Integer currentHIndex = HorizontalSpaceIndex.getIndexByName(currentSpace.getH());
    Integer currentVIndex = VerticalSpaceIndex.getIndexByName(currentSpace.getV());
    Integer proposedHIndex = HorizontalSpaceIndex.getIndexByName(proposedSpace.getH());
    Integer proposedVIndex = VerticalSpaceIndex.getIndexByName(proposedSpace.getV());

    if (
      isNull(currentHIndex) ||
      isNull(currentVIndex) ||
      isNull(proposedHIndex) ||
      isNull(proposedVIndex)
    ) {
      return true;
    }
    int hDiff = proposedHIndex - currentHIndex;
    int vDiff = proposedVIndex - currentVIndex;

    return isValidMove(hDiff, vDiff, proposedSpace.hasPiece());
  }

  public abstract boolean isValidMove(int hDiff, int vDiff, boolean isCapturing);

  public void move(Space currentSpace, Space proposedSpace) {
    currentSpace.setPiece(null);
    proposedSpace.setPiece(this);
  }
}