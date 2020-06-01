package com.grahambartley.terminalchess.board;

import static java.util.Objects.isNull;

import java.util.ArrayList;

import static java.util.Collections.emptyList;
import static java.lang.Math.abs;

import java.util.List;
import java.util.Optional;

import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;
import com.grahambartley.terminalchess.pieces.Bishop;
import com.grahambartley.terminalchess.pieces.King;
import com.grahambartley.terminalchess.pieces.Knight;
import com.grahambartley.terminalchess.pieces.Pawn;
import com.grahambartley.terminalchess.pieces.Queen;
import com.grahambartley.terminalchess.pieces.Rook;

public class Board {
  private static Board instance;
  private Space[][] board;

  private Board() {
    this.board = new Space[8][8];

    // add spaces to the board
    for (int i = 0; i < 8; i++) {
      String h = HorizontalSpaceIndex.getNameByIndex(i);
      for (int j = 0; j < 8; j++) {
        String v = VerticalSpaceIndex.getNameByIndex(j);
        board[i][j] = new Space(h, v, null);
      }
    }
  }

  public static Board getInstance() {
    if (isNull(instance)) {
      instance = new Board();
    }

    return instance;
  }

  public void setUpPieces() {
    for (int i = 0; i < 8; i++) {
      board[1][i].setPiece(new Pawn(true));
      board[6][i].setPiece(new Pawn(false));
    }
    board[0][0].setPiece(new Rook(true));
    board[0][1].setPiece(new Knight(true));
    board[0][2].setPiece(new Bishop(true));
    board[0][3].setPiece(new Queen(true));
    board[0][4].setPiece(new King(true));
    board[0][5].setPiece(new Bishop(true));
    board[0][6].setPiece(new Knight(true));
    board[0][7].setPiece(new Rook(true));

    board[7][0].setPiece(new Rook(false));
    board[7][1].setPiece(new Knight(false));
    board[7][2].setPiece(new Bishop(false));
    board[7][3].setPiece(new King(false));
    board[7][4].setPiece(new Queen(false));
    board[7][5].setPiece(new Bishop(false));
    board[7][6].setPiece(new Knight(false));
    board[7][7].setPiece(new Rook(false));
  }

  public Optional<Space> getSpace(char h, char v) {
    return getSpace(String.valueOf(h), String.valueOf(v));
  }

  public Optional<Space> getSpace(String h, String v) {
    Integer hIndex = HorizontalSpaceIndex.getIndexByName(h);
    Integer vIndex = VerticalSpaceIndex.getIndexByName(v);

    try {
      return Optional.of(board[hIndex][vIndex]);
    } catch (IndexOutOfBoundsException | NullPointerException e) {
      return Optional.empty();
    }
  }

  public boolean isPathClear(Space currentSpace, Space proposedSpace) {
    return !getSpacesBetween(currentSpace, proposedSpace)
      .stream()
      .anyMatch(Space::hasPiece);
  }

  protected List<Space> getSpacesBetween(Space currentSpace, Space proposedSpace) {
    int currentHIndex = HorizontalSpaceIndex.getIndexByName(currentSpace.getH());
    int currentVIndex = VerticalSpaceIndex.getIndexByName(currentSpace.getV());
    int proposedHIndex = HorizontalSpaceIndex.getIndexByName(proposedSpace.getH());
    int proposedVIndex = VerticalSpaceIndex.getIndexByName(proposedSpace.getV());
    int hDiff = proposedHIndex - currentHIndex;
    int vDiff = proposedVIndex - currentVIndex;

    // not moving at all
    if (hDiff == 0 && vDiff == 0) {
      return emptyList();
    }

    // moving to adjacent space
    if (hDiff == 1 || hDiff == -1 || vDiff == 1 || vDiff == -1) {
      return emptyList();
    }

    List<Space> spacesBetween = new ArrayList<>();

    // moving diagonally
    if (abs(hDiff) == abs(vDiff)) {
      int numOfSpacesBetween = abs(hDiff) - 1;

      for (int i = 1; i <= numOfSpacesBetween; i++) {
        int hIndex = currentHIndex + (hDiff < 0 ? -1 * i : i);
        int vIndex = currentVIndex + (vDiff < 0 ? -1 * i : i);
        spacesBetween.add(board[hIndex][vIndex]);
      }

      return spacesBetween;
    }

    // moving horizontally
    if (hDiff == 0) {
      int numOfSpacesBetween = abs(vDiff) - 1;

      for (int i = 1; i <= numOfSpacesBetween; i++) {
        int vIndex = currentVIndex + (vDiff < 0 ? -1 * i : i);
        spacesBetween.add(board[currentHIndex][vIndex]);
      }

      return spacesBetween;
    // moving vertically
    } else {
      int numOfSpacesBetween = abs(hDiff) - 1;

      for (int i = 1; i <= numOfSpacesBetween; i++) {
        int hIndex = currentHIndex + (hDiff < 0 ? -1 * i : i);
        spacesBetween.add(board[hIndex][currentVIndex]);
      }

      return spacesBetween;
    }
  }
}