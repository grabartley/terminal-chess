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

public class BishopTest {
  private Bishop bishop;

  @Before
  public void beforeEach() {
    bishop = new Bishop(true);
  }

  @Test
  public void testIsValidMove() {
    Map<Integer, List<Integer>> validMap = new HashMap<>();
    validMap.put(-7, asList(-7, 7));
    validMap.put(-6, asList(-6, 6));
    validMap.put(-5, asList(-5, 5));
    validMap.put(-4, asList(-4, 4));
    validMap.put(-3, asList(-3, 3));
    validMap.put(-2, asList(-2, 2));
    validMap.put(-1, asList(-1, 1));
    validMap.put(1, asList(-1, 1));
    validMap.put(2, asList(-2, 2));
    validMap.put(3, asList(-3, 3));
    validMap.put(4, asList(-4, 4));
    validMap.put(5, asList(-5, 5));
    validMap.put(6, asList(-6, 6));
    validMap.put(7, asList(-7, 7));

    for (int hDiff = -7; hDiff < 8; hDiff++) {
      List<Integer> validVDiffs = validMap.get(hDiff);
      for (int vDiff = -7; vDiff < 8; vDiff++) {
        if (nonNull(validVDiffs) && validVDiffs.contains(vDiff)) {
          assertTrue(bishop.isValidMove(hDiff, vDiff, false));
          assertTrue(bishop.isValidMove(hDiff, vDiff, true));
        } else {
          assertFalse(bishop.isValidMove(hDiff, vDiff, false));
          assertFalse(bishop.isValidMove(hDiff, vDiff, true));
        }
      }
    }
  }
}