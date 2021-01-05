package com.grahambartley.terminalchess.pieces;

import com.grahambartley.terminalchess.board.Space;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PieceTest {
  @Test
  public void testSimulateMove_capturing() {
    Piece pieceToMove = new Pawn(true);
    Piece pieceToCapture = new Rook(false);
    Space currentSpace = new Space("2", "C", pieceToMove);
    Space proposedSpace = new Space("3", "D", pieceToCapture);
    Optional<Piece> expected = Optional.of(pieceToCapture);

    Optional<Piece> actual = pieceToMove.simulateMove(currentSpace, proposedSpace);

    assertEquals(expected, actual);
    assertNull(currentSpace.getPiece());
    assertEquals(pieceToMove, proposedSpace.getPiece());
  }

  @Test
  public void testSimulateMove_notCapturing() {
    Piece pieceToMove = new Pawn(true);
    Space currentSpace = new Space("2", "C", pieceToMove);
    Space proposedSpace = new Space("3", "D", null);
    Optional<Piece> expected = Optional.empty();

    Optional<Piece> actual = pieceToMove.simulateMove(currentSpace, proposedSpace);

    assertEquals(expected, actual);
    assertNull(currentSpace.getPiece());
    assertEquals(pieceToMove, proposedSpace.getPiece());
  }
}