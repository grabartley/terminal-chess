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

public class KnightTest {
  private Knight knight;

  @Before
  public void beforeEach() {
    knight = new Knight(true);
  }

  @Test
  public void testIsValidMove() {
    Map<Integer, List<Integer>> validMap = new HashMap<>();
    validMap.put(-2, asList(-1, 1));
    validMap.put(-1, asList(-2, 2));
    validMap.put(1, asList(-2, 2));
    validMap.put(2, asList(-1, 1));

    for (int hDiff = -7; hDiff < 8; hDiff++) {
      List<Integer> validVDiffs = validMap.get(hDiff);
      for (int vDiff = -7; vDiff < 8; vDiff++) {
        if (nonNull(validVDiffs) && validVDiffs.contains(vDiff)) {
          assertTrue(knight.isValidMove(hDiff, vDiff, false));
          assertTrue(knight.isValidMove(hDiff, vDiff, true));
        } else {
          assertFalse(knight.isValidMove(hDiff, vDiff, false));
          assertFalse(knight.isValidMove(hDiff, vDiff, true));
        }
      }
    }
  }
}