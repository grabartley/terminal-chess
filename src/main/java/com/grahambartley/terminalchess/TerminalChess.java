package com.grahambartley.terminalchess;

import static com.grahambartley.terminalchess.utils.TerminalUtil.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.Messages;
import com.grahambartley.terminalchess.constants.State;
import com.grahambartley.terminalchess.pieces.King;
import com.grahambartley.terminalchess.pieces.Piece;

import java.util.Optional;

public class TerminalChess {
    private State state;
    private final Board board;
    private boolean isWhiteTurn;
    private final List<Piece> capturedWhitePieces;
    private final List<Piece> capturedBlackPieces;

    private TerminalChess(State state) {
        this.state = state;
        this.board = Board.getInstance();
        this.isWhiteTurn = true;
        this.capturedWhitePieces = new ArrayList<>();
        this.capturedBlackPieces = new ArrayList<>();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            State state = State.getByName(args[0]);
            if (nonNull(state)) {
                new TerminalChess(state).play();
                return;
            }
        }
        new TerminalChess(State.MENU).play();
    }

    private void play() {
        board.setUpPieces();
        while (state != State.EXIT) {
            if (state == State.MENU) {
                showMenu();
            } else if (state == State.NEW_GAME) {
                newGame();
            } else if (state == State.PLAY) {
                takeTurn();
            } else if (state == State.CHECKMATE) {
                showCheckmate();
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

    private void newGame() {
        state = State.EXIT;
        String[] newGameArgs = {"P"};
        new Thread(() -> main(newGameArgs)).start();
    }

    private void takeTurn() {
        display((isWhiteTurn ? "White" : "Black") + " turn");
        displayBoard(board, capturedWhitePieces, capturedBlackPieces, isWhiteTurn);
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
            boolean isMovingIntoCheck = isMovingIntoCheck(pieceToMove, currentSpace, proposedSpace);
            boolean isMoveValid = pieceToMove.isValidMove(currentSpace, proposedSpace, board) && !isMovingIntoCheck;
            if (isMoveValid) {
                Optional<Piece> capturedPiece = pieceToMove.move(currentSpace, proposedSpace);
                capturedPiece.ifPresent(this::performCapture);
                if (isInCheckmate(!isWhiteTurn)) {
                    display(Messages.CHECKMATE.getText());
                    state = State.CHECKMATE;
                    return;
                }
                if (isInCheck(!isWhiteTurn)) {
                    display(Messages.CHECK.getText());
                }
                isWhiteTurn = !isWhiteTurn;
                return;
            }
        }
        display(Messages.INVALID_MOVE_ERROR.getText());
    }

    private void showCheckmate() {
        displayBoard(board, capturedWhitePieces, capturedBlackPieces, isWhiteTurn);
        newLine();
        display("[N]ew game");
        display("[M]enu");
        Optional<String> chosenStateOption = Optional.empty();
        while (!chosenStateOption.isPresent()) {
            chosenStateOption = promptPlayer();
        }
        State chosenState = State.getByName(chosenStateOption.get());
        if (nonNull(chosenState)) {
            state = chosenState;
        }
    }

    private List<Space> getProposedMove() {
        List<Space> proposedMove = new ArrayList<>();
        boolean isMoveOptionValid = false;
        while (!isMoveOptionValid) {
            Optional<String> moveOption = promptPlayer();
            if (!moveOption.isPresent()) {
                continue;
            }
            String moveString = moveOption.get();
            if (isExiting(moveString)) {
                state = State.MENU;
                return null;
            }
            if (moveString.split(" ").length == 2) {
                String[] move = moveString.split(" ");
                try {
                    Optional<Space> currentSpaceOptional = board.getSpace(
                        move[0].charAt(1),
                        move[0].charAt(0)
                    );
                    Optional<Space> proposedSpaceOptional = board.getSpace(
                        move[1].charAt(1),
                        move[1].charAt(0)
                    );
                    if (currentSpaceOptional.isPresent() && proposedSpaceOptional.isPresent()) {
                        proposedMove.addAll(asList(currentSpaceOptional.get(), proposedSpaceOptional.get()));
                        isMoveOptionValid = true;
                    }
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }

        return proposedMove;
    }

    private boolean isExiting(String input) {
        return input.equalsIgnoreCase("exit") ||
            input.equalsIgnoreCase("quit") ||
            input.equalsIgnoreCase("menu");
    }

    private void performCapture(Piece piece) {
        if (isWhiteTurn) {
            capturedBlackPieces.add(piece);
        } else {
            capturedWhitePieces.add(piece);
        }
    }

    private boolean isMovingIntoCheck(Piece piece, Space currentSpace, Space proposedSpace) {
        Optional<Piece> capturedPiece = piece.simulateMove(currentSpace, proposedSpace);
        boolean isPieceMovingIntoCheck = isInCheck(piece.isWhite());
        piece.simulateMove(proposedSpace, currentSpace);
        // put the captured piece back in place
        capturedPiece.ifPresent(proposedSpace::setPiece);
        return isPieceMovingIntoCheck;
    }

    private boolean isInCheck(boolean isWhite) {
        calculateValidMoveSets();
        Space friendlyKingSpace = board.getSpacesContainingPiece(King.class, isWhite).get(0);
        List<Piece> enemyPieces = board.getActiveSpaces(!isWhite).stream()
            .map(Space::getPiece)
            .collect(toList());
        return enemyPieces.stream()
            .anyMatch(enemyPiece -> enemyPiece.getValidMoveSet().contains(friendlyKingSpace));
    }

    private boolean isInCheckmate(boolean isWhite) {
        if (!isInCheck(isWhite)) {
            return false;
        }
        List<Space> friendlySpaces = board.getActiveSpaces(isWhite);
        for (Space currentSpace : friendlySpaces) {
            Piece piece = currentSpace.getPiece();
            List<Space> validMoveSet = piece.getValidMoveSet();
            for (Space proposedSpace : validMoveSet) {
                if (!isMovingIntoCheck(piece, currentSpace, proposedSpace)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void calculateValidMoveSets() {
        for (Space space : board.getActiveSpaces()) {
            Piece piece = space.getPiece();
            piece.calculateValidMoveSet(space, board);
        }
    }
}
