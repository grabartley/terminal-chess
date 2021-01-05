package com.grahambartley.terminalchess.pieces;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

public class PieceTest {
  @Spy
  private TestPiece piece;

  @Mock
  private Board board;

  @Before
  public void beforeEach() {
    initMocks(this);
  }

  @NoArgsConstructor
  private static class TestPiece extends Piece {
    public TestPiece(boolean isWhite) {
      this.isWhite = isWhite;
    }

    @Override
    public boolean isValidMove(int hDiff, int vDiff, boolean isCapturing) {
      return true;
    }
  }

  @Test
  public void testSimulateMove_capturing() {
    Piece pieceToCapture = new TestPiece(false);
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = new Space("3", "D", pieceToCapture);
    Optional<Piece> expected = Optional.of(pieceToCapture);

    Optional<Piece> actual = piece.simulateMove(currentSpace, proposedSpace);

    assertEquals(expected, actual);
    assertNull(currentSpace.getPiece());
    assertEquals(piece, proposedSpace.getPiece());
    assertFalse(piece.isHasMoved());
  }

  @Test
  public void testSimulateMove_notCapturing() {
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = new Space("3", "D", null);
    Optional<Piece> expected = Optional.empty();

    Optional<Piece> actual = piece.simulateMove(currentSpace, proposedSpace);

    assertEquals(expected, actual);
    assertNull(currentSpace.getPiece());
    assertEquals(piece, proposedSpace.getPiece());
    assertFalse(piece.isHasMoved());
  }

  @Test
  public void testMove_capturing() {
    Piece pieceToCapture = new TestPiece(false);
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = new Space("3", "D", pieceToCapture);
    Optional<Piece> expected = Optional.of(pieceToCapture);
    doReturn(expected).when(piece).simulateMove(eq(currentSpace), eq(proposedSpace));

    Optional<Piece> actual = piece.move(currentSpace, proposedSpace);

    assertEquals(expected, actual);
    assertTrue(piece.isHasMoved());
  }

  @Test
  public void testMove_notCapturing() {
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = new Space("3", "D", null);
    Optional<Piece> expected = Optional.empty();
    doReturn(expected).when(piece).simulateMove(eq(currentSpace), eq(proposedSpace));

    Optional<Piece> actual = piece.move(currentSpace, proposedSpace);

    assertEquals(expected, actual);
    assertTrue(piece.isHasMoved());
  }

  @Test
  public void testIsValidMove_valid() {
    piece.setWhite(true);
    piece.setAbleToJump(false);
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = new Space("4", "C", null);
    doReturn(true).when(board).isPathClear(eq(currentSpace), eq(proposedSpace));

    assertTrue(piece.isValidMove(currentSpace, proposedSpace, board));
  }

  @Test
  public void testIsValidMove_validWithJump() {
    piece.setWhite(true);
    piece.setAbleToJump(true);
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = new Space("4", "C", null);
    doReturn(false).when(board).isPathClear(eq(currentSpace), eq(proposedSpace));

    assertTrue(piece.isValidMove(currentSpace, proposedSpace, board));
  }

  @Test
  public void testIsValidMove_invalidMovingOntoOwnPiece() {
    piece.setWhite(true);
    piece.setAbleToJump(false);
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = new Space("4", "C", new TestPiece(true));
    doReturn(true).when(board).isPathClear(eq(currentSpace), eq(proposedSpace));

    assertFalse(piece.isValidMove(currentSpace, proposedSpace, board));
  }

  @Test
  public void testIsValidMove_invalidPathNotClearAndCannotJump() {
    piece.setWhite(true);
    piece.setAbleToJump(false);
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = new Space("4", "C", null);
    doReturn(false).when(board).isPathClear(eq(currentSpace), eq(proposedSpace));

    assertFalse(piece.isValidMove(currentSpace, proposedSpace, board));
  }

  @Test
  public void testCalculateValidMoveSet() {
    Space currentSpace = new Space("2", "C", piece);
    Space space1 = new Space("", "", null);
    Space space2 = new Space("", "", null);
    Space space3 = new Space("", "", null);
    Space space4 = new Space("", "", null);
    List<Space> allSpaces = asList(currentSpace, space1, space2, space3, space4);
    doReturn(allSpaces).when(board).getAllSpaces();
    doReturn(false).when(piece).isValidMove(eq(currentSpace), eq(currentSpace), eq(board));
    doReturn(false).when(piece).isValidMove(eq(currentSpace), eq(space1), eq(board));
    doReturn(true).when(piece).isValidMove(eq(currentSpace), eq(space2), eq(board));
    doReturn(false).when(piece).isValidMove(eq(currentSpace), eq(space3), eq(board));
    doReturn(true).when(piece).isValidMove(eq(currentSpace), eq(space4), eq(board));
    List<Space> expected = asList(space2, space4);

    piece.calculateValidMoveSet(currentSpace, board);

    assertEquals(expected, piece.getValidMoveSet());
  }
}