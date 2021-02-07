package com.grahambartley.terminalchess;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.Messages;
import com.grahambartley.terminalchess.constants.State;
import com.grahambartley.terminalchess.pieces.*;
import com.grahambartley.terminalchess.utils.TerminalUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.*;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TerminalChessTest {
  @Spy
  @InjectMocks
  private TerminalChess terminalChess;

  @Mock
  private Board board;

  private static InputStream STD_IN;
  private static PrintStream STD_OUT;

  @BeforeClass
  public static void before() {
    STD_IN = System.in;
    STD_OUT = System.out;
  }

  @Before
  public void beforeEach() {
    initMocks(this);
  }

  @AfterClass
  public static void after() {
    System.setIn(STD_IN);
    System.setOut(STD_OUT);
  }

  @Test
  public void testPlay_menu() {
    setUpScannerFakeInput("e\n");
    terminalChess.setState(State.MENU);
    terminalChess.play();
    verify(terminalChess).showMenu();
    verify(terminalChess, never()).newGame();
    verify(terminalChess, never()).takeTurn();
    verify(terminalChess, never()).showCheckmate();
  }

  @Test
  public void testPlay_newGame() throws InterruptedException {
    setUpScannerFakeInput("quit\ne\n");
    terminalChess.setState(State.NEW_GAME);
    terminalChess.play();
    Thread.sleep(500L);
    verify(terminalChess, never()).showMenu();
    verify(terminalChess).newGame();
    verify(terminalChess, never()).takeTurn();
    verify(terminalChess, never()).showCheckmate();
  }

  @Test
  public void testPlay_play() {
    setUpScannerFakeInput("quit\ne\n");
    terminalChess.setState(State.PLAY);
    terminalChess.play();
    verify(terminalChess).showMenu();
    verify(terminalChess, never()).newGame();
    verify(terminalChess).takeTurn();
    verify(terminalChess, never()).showCheckmate();
  }

  @Test
  public void testPlay_checkmate() {
    setUpScannerFakeInput("m\ne\n");
    terminalChess.setState(State.CHECKMATE);
    terminalChess.play();
    verify(terminalChess).showMenu();
    verify(terminalChess, never()).newGame();
    verify(terminalChess, never()).takeTurn();
    verify(terminalChess).showCheckmate();
  }

  @Test
  public void testTakeTurn_movingNoPiece() throws IOException {
    ByteArrayOutputStream outputStream = setUpOutputStreamCapture();
    terminalChess.setWhiteTurn(false);
    Space currentSpace = new Space("2", "C", null);
    Space proposedSpace = new Space("3", "C", null);
    doReturn(asList(currentSpace, proposedSpace)).when(terminalChess).getProposedMove();

    terminalChess.takeTurn();

    verify(terminalChess, never()).performCapture(any());
    assertFalse(terminalChess.isWhiteTurn());
    assertTrue(outputStream.toString().contains(Messages.INVALID_MOVE_ERROR.getText()));
    outputStream.close();
  }

  @Test
  public void testTakeTurn_movingEnemyPiece() throws IOException {
    ByteArrayOutputStream outputStream = setUpOutputStreamCapture();
    terminalChess.setWhiteTurn(false);
    Rook pieceToMove = spy(new Rook(true));
    Space currentSpace = new Space("2", "C", pieceToMove);
    Space proposedSpace = new Space("3", "C", null);
    doReturn(asList(currentSpace, proposedSpace)).when(terminalChess).getProposedMove();

    terminalChess.takeTurn();

    verify(pieceToMove, never()).move(eq(currentSpace), eq(proposedSpace));
    verify(terminalChess, never()).performCapture(any());
    assertFalse(terminalChess.isWhiteTurn());
    assertTrue(outputStream.toString().contains(Messages.INVALID_MOVE_ERROR.getText()));
    outputStream.close();
  }

  @Test
  public void testTakeTurn_movingIntoCheck() throws IOException {
    ByteArrayOutputStream outputStream = setUpOutputStreamCapture();
    terminalChess.setWhiteTurn(false);
    Rook pieceToMove = spy(new Rook(false));
    Space currentSpace = new Space("7", "C", pieceToMove);
    Space proposedSpace = new Space("6", "C", null);
    doReturn(asList(currentSpace, proposedSpace)).when(terminalChess).getProposedMove();
    doReturn(true).when(terminalChess).isMovingIntoCheck(eq(pieceToMove), eq(currentSpace), eq(proposedSpace));

    terminalChess.takeTurn();

    verify(pieceToMove, never()).move(eq(currentSpace), eq(proposedSpace));
    verify(terminalChess, never()).performCapture(any());
    assertFalse(terminalChess.isWhiteTurn());
    assertTrue(outputStream.toString().contains(Messages.INVALID_MOVE_ERROR.getText()));
    outputStream.close();
  }

  @Test
  public void testTakeTurn_validMove() throws IOException {
    ByteArrayOutputStream outputStream = setUpOutputStreamCapture();
    terminalChess.setWhiteTurn(false);
    Rook pieceToMove = spy(new Rook(false));
    Space currentSpace = new Space("7", "C", pieceToMove);
    Space proposedSpace = new Space("6", "C", null);
    doReturn(asList(currentSpace, proposedSpace)).when(terminalChess).getProposedMove();
    doReturn(false).when(terminalChess).isMovingIntoCheck(eq(pieceToMove), eq(currentSpace), eq(proposedSpace));
    doReturn(true).when(pieceToMove).isValidMove(eq(currentSpace), eq(proposedSpace), eq(board));
    doReturn(false).when(terminalChess).isInCheckmate(eq(true));
    doReturn(false).when(terminalChess).isInCheck(eq(true));

    terminalChess.takeTurn();

    verify(pieceToMove).move(eq(currentSpace), eq(proposedSpace));
    verify(terminalChess, never()).performCapture(any());
    assertTrue(terminalChess.isWhiteTurn());
    assertFalse(outputStream.toString().contains(Messages.INVALID_MOVE_ERROR.getText()));
    outputStream.close();
  }

  @Test
  public void testTakeTurn_validMoveWithCapture() throws IOException {
    ByteArrayOutputStream outputStream = setUpOutputStreamCapture();
    terminalChess.setWhiteTurn(false);
    Rook pieceToMove = spy(new Rook(false));
    Bishop pieceToCapture = new Bishop(true);
    Space currentSpace = new Space("7", "C", pieceToMove);
    Space proposedSpace = new Space("6", "C", pieceToCapture);
    doReturn(asList(currentSpace, proposedSpace)).when(terminalChess).getProposedMove();
    doReturn(false).when(terminalChess).isMovingIntoCheck(eq(pieceToMove), eq(currentSpace), eq(proposedSpace));
    doReturn(true).when(pieceToMove).isValidMove(eq(currentSpace), eq(proposedSpace), eq(board));
    doReturn(false).when(terminalChess).isInCheckmate(eq(true));
    doReturn(false).when(terminalChess).isInCheck(eq(true));

    terminalChess.takeTurn();

    verify(pieceToMove).move(eq(currentSpace), eq(proposedSpace));
    verify(terminalChess).performCapture(eq(pieceToCapture));
    assertTrue(terminalChess.isWhiteTurn());
    assertFalse(outputStream.toString().contains(Messages.INVALID_MOVE_ERROR.getText()));
    outputStream.close();
  }

  @Test
  public void testTakeTurn_validMoveWithCheckmate() throws IOException {
    ByteArrayOutputStream outputStream = setUpOutputStreamCapture();
    terminalChess.setWhiteTurn(false);
    Rook pieceToMove = spy(new Rook(false));
    Space currentSpace = new Space("7", "C", pieceToMove);
    Space proposedSpace = new Space("6", "C", null);
    doReturn(asList(currentSpace, proposedSpace)).when(terminalChess).getProposedMove();
    doReturn(false).when(terminalChess).isMovingIntoCheck(eq(pieceToMove), eq(currentSpace), eq(proposedSpace));
    doReturn(true).when(pieceToMove).isValidMove(eq(currentSpace), eq(proposedSpace), eq(board));
    doReturn(true).when(terminalChess).isInCheckmate(eq(true));
    doReturn(false).when(terminalChess).isInCheck(eq(true));

    terminalChess.takeTurn();

    verify(pieceToMove).move(eq(currentSpace), eq(proposedSpace));
    verify(terminalChess, never()).performCapture(any());
    assertFalse(terminalChess.isWhiteTurn());
    assertFalse(outputStream.toString().contains(Messages.INVALID_MOVE_ERROR.getText()));
    assertTrue(outputStream.toString().contains(Messages.CHECKMATE.getText()));
    assertEquals(State.CHECKMATE, terminalChess.getState());
    outputStream.close();
  }

  @Test
  public void testTakeTurn_validMoveWithCheck() throws IOException {
    ByteArrayOutputStream outputStream = setUpOutputStreamCapture();
    terminalChess.setWhiteTurn(false);
    Rook pieceToMove = spy(new Rook(false));
    Space currentSpace = new Space("7", "C", pieceToMove);
    Space proposedSpace = new Space("6", "C", null);
    doReturn(asList(currentSpace, proposedSpace)).when(terminalChess).getProposedMove();
    doReturn(false).when(terminalChess).isMovingIntoCheck(eq(pieceToMove), eq(currentSpace), eq(proposedSpace));
    doReturn(true).when(pieceToMove).isValidMove(eq(currentSpace), eq(proposedSpace), eq(board));
    doReturn(false).when(terminalChess).isInCheckmate(eq(true));
    doReturn(true).when(terminalChess).isInCheck(eq(true));

    terminalChess.takeTurn();

    verify(pieceToMove).move(eq(currentSpace), eq(proposedSpace));
    verify(terminalChess, never()).performCapture(any());
    assertTrue(terminalChess.isWhiteTurn());
    assertFalse(outputStream.toString().contains(Messages.INVALID_MOVE_ERROR.getText()));
    assertTrue(outputStream.toString().contains(Messages.CHECK.getText()));
    outputStream.close();
  }

  @Test
  public void testGetProposedMove_exiting() {
    setUpScannerFakeInput("\nexit\n");
    doReturn(true).when(terminalChess).isExiting(eq("exit"));

    List<Space> actual = terminalChess.getProposedMove();

    assertNull(actual);
    assertEquals(State.MENU, terminalChess.getState());
  }

  @Test
  public void testGetProposedMove_validMove() {
    setUpScannerFakeInput("C2 E4\n");
    Space currentSpace = new Space("2", "C", new Rook(false));
    Space proposedSpace = new Space("4", "E", null);
    doReturn(false).when(terminalChess).isExiting(any());
    doReturn(Optional.of(currentSpace)).when(board).getSpace(eq('2'), eq('C'));
    doReturn(Optional.of(proposedSpace)).when(board).getSpace(eq('4'), eq('E'));

    List<Space> actual = terminalChess.getProposedMove();

    assertNotNull(actual);
    assertEquals("2", actual.get(0).getH());
    assertEquals("C", actual.get(0).getV());
    assertEquals("4", actual.get(1).getH());
    assertEquals("E", actual.get(1).getV());
  }

  @Test
  public void testGetProposedMove_validMoveOnThirdTry() {
    setUpScannerFakeInput("Z8 X1\nrandom,input\nC2 E4\n");
    Space currentSpace = new Space("2", "C", new Rook(false));
    Space proposedSpace = new Space("4", "E", null);
    doReturn(false).when(terminalChess).isExiting(any());
    doReturn(Optional.of(currentSpace)).when(board).getSpace(eq('2'), eq('C'));
    doReturn(Optional.of(proposedSpace)).when(board).getSpace(eq('4'), eq('E'));

    List<Space> actual = terminalChess.getProposedMove();

    assertNotNull(actual);
    assertEquals("2", actual.get(0).getH());
    assertEquals("C", actual.get(0).getV());
    assertEquals("4", actual.get(1).getH());
    assertEquals("E", actual.get(1).getV());
  }

  @Test
  public void testIsExiting() {
    List<String> someValidValues = asList("exit", "EXIT", "quit", "QUIT", "menu", "MENU");
    someValidValues.forEach(validValue -> assertTrue(terminalChess.isExiting(validValue)));
  }

  @Test
  public void testPerformCapture_white() {
    terminalChess.setWhiteTurn(true);
    Pawn pieceToCapture = new Pawn(false);

    terminalChess.performCapture(pieceToCapture);

    assertEquals(1, terminalChess.getCapturedBlackPieces().size());
    assertTrue(terminalChess.getCapturedBlackPieces().contains(pieceToCapture));
  }

  @Test
  public void testPerformCapture_black() {
    terminalChess.setWhiteTurn(false);
    Pawn pieceToCapture = new Pawn(true);

    terminalChess.performCapture(pieceToCapture);

    assertEquals(1, terminalChess.getCapturedWhitePieces().size());
    assertTrue(terminalChess.getCapturedWhitePieces().contains(pieceToCapture));
  }

  @Test
  public void testIsMovingIntoCheck_true() {
    Bishop piece = spy(new Bishop(true));
    Queen capturedPiece = new Queen(false);
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = spy(new Space("4", "E", capturedPiece));
    doReturn(Optional.of(capturedPiece)).when(piece).simulateMove(eq(currentSpace), eq(proposedSpace));
    doReturn(true).when(terminalChess).isInCheck(eq(true));
    doReturn(Optional.empty()).when(piece).simulateMove(eq(proposedSpace), eq(currentSpace));

    boolean actual = terminalChess.isMovingIntoCheck(piece, currentSpace, proposedSpace);

    assertTrue(actual);
    verify(piece).simulateMove(eq(currentSpace), eq(proposedSpace));
    verify(piece).simulateMove(eq(proposedSpace), eq(currentSpace));
    verify(proposedSpace).setPiece(eq(capturedPiece));
  }

  @Test
  public void testIsMovingIntoCheck_false() {
    Bishop piece = spy(new Bishop(true));
    Queen capturedPiece = new Queen(false);
    Space currentSpace = new Space("2", "C", piece);
    Space proposedSpace = spy(new Space("4", "E", capturedPiece));
    doReturn(Optional.of(capturedPiece)).when(piece).simulateMove(eq(currentSpace), eq(proposedSpace));
    doReturn(false).when(terminalChess).isInCheck(eq(true));
    doReturn(Optional.empty()).when(piece).simulateMove(eq(proposedSpace), eq(currentSpace));

    boolean actual = terminalChess.isMovingIntoCheck(piece, currentSpace, proposedSpace);

    assertFalse(actual);
    verify(piece).simulateMove(eq(currentSpace), eq(proposedSpace));
    verify(piece).simulateMove(eq(proposedSpace), eq(currentSpace));
    verify(proposedSpace).setPiece(eq(capturedPiece));
  }

  @Test
  public void testIsInCheck_true() {
    King friendlyKing = new King(false);
    Space friendlyKingSpace = new Space("2", "C", friendlyKing);
    Bishop enemyBishop = new Bishop(true);
    enemyBishop.setValidMoveSet(emptyList());
    Queen enemyQueen = new Queen(true);
    enemyQueen.setValidMoveSet(singletonList(friendlyKingSpace));
    Pawn enemyPawn = new Pawn(true);
    enemyPawn.setValidMoveSet(emptyList());
    List<Space> activeEnemySpaces = asList(
      new Space("4", "E", enemyBishop),
      new Space("2", "G", enemyQueen),
      new Space("6", "G", enemyPawn)
    );
    doNothing().when(terminalChess).calculateValidMoveSets();
    doReturn(singletonList(friendlyKingSpace)).when(board).getSpacesContainingPiece(eq(King.class), eq(false));
    doReturn(activeEnemySpaces).when(board).getActiveSpaces(eq(true));

    assertTrue(terminalChess.isInCheck(false));
  }

  @Test
  public void testIsInCheck_false() {
    King friendlyKing = new King(false);
    Space friendlyKingSpace = new Space("2", "C", friendlyKing);
    Bishop enemyBishop = new Bishop(true);
    enemyBishop.setValidMoveSet(emptyList());
    Queen enemyQueen = new Queen(true);
    enemyQueen.setValidMoveSet(emptyList());
    Pawn enemyPawn = new Pawn(true);
    enemyPawn.setValidMoveSet(emptyList());
    List<Space> activeEnemySpaces = asList(
      new Space("4", "E", enemyBishop),
      new Space("2", "G", enemyQueen),
      new Space("6", "G", enemyPawn)
    );
    doNothing().when(terminalChess).calculateValidMoveSets();
    doReturn(singletonList(friendlyKingSpace)).when(board).getSpacesContainingPiece(eq(King.class), eq(false));
    doReturn(activeEnemySpaces).when(board).getActiveSpaces(eq(true));

    assertFalse(terminalChess.isInCheck(false));
  }

  @Test
  public void testIsInCheckmate_true() {
    Rook friendlyRook = new Rook(true);
    friendlyRook.setValidMoveSet(singletonList(new Space("6", "C", null)));
    Bishop friendlyBishop = new Bishop(true);
    friendlyBishop.setValidMoveSet(singletonList(new Space("2", "A", null)));
    Knight friendlyKnight = new Knight(true);
    friendlyKnight.setValidMoveSet(singletonList(new Space("2", "G", null)));
    doReturn(true).when(terminalChess).isInCheck(true);
    doReturn(asList(
      new Space("4", "E", friendlyRook),
      new Space("2", "G", friendlyBishop),
      new Space("6", "G", friendlyKnight)
    )).when(board).getActiveSpaces(eq(true));
    doReturn(true).when(terminalChess).isMovingIntoCheck(any(), any(), any());

    assertTrue(terminalChess.isInCheckmate(true));
  }

  @Test
  public void testIsInCheckmate_falseNotInCheck() {
    doReturn(false).when(terminalChess).isInCheck(eq(true));

    assertFalse(terminalChess.isInCheckmate(true));
  }

  @Test
  public void testIsInCheckmate_falseCanEscapeCheck() {
    Rook friendlyRook = new Rook(true);
    friendlyRook.setValidMoveSet(singletonList(new Space("6", "C", null)));
    Bishop friendlyBishop = new Bishop(true);
    Space friendlyBishopCurrentSpace = new Space("2", "G", friendlyBishop);
    Space friendlyBishopValidMove = new Space("2", "A", null);
    friendlyBishop.setValidMoveSet(singletonList(friendlyBishopValidMove));
    Knight friendlyKnight = new Knight(true);
    friendlyKnight.setValidMoveSet(singletonList(new Space("2", "G", null)));
    doReturn(true).when(terminalChess).isInCheck(true);
    doReturn(asList(
      new Space("4", "E", friendlyRook),
      friendlyBishopCurrentSpace,
      new Space("6", "G", friendlyKnight)
    )).when(board).getActiveSpaces(eq(true));
    doReturn(true).when(terminalChess).isMovingIntoCheck(any(), any(), any());
    doReturn(false).when(terminalChess).isMovingIntoCheck(eq(friendlyBishop), eq(friendlyBishopCurrentSpace), eq(friendlyBishopValidMove));

    assertFalse(terminalChess.isInCheckmate(true));
  }

  @Test
  public void testCalculateValidMoveSets() {
    Rook activeRook = spy(new Rook(true));
    Queen activeQueen = spy(new Queen(false));
    King activeKing = spy(new King(false));
    Pawn activePawn = spy(new Pawn(true));
    Space activeRookSpace = new Space("2", "C", activeRook);
    Space activeQueenSpace = new Space("4", "E", activeQueen);
    Space activeKingSpace = new Space("6", "D", activeKing);
    Space activePawnSpace = new Space("6", "F", activePawn);
    doReturn(asList(
      activeRookSpace,
      activeQueenSpace,
      activeKingSpace,
      activePawnSpace
    )).when(board).getActiveSpaces();

    terminalChess.calculateValidMoveSets();

    verify(activeRook).calculateValidMoveSet(eq(activeRookSpace), eq(board));
    verify(activeQueen).calculateValidMoveSet(eq(activeQueenSpace), eq(board));
    verify(activeKing).calculateValidMoveSet(eq(activeKingSpace), eq(board));
    verify(activePawn).calculateValidMoveSet(eq(activePawnSpace), eq(board));
  }

  private void setUpScannerFakeInput(String fakeInput) {
    System.setIn(new ByteArrayInputStream(fakeInput.getBytes()));
    TerminalUtil.resetScanner();
  }

  private ByteArrayOutputStream setUpOutputStreamCapture() {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    return outputStream;
  }
}