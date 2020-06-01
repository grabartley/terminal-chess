package com.grahambartley.terminalchess.board;

import static java.util.Objects.nonNull;

import com.grahambartley.terminalchess.pieces.Piece;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Space {
  private String h;
  private String v;
  private Piece piece;

  public boolean hasPiece() {
    return nonNull(piece);
  }
}