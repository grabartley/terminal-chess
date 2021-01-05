package com.grahambartley.terminalchess.board;

import com.grahambartley.terminalchess.pieces.Pawn;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SpaceTest {
  @Test
  public void testHasPiece() {
    Space space = new Space("2", "C", null);

    assertFalse(space.hasPiece());

    space.setPiece(new Pawn(true));

    assertTrue(space.hasPiece());
  }
}