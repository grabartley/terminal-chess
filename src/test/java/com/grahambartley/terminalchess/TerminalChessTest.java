package com.grahambartley.terminalchess;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.Messages;
import com.grahambartley.terminalchess.constants.State;
import com.grahambartley.terminalchess.pieces.Bishop;
import com.grahambartley.terminalchess.pieces.Rook;
import com.grahambartley.terminalchess.utils.TerminalUtil;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.*;

import static java.util.Arrays.asList;
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