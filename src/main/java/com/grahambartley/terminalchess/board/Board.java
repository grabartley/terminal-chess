package com.grahambartley.terminalchess.board;

import static java.util.Objects.isNull;

import java.util.ArrayList;

import static java.util.Collections.emptyList;
import static java.lang.Math.abs;

import java.util.List;
import java.util.Optional;

import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;
import com.grahambartley.terminalchess.pieces.Pawn;

public class Board {
  private static Board instance;
  private Space[][] board;

  private Board() {
    this.board = new Space[8][8];

    for (int i = 0; i < 8; i++) {
      String h = HorizontalSpaceIndex.getNameByIndex(i);
      for (int j = 0; j < 8; j++) {
        String v = VerticalSpaceIndex.getNameByIndex(j);
        if (i == 1) {
          board[i][j] = new Space(h, v, new Pawn(true));
        } else if (i == 6) {
          board[i][j] = new Space(h, v, new Pawn(false));
        } else {
          board[i][j] = new Space(h, v, null);
        }
      }
    }
  }

  public static Board getInstance() {
    if (isNull(instance)) {
      instance = new Board();
    }

    return instance;
  }

  public Optional<Space> getSpace(char h, char v) {
    return getSpace(String.valueOf(h), String.valueOf(v));
  }

  public Optional<Space> getSpace(String h, String v) {
    int hIndex = HorizontalSpaceIndex.getIndexByName(h);
    int vIndex = VerticalSpaceIndex.getIndexByName(v);

    try {
      return Optional.of(board[hIndex][vIndex]);
    } catch (IndexOutOfBoundsException e) {
      return Optional.empty();
    }
  }

  public boolean isPathClear(Space currentSpace, Space proposedSpace) {
    getSpacesBetween(currentSpace, proposedSpace).forEach(space -> System.out.println(space.getV() + space.getH()));
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