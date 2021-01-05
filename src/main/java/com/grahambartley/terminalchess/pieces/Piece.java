package com.grahambartley.terminalchess.pieces;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;

import com.grahambartley.terminalchess.utils.CommonUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
public abstract class Piece {
  public boolean isWhite;
  public boolean isAbleToJump;
  public String emoji;
  public List<Space> validMoveSet;

  public void move(Space currentSpace, Space proposedSpace) {
    currentSpace.setPiece(null);
    proposedSpace.setPiece(this);
  }

  public boolean isValidMoveBasic(Space currentSpace, Space proposedSpace, Board board) {
    boolean capturingYourOwnPiece = proposedSpace.hasPiece() && proposedSpace.getPiece().isWhite == isWhite;
    boolean pathIsBlockedAndCantJump = !isAbleToJump && !board.isPathClear(currentSpace, proposedSpace);
    if (capturingYourOwnPiece || pathIsBlockedAndCantJump) {
      return false;
    }

    int hDiff = CommonUtil.getHDiff(currentSpace, proposedSpace);
    int vDiff = CommonUtil.getVDiff(currentSpace, proposedSpace);

    return isValidMove(hDiff, vDiff, proposedSpace.hasPiece());
  }

  public boolean isValidMoveAdvanced(Space currentSpace, Space proposedSpace, Board board) {
    boolean isMovingIntoCheck = isPieceMovingIntoCheck(currentSpace, proposedSpace, board);

    return isValidMoveBasic(currentSpace, proposedSpace, board) && !isMovingIntoCheck;
  }

  public abstract boolean isValidMove(int hDiff, int vDiff, boolean isCapturing);

  public void calculateValidMoveSet(Space currentSpace, Board board) {
    List<Space> localValidMoveSet = new ArrayList<>();
    for (Space space : board.getAllSpaces()) {
      if (isValidMoveBasic(currentSpace, space, board)) {
        localValidMoveSet.add(space);
      }
    }
    validMoveSet = localValidMoveSet;
  }

  private boolean isPieceMovingIntoCheck(Space currentSpace, Space proposedSpace, Board board) {
    boolean isPieceMovingIntoCheck = false;
    move(currentSpace, proposedSpace);
    Space friendlyKingSpace = board.getSpacesContainingPiece(King.class, isWhite).get(0);
    List<Piece> enemyPieces = board.getActiveSpaces(!isWhite).stream()
      .map(Space::getPiece)
      .collect(toList());
    for (Piece enemyPiece : enemyPieces) {
      if (enemyPiece.getValidMoveSet().contains(friendlyKingSpace)) {
        isPieceMovingIntoCheck = true;
        break;
      }
    }
    move(proposedSpace, currentSpace);
    return isPieceMovingIntoCheck;
  }
}