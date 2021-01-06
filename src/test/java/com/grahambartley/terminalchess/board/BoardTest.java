package com.grahambartley.terminalchess.board;

import com.grahambartley.terminalchess.pieces.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

public class BoardTest {
  @Spy
  private Board board;

  @Before
  public void beforeEach() {
    initMocks(this);
  }

  @Test
  public void testSetupPieces() {
    board.setUpPieces();

    assertTrue(board.board[0][0].getPiece() instanceof Rook);
    assertTrue(board.board[0][0].getPiece().isWhite());
    assertTrue(board.board[0][1].getPiece() instanceof Knight);
    assertTrue(board.board[0][1].getPiece().isWhite());
    assertTrue(board.board[0][2].getPiece() instanceof Bishop);
    assertTrue(board.board[0][2].getPiece().isWhite());
    assertTrue(board.board[0][3].getPiece() instanceof Queen);
    assertTrue(board.board[0][3].getPiece().isWhite());
    assertTrue(board.board[0][4].getPiece() instanceof King);
    assertTrue(board.board[0][4].getPiece().isWhite());
    assertTrue(board.board[0][5].getPiece() instanceof Bishop);
    assertTrue(board.board[0][5].getPiece().isWhite());
    assertTrue(board.board[0][6].getPiece() instanceof Knight);
    assertTrue(board.board[0][6].getPiece().isWhite());
    assertTrue(board.board[0][7].getPiece() instanceof Rook);
    assertTrue(board.board[0][7].getPiece().isWhite());
    for (int i = 0; i < 8; i++) {
      assertTrue(board.board[1][i].getPiece() instanceof Pawn);
      assertTrue(board.board[1][i].getPiece().isWhite());
      assertNull(board.board[2][i].getPiece());
      assertNull(board.board[3][i].getPiece());
      assertNull(board.board[4][i].getPiece());
      assertNull(board.board[5][i].getPiece());
      assertTrue(board.board[6][i].getPiece() instanceof Pawn);
      assertFalse(board.board[6][i].getPiece().isWhite());
    }
    assertTrue(board.board[7][0].getPiece() instanceof Rook);
    assertFalse(board.board[7][0].getPiece().isWhite());
    assertTrue(board.board[7][1].getPiece() instanceof Knight);
    assertFalse(board.board[7][1].getPiece().isWhite());
    assertTrue(board.board[7][2].getPiece() instanceof Bishop);
    assertFalse(board.board[7][2].getPiece().isWhite());
    assertTrue(board.board[7][3].getPiece() instanceof King);
    assertFalse(board.board[7][3].getPiece().isWhite());
    assertTrue(board.board[7][4].getPiece() instanceof Queen);
    assertFalse(board.board[7][4].getPiece().isWhite());
    assertTrue(board.board[7][5].getPiece() instanceof Bishop);
    assertFalse(board.board[7][5].getPiece().isWhite());
    assertTrue(board.board[7][6].getPiece() instanceof Knight);
    assertFalse(board.board[7][6].getPiece().isWhite());
    assertTrue(board.board[7][7].getPiece() instanceof Rook);
    assertFalse(board.board[7][7].getPiece().isWhite());
  }

  @Test
  public void testGetSpace_spaceFound() {
    String h = "2";
    String v = "C";
    Space expected = new Space(h, v, null);
    board.board[1][2] = expected;

    Optional<Space> actualOptional = board.getSpace(h, v);

    assertTrue(actualOptional.isPresent());
    assertEquals(expected, actualOptional.get());
  }

  @Test
  public void testGetSpace_spaceNotFound() {
    String h = "-1";
    String v = "Z";

    Optional<Space> actualOptional = board.getSpace(h, v);

    assertFalse(actualOptional.isPresent());
  }

  @Test
  public void testIsPathClear_clear() {
    Space currentSpace = new Space("2", "C", new Rook(true));
    Space proposedSpace = new Space("5", "C", new Pawn(false));
    doReturn(asList(
      new Space("3", "C", null),
      new Space("4", "C", null)
    )).when(board).getSpacesBetween(eq(currentSpace), eq(proposedSpace));

    assertTrue(board.isPathClear(currentSpace, proposedSpace));
  }

  @Test
  public void testIsPathClear_notClear() {
    Space currentSpace = new Space("2", "C", new Rook(true));
    Space proposedSpace = new Space("5", "C", new Pawn(false));
    doReturn(asList(
      new Space("3", "C", null),
      new Space("4", "C", new Bishop(false))
    )).when(board).getSpacesBetween(eq(currentSpace), eq(proposedSpace));

    assertFalse(board.isPathClear(currentSpace, proposedSpace));
  }

  @Test
  public void testGetActiveSpaces() {
    Space space1 = new Space("1", "A", null);
    Space space2 = new Space("2", "A", new Pawn(false));
    Space space3 = new Space("3", "A", null);
    Space space4 = new Space("4", "A", new Knight(false));
    Space space5 = new Space("5", "A", null);
    doReturn(asList(space1, space2, space3, space4, space5)).when(board).getAllSpaces();
    List<Space> expected = asList(space2, space4);

    List<Space> actual = board.getActiveSpaces();

    assertEquals(expected, actual);
  }

  @Test
  public void testGetActiveSpaces_team() {
    Space space1 = new Space("1", "A", new Pawn(false));
    Space space2 = new Space("2", "A", new Queen(true));
    Space space3 = new Space("3", "A", new Knight(false));
    doReturn(asList(space1, space2, space3)).when(board).getActiveSpaces();
    List<Space> expected = asList(space1, space3);

    List<Space> actual = board.getActiveSpaces(false);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSpacesContainingPiece() {
    Space space1 = new Space("1", "A", new Pawn(false));
    Space space2 = new Space("2", "A", new Knight(false));
    Space space3 = new Space("3", "A", new Knight(false));
    doReturn(asList(space1, space2, space3)).when(board).getActiveSpaces(eq(false));
    List<Space> expected = asList(space2, space3);

    List<Space> actual = board.getSpacesContainingPiece(Knight.class, false);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSpacesBetween_sameSpace() {
    Space currentSpace = new Space("2", "C", null);
    Space proposedSpace = new Space("2", "C", null);
    List<Space> expected = emptyList();

    List<Space> actual = board.getSpacesBetween(currentSpace, proposedSpace);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSpacesBetween_adjacentSpace() {
    Space currentSpace = new Space("2", "C", null);
    Space proposedSpace = new Space("2", "D", null);
    List<Space> expected = emptyList();

    List<Space> actual = board.getSpacesBetween(currentSpace, proposedSpace);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSpacesBetween_diagonalSpace() {
    Space currentSpace = new Space("2", "C", null);
    Space proposedSpace = new Space("5", "F", null);
    Space space1 = new Space("3", "D", null);
    Space space2 = new Space("4", "E", null);
    List<Space> expected = asList(space1, space2);
    doReturn(expected).when(board).getSpacesBetweenDiagonal(eq(1), eq(2), eq(3), eq(3));

    List<Space> actual = board.getSpacesBetween(currentSpace, proposedSpace);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSpacesBetween_horizontalSpace() {
    Space currentSpace = new Space("2", "C", null);
    Space proposedSpace = new Space("2", "F", null);
    Space space1 = new Space("2", "D", null);
    Space space2 = new Space("2", "E", null);
    List<Space> expected = asList(space1, space2);
    doReturn(expected).when(board).getSpacesBetweenHorizontal(eq(1), eq(2), eq(3));

    List<Space> actual = board.getSpacesBetween(currentSpace, proposedSpace);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSpacesBetween_verticalSpace() {
    Space currentSpace = new Space("2", "C", null);
    Space proposedSpace = new Space("5", "C", null);
    Space space1 = new Space("3", "C", null);
    Space space2 = new Space("4", "C", null);
    List<Space> expected = asList(space1, space2);
    doReturn(expected).when(board).getSpacesBetweenVertical(eq(1), eq(2), eq(3));

    List<Space> actual = board.getSpacesBetween(currentSpace, proposedSpace);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSpacesBetweenDiagonal() {
    int currentHIndex = 1;
    int currentVIndex = 2;
    int hDiff = 3;
    int vDiff = 3;
    Space space1 = new Space("3", "D", null);
    Space space2 = new Space("4", "E", null);
    board.board[2][3] = space1;
    board.board[3][4] = space2;
    List<Space> expected = asList(space1, space2);

    List<Space> actual = board.getSpacesBetweenDiagonal(currentHIndex, currentVIndex, hDiff, vDiff);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSpacesBetweenHorizontal() {
    int currentHIndex = 1;
    int currentVIndex = 2;
    int vDiff = 3;
    Space space1 = new Space("2", "D", null);
    Space space2 = new Space("2", "E", null);
    board.board[1][3] = space1;
    board.board[1][4] = space2;
    List<Space> expected = asList(space1, space2);

    List<Space> actual = board.getSpacesBetweenHorizontal(currentHIndex, currentVIndex, vDiff);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetSpacesBetweenVertical() {
    int currentHIndex = 1;
    int currentVIndex = 2;
    int hDiff = 3;
    Space space1 = new Space("3", "C", null);
    Space space2 = new Space("4", "C", null);
    board.board[2][2] = space1;
    board.board[3][2] = space2;
    List<Space> expected = asList(space1, space2);

    List<Space> actual = board.getSpacesBetweenVertical(currentHIndex, currentVIndex, hDiff);

    assertEquals(expected, actual);
  }
}
