package com.grahambartley.terminalchess.pieces;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueenTest {
  private Queen queen;

  @Before
  public void beforeEach() {
    queen = new Queen(true);
  }

  @Test
  public void testIsValidMove() {
    Map<Integer, List<Integer>> validMap = new HashMap<>();
    validMap.put(-7, asList(-7, 0, 7));
    validMap.put(-6, asList(-6, 0, 6));
    validMap.put(-5, asList(-5, 0, 5));
    validMap.put(-4, asList(-4, 0, 4));
    validMap.put(-3, asList(-3, 0, 3));
    validMap.put(-2, asList(-2, 0, 2));
    validMap.put(-1, asList(-1, 0, 1));
    validMap.put(0, asList(-7, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 7));
    validMap.put(1, asList(-1, 0, 1));
    validMap.put(2, asList(-2, 0, 2));
    validMap.put(3, asList(-3, 0, 3));
    validMap.put(4, asList(-4, 0, 4));
    validMap.put(5, asList(-5, 0, 5));
    validMap.put(6, asList(-6, 0, 6));
    validMap.put(7, asList(-7, 0, 7));

    for (int hDiff = -7; hDiff < 8; hDiff++) {
      List<Integer> validVDiffs = validMap.get(hDiff);
      for (int vDiff = -7; vDiff < 8; vDiff++) {
        if (nonNull(validVDiffs) && validVDiffs.contains(vDiff)) {
          assertTrue(queen.isValidMove(hDiff, vDiff, false));
          assertTrue(queen.isValidMove(hDiff, vDiff, true));
        } else {
          assertFalse(queen.isValidMove(hDiff, vDiff, false));
          assertFalse(queen.isValidMove(hDiff, vDiff, true));
        }
      }
    }
  }
}