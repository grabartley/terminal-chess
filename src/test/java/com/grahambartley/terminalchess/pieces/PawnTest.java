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

public class PawnTest {
  private Pawn pawn;

  @Before
  public void beforeEach() {
    pawn = new Pawn(true);
  }

  @Test
  public void testIsValidMove() {
    Map<Integer, List<Integer>> validMapNotMovedNotCapturing = new HashMap<>();
    validMapNotMovedNotCapturing.put(1, singletonList(0));
    validMapNotMovedNotCapturing.put(2, singletonList(0));
    Map<Integer, List<Integer>> validMapMovedNotCapturing = new HashMap<>();
    validMapMovedNotCapturing.put(1, singletonList(0));
    Map<Integer, List<Integer>> validMapCapturing = new HashMap<>();
    validMapCapturing.put(1, asList(-1, 1));

    pawn.setHasMoved(false);
    for (int hDiff = -7; hDiff < 8; hDiff++) {
      List<Integer> validVDiffs = validMapNotMovedNotCapturing.get(hDiff);
      for (int vDiff = -7; vDiff < 8; vDiff++) {
        if (nonNull(validVDiffs) && validVDiffs.contains(vDiff)) {
          assertTrue(pawn.isValidMove(hDiff, vDiff, false));
        } else {
          assertFalse(pawn.isValidMove(hDiff, vDiff, false));
        }
      }
    }

    pawn.setHasMoved(true);
    for (int hDiff = -7; hDiff < 8; hDiff++) {
      List<Integer> validVDiffs = validMapMovedNotCapturing.get(hDiff);
      for (int vDiff = -7; vDiff < 8; vDiff++) {
        if (nonNull(validVDiffs) && validVDiffs.contains(vDiff)) {
          assertTrue(pawn.isValidMove(hDiff, vDiff, false));
        } else {
          assertFalse(pawn.isValidMove(hDiff, vDiff, false));
        }
      }
    }

    for (int hDiff = -7; hDiff < 8; hDiff++) {
      List<Integer> validVDiffs = validMapCapturing.get(hDiff);
      for (int vDiff = -7; vDiff < 8; vDiff++) {
        if (nonNull(validVDiffs) && validVDiffs.contains(vDiff)) {
          pawn.setHasMoved(false);
          assertTrue(pawn.isValidMove(hDiff, vDiff, true));
          pawn.setHasMoved(true);
          assertTrue(pawn.isValidMove(hDiff, vDiff, true));
        } else {
          pawn.setHasMoved(false);
          assertFalse(pawn.isValidMove(hDiff, vDiff, true));
          pawn.setHasMoved(true);
          assertFalse(pawn.isValidMove(hDiff, vDiff, true));
        }
      }
    }
  }
}