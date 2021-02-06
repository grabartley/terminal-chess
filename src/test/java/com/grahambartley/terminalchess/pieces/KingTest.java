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

public class KingTest {
  private King king;

  @Before
  public void beforeEach() {
    king = new King(true);
  }

  @Test
  public void testIsValidMove() {
    Map<Integer, List<Integer>> validMap = new HashMap<>();
    validMap.put(-1, asList(-1, 0, 1));
    validMap.put(0, asList(-1, 1));
    validMap.put(1, asList(-1, 0, 1));

    for (int hDiff = -7; hDiff < 8; hDiff++) {
      List<Integer> validVDiffs = validMap.get(hDiff);
      for (int vDiff = -7; vDiff < 8; vDiff++) {
        if (nonNull(validVDiffs) && validVDiffs.contains(vDiff)) {
          assertTrue(king.isValidMove(hDiff, vDiff, false));
          assertTrue(king.isValidMove(hDiff, vDiff, true));
        } else {
          assertFalse(king.isValidMove(hDiff, vDiff, false));
          assertFalse(king.isValidMove(hDiff, vDiff, true));
        }
      }
    }
  }
}