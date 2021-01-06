package com.grahambartley.terminalchess.board;

import static java.util.Objects.isNull;

import static java.util.Collections.emptyList;
import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;
import com.grahambartley.terminalchess.pieces.*;
import com.grahambartley.terminalchess.utils.CommonUtil;

public class Board {
  private static Board instance;
  protected final Space[][] board;
  private final List<Space> allSpacesFlattened;

  private Board() {
    this.board = new Space[8][8];
    this.allSpacesFlattened = new ArrayList<>();

    // add spaces to the board
    for (int i = 0; i < 8; i++) {
      String h = HorizontalSpaceIndex.getNameByIndex(i);
      for (int j = 0; j < 8; j++) {
        String v = VerticalSpaceIndex.getNameByIndex(j);
        Space space = new Space(h, v, null);
        board[i][j] = space;
        allSpacesFlattened.add(space);
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
    board[0][0].setPiece(new Rook(true));
    board[0][1].setPiece(new Knight(true));
    board[0][2].setPiece(new Bishop(true));
    board[0][3].setPiece(new Queen(true));
    board[0][4].setPiece(new King(true));
    board[0][5].setPiece(new Bishop(true));
    board[0][6].setPiece(new Knight(true));
    board[0][7].setPiece(new Rook(true));
    for (int i = 0; i < 8; i++) {
      board[1][i].setPiece(new Pawn(true));
      board[2][i].setPiece(null);
      board[3][i].setPiece(null);
      board[4][i].setPiece(null);
      board[5][i].setPiece(null);
      board[6][i].setPiece(new Pawn(false));
    }
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
    int hIndex = CommonUtil.getHIndex(h);
    int vIndex = CommonUtil.getVIndex(v);

    try {
      return Optional.of(board[hIndex][vIndex]);
    } catch (IndexOutOfBoundsException e) {
      return Optional.empty();
    }
  }

  public boolean isPathClear(Space currentSpace, Space proposedSpace) {
    return getSpacesBetween(currentSpace, proposedSpace)
      .stream()
      .noneMatch(Space::hasPiece);
  }

  public List<Space> getAllSpaces() {
    return allSpacesFlattened;
  }

  public List<Space> getActiveSpaces() {
    return getAllSpaces().stream()
      .filter(Space::hasPiece)
      .collect(toList());
  }

  public List<Space> getActiveSpaces(boolean isWhite) {
    return getActiveSpaces().stream()
      .filter(space -> space.getPiece().isWhite() == isWhite)
      .collect(toList());
  }

  public List<Space> getSpacesContainingPiece(Class<?> pieceType, boolean isWhite) {
    return getActiveSpaces(isWhite).stream()
      .filter(space -> pieceType.isInstance(space.getPiece()))
      .collect(toList());
  }

  protected List<Space> getSpacesBetween(Space currentSpace, Space proposedSpace) {
    int currentHIndex = CommonUtil.getHIndex(currentSpace);
    int currentVIndex = CommonUtil.getVIndex(currentSpace);
    int hDiff = CommonUtil.getHDiff(currentSpace, proposedSpace);
    int vDiff = CommonUtil.getVDiff(currentSpace, proposedSpace);
    boolean isSameSpace = hDiff == 0 && vDiff == 0;
    boolean isAdjacentSpace = hDiff == 1 || hDiff == -1 || vDiff == 1 || vDiff == -1;
    boolean isDiagonalSpace = abs(hDiff) == abs(vDiff);
    boolean isHorizontalSpace = hDiff == 0;

    if (isSameSpace || isAdjacentSpace) {
      return emptyList();
    }

    if (isDiagonalSpace) {
      return getSpacesBetweenDiagonal(currentHIndex, currentVIndex, hDiff, vDiff);
    }

    if (isHorizontalSpace) {
      return getSpacesBetweenHorizontal(currentHIndex, currentVIndex, vDiff);
    }

    return getSpacesBetweenVertical(currentHIndex, currentVIndex, hDiff);
  }

  protected List<Space> getSpacesBetweenDiagonal(int currentHIndex, int currentVIndex, int hDiff, int vDiff) {
    List<Space> spacesBetween = new ArrayList<>();
    int numOfSpacesBetween = abs(hDiff) - 1;

    for (int i = 1; i <= numOfSpacesBetween; i++) {
      int hIndex = currentHIndex + (hDiff < 0 ? -1 * i : i);
      int vIndex = currentVIndex + (vDiff < 0 ? -1 * i : i);
      spacesBetween.add(board[hIndex][vIndex]);
    }

    return spacesBetween;
  }

  protected List<Space> getSpacesBetweenHorizontal(int currentHIndex, int currentVIndex, int vDiff) {
    List<Space> spacesBetween = new ArrayList<>();
    int numOfSpacesBetween = abs(vDiff) - 1;

    for (int i = 1; i <= numOfSpacesBetween; i++) {
      int vIndex = currentVIndex + (vDiff < 0 ? -1 * i : i);
      spacesBetween.add(board[currentHIndex][vIndex]);
    }

    return spacesBetween;
  }

  protected List<Space> getSpacesBetweenVertical(int currentHIndex, int currentVIndex, int hDiff) {
    List<Space> spacesBetween = new ArrayList<>();
    int numOfSpacesBetween = abs(hDiff) - 1;

    for (int i = 1; i <= numOfSpacesBetween; i++) {
      int hIndex = currentHIndex + (hDiff < 0 ? -1 * i : i);
      spacesBetween.add(board[hIndex][currentVIndex]);
    }

    return spacesBetween;
  }
}