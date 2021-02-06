package com.grahambartley.terminalchess.utils;

import com.grahambartley.terminalchess.board.Board;
import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;
import com.grahambartley.terminalchess.pieces.Piece;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class TerminalUtil {
  private static Scanner scanner = new Scanner(in);

  public static void resetScanner() {
    scanner = new Scanner(in);
  }

  public static void display(String text) {
    stream(text.split("\n")).forEach(out::println);
  }

  public static void displayExact(String text) {
    out.print(text);
  }

  public static void newLine() {
    out.println();
  }

  public static Optional<String> promptPlayer() {
    return promptPlayer("");
  }

  public static Optional<String> promptPlayer(String prompt) {
    display(prompt);
    displayExact("\n> ");
    String playerInput = scanner.nextLine();
    newLine();

    return playerInput.equals("") ? Optional.empty() : Optional.of(playerInput);
  }

  public static void displayMenu() {
    display("\n" + 
    "   ████████╗███████╗██████╗ ███╗   ███╗██╗███╗   ██╗ █████╗ ██╗          ██████╗██╗  ██╗███████╗███████╗███████╗  \n"  + 
    "   ╚══██╔══╝██╔════╝██╔══██╗████╗ ████║██║████╗  ██║██╔══██╗██║         ██╔════╝██║  ██║██╔════╝██╔════╝██╔════╝  \n"  + 
    "      ██║   █████╗  ██████╔╝██╔████╔██║██║██╔██╗ ██║███████║██║         ██║     ███████║█████╗  ███████╗███████╗  \n"  + 
    "      ██║   ██╔══╝  ██╔══██╗██║╚██╔╝██║██║██║╚██╗██║██╔══██║██║         ██║     ██╔══██║██╔══╝  ╚════██║╚════██║  \n"  + 
    "      ██║   ███████╗██║  ██║██║ ╚═╝ ██║██║██║ ╚████║██║  ██║███████╗    ╚██████╗██║  ██║███████╗███████║███████║  \n"  + 
    "      ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝     ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝  \n"  + 
    "                                                                                                                  \n"  + 
    "    ");
    display("[N]ew game");
    display("[E]xit");
  }

  public static void displayBoard(Board board, List<Piece> capturedWhitePieces, List<Piece> capturedBlackPieces, boolean isWhite) {
    newLine();
    if (isWhite) {
      for (int i = 7; i > -1; i--) {
        String h = HorizontalSpaceIndex.getNameByIndex(i);
        displayExact(" " + h + " ");
        for (int j = 0; j < 8; j++) {
          String v = VerticalSpaceIndex.getNameByIndex(j);
          Optional<Space> optionalSpace = board.getSpace(h, v);
          if (optionalSpace.isPresent()) {
            Space space = optionalSpace.get();
            String piece = space.hasPiece() ? space.getPiece().getEmoji() : " ";
            displayExact("[" + piece + "]");
          }
        }
        if (i == 0) {
          displayCapturedPieces(capturedBlackPieces);
        }
        if (i == 7) {
          displayCapturedPieces(capturedWhitePieces);
        }
        newLine();
      }
      displayExact("  ");
      for (VerticalSpaceIndex letter : VerticalSpaceIndex.values()) {
        displayExact("  " + letter.getName());
      }
    } else {
      for (int i = 0; i < 8; i++) {
        String h = HorizontalSpaceIndex.getNameByIndex(i);
        displayExact(" " + h + " ");
        for (int j = 7; j > -1; j--) {
          String v = VerticalSpaceIndex.getNameByIndex(j);
          Optional<Space> optionalSpace = board.getSpace(h, v);
          if (optionalSpace.isPresent()) {
            Space space = optionalSpace.get();
            String piece = space.hasPiece() ? space.getPiece().getEmoji() : " ";
            displayExact("[" + piece + "]");
          }
        }
        if (i == 0) {
          displayCapturedPieces(capturedBlackPieces);
        }
        if (i == 7) {
          displayCapturedPieces(capturedWhitePieces);
        }
        newLine();
      }
      displayExact("  ");
      for (int j = 7; j > -1; j--) {
        String v = VerticalSpaceIndex.getNameByIndex(j);
        displayExact("  " + v);
      }
    }
    newLine();
  }

  private static void displayCapturedPieces(List<Piece> capturedPieces) {
    displayExact("\t" + capturedPieces.stream().map(Piece::getEmoji).collect(joining(" ")));
  }
}