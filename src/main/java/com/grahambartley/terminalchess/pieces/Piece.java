package com.grahambartley.terminalchess.pieces;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;

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

    return false;
  }

  public void move(Space currentSpace, Space proposedSpace) {
    currentSpace.setPiece(null);
    proposedSpace.setPiece(this);
  }
}