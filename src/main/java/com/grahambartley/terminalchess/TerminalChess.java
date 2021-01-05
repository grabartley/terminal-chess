package com.grahambartley.terminalchess;

import static com.grahambartley.terminalchess.utils.TerminalUtil.displayMenu;
import static com.grahambartley.terminalchess.utils.TerminalUtil.promptPlayer;
import static com.grahambartley.terminalchess.utils.TerminalUtil.displayBoard;
import static com.grahambartley.terminalchess.utils.TerminalUtil.display;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.Messages;
import com.grahambartley.terminalchess.constants.State;
import com.grahambartley.terminalchess.pieces.Piece;

import java.util.Optional;

public class TerminalChess {
    private State state;
    private final Board board;
    private boolean isWhiteTurn;
    private final List<Piece> capturedWhitePieces;
    private final List<Piece> capturedBlackPieces;

    private TerminalChess() {
        this.state = State.MENU;
        this.board = Board.getInstance();
        this.isWhiteTurn = true;
        this.capturedWhitePieces = new ArrayList<>();
        this.capturedBlackPieces = new ArrayList<>();
    }

    public static void main(String[] args) {
        new TerminalChess().play();
    }

    private void play() {
        board.setUpPieces();
        while (state != State.EXIT) {
            if (state == State.MENU) {
                showMenu();
            } else if (state == State.PLAY) {
                takeTurn();
            }
        }
    }

    private void showMenu() {
        displayMenu();
        Optional<String> chosenStateOption = Optional.empty();
        while (!chosenStateOption.isPresent()) {
            chosenStateOption = promptPlayer();
        }
        State chosenState = State.getByName(chosenStateOption.get());
        if (nonNull(chosenState)) {
            state = chosenState;
        }
    }

    private void takeTurn() {
        display((isWhiteTurn ? "White" : "Black") + " turn");
        displayBoard(board, capturedWhitePieces, capturedBlackPieces);
        List<Space> proposedMove = getProposedMove();
        if (isNull(proposedMove)) {
            return;
        }
        Space currentSpace = proposedMove.get(0);
        Space proposedSpace = proposedMove.get(1);
        boolean selectedPieceExistsAndIsYours = 
            currentSpace.hasPiece() &&
            currentSpace.getPiece().isWhite == isWhiteTurn;
        
        if (selectedPieceExistsAndIsYours) {
            Piece pieceToMove = currentSpace.getPiece();
            // simulate moving piece to calculate valid move sets afterwards
            pieceToMove.move(currentSpace, proposedSpace);
            calculateValidMoveSets();
            pieceToMove.move(proposedSpace, currentSpace);
            boolean isMoveValid = pieceToMove.isValidMoveAdvanced(currentSpace, proposedSpace, board);
            if (isMoveValid) {
                boolean isCapturing = proposedSpace.hasPiece();
                if (isCapturing) {
                    performCapture(proposedSpace);
                }
                pieceToMove.move(currentSpace, proposedSpace);
                isWhiteTurn = !isWhiteTurn;
                return;
            }
        }
        display(Messages.INVALID_MOVE_ERROR.getText());
    }

    private List<Space> getProposedMove() {
        List<Space> proposedMove = new ArrayList<>();
        Optional<String> moveOption = Optional.empty();
        while (!moveOption.isPresent()) {
            moveOption = promptPlayer();
            if (isExiting(moveOption.get())) {
                state = State.MENU;
                return null;
            } else if (moveOption.get().split(" ").length != 2) {
                moveOption = Optional.empty();
            } else {
                String[] move = moveOption.get().split(" ");
                Optional<Space> currentSpaceOptional = board.getSpace(
                    move[0].charAt(1),
                    move[0].charAt(0)
                );
                Optional<Space> proposedSpaceOptional = board.getSpace(
                    move[1].charAt(1),
                    move[1].charAt(0)
                );
                if (!currentSpaceOptional.isPresent() || !proposedSpaceOptional.isPresent()) {
                    moveOption = Optional.empty();
                    display(Messages.INVALID_MOVE_ERROR.getText());
                } else {
                    proposedMove.addAll(asList(currentSpaceOptional.get(), proposedSpaceOptional.get()));
                }
            }
        }

        return proposedMove;
    }

    private boolean isExiting(String input) {
        return input.equalsIgnoreCase("exit") ||
            input.equalsIgnoreCase("quit") ||
            input.equalsIgnoreCase("menu");
    }

    private void performCapture(Space captureSpace) {
        Piece pieceToCapture = captureSpace.getPiece();
        if (isWhiteTurn) {
            capturedBlackPieces.add(pieceToCapture);
        } else {
            capturedWhitePieces.add(pieceToCapture);
        }
        captureSpace.setPiece(null);
    }

    private void calculateValidMoveSets() {
        for (Space space : board.getActiveSpaces()) {
            Piece piece = space.getPiece();
            piece.calculateValidMoveSet(space, board);
        }
    }
}
