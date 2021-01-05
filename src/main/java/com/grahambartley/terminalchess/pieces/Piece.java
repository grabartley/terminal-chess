package com.grahambartley.terminalchess.pieces;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;

import com.grahambartley.terminalchess.utils.CommonUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public abstract class Piece {
  public boolean isWhite;
  public boolean isAbleToJump;
  public boolean hasMoved;
  public String emoji;
  public List<Space> validMoveSet;

  public Optional<Piece> simulateMove(Space currentSpace, Space proposedSpace) {
    Optional<Piece> capturedPiece = proposedSpace.hasPiece() ?
      Optional.of(proposedSpace.getPiece()) :
      Optional.empty();
    currentSpace.setPiece(null);
    proposedSpace.setPiece(this);
    return capturedPiece;
  }

  public Optional<Piece> move(Space currentSpace, Space proposedSpace) {
    Optional<Piece> capturedPiece = simulateMove(currentSpace, proposedSpace);
    this.hasMoved = true;
    return capturedPiece;
  }

  public boolean isValidMove(Space currentSpace, Space proposedSpace, Board board) {
    boolean capturingYourOwnPiece = proposedSpace.hasPiece() && proposedSpace.getPiece().isWhite == isWhite;
    boolean pathIsBlockedAndCantJump = !isAbleToJump && !board.isPathClear(currentSpace, proposedSpace);
    if (capturingYourOwnPiece || pathIsBlockedAndCantJump) {
      return false;
    }

    int hDiff = CommonUtil.getHDiff(currentSpace, proposedSpace);
    int vDiff = CommonUtil.getVDiff(currentSpace, proposedSpace);

    return isValidMove(hDiff, vDiff, proposedSpace.hasPiece());
  }

  public abstract boolean isValidMove(int hDiff, int vDiff, boolean isCapturing);

  public void calculateValidMoveSet(Space currentSpace, Board board) {
    List<Space> localValidMoveSet = new ArrayList<>();
    for (Space space : board.getAllSpaces()) {
      if (isValidMove(currentSpace, space, board)) {
        localValidMoveSet.add(space);
      }
    }
    validMoveSet = localValidMoveSet;
  }
}