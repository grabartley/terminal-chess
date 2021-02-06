package com.grahambartley.terminalchess.pieces;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RookTest {
  private Rook rook;

  @Before
  public void beforeEach() {
    rook = new Rook(true);
  }

  @Test
  public void testIsValidMove() {
    Map<Integer, List<Integer>> validMap = new HashMap<>();
    validMap.put(-7, singletonList(0));
    validMap.put(-6, singletonList(0));
    validMap.put(-5, singletonList(0));
    validMap.put(-4, singletonList(0));
    validMap.put(-3, singletonList(0));
    validMap.put(-2, singletonList(0));
    validMap.put(-1, singletonList(0));
    validMap.put(0, asList(-7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7));
    validMap.put(1, singletonList(0));
    validMap.put(2, singletonList(0));
    validMap.put(3, singletonList(0));
    validMap.put(4, singletonList(0));
    validMap.put(5, singletonList(0));
    validMap.put(6, singletonList(0));
    validMap.put(7, singletonList(0));

    for (int hDiff = -7; hDiff < 8; hDiff++) {
      List<Integer> validVDiffs = validMap.get(hDiff);
      for (int vDiff = -7; vDiff < 8; vDiff++) {
        if (nonNull(validVDiffs) && validVDiffs.contains(vDiff)) {
          assertTrue(rook.isValidMove(hDiff, vDiff, false));
          assertTrue(rook.isValidMove(hDiff, vDiff, true));
        } else {
          assertFalse(rook.isValidMove(hDiff, vDiff, false));
          assertFalse(rook.isValidMove(hDiff, vDiff, true));
        }
      }
    }
  }
}