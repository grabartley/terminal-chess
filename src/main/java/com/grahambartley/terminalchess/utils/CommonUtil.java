package com.grahambartley.terminalchess.utils;

import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;

import static java.util.Objects.nonNull;

public class CommonUtil {
  public static int getHIndex(String h) {
    Integer hIndex = HorizontalSpaceIndex.getIndexByName(h);

    return nonNull(hIndex) ? hIndex : -1;
  }

  public static int getHIndex(Space space) {
    return getHIndex(space.getH());
  }

  public static int getHDiff(Space currentSpace, Space proposedSpace) {
    Integer currentHIndex = getHIndex(currentSpace);
    Integer proposedHIndex = getHIndex(proposedSpace);

    return proposedHIndex - currentHIndex;
  }

  public static int getVIndex(String v) {
    Integer vIndex = VerticalSpaceIndex.getIndexByName(v);

    return nonNull(vIndex) ? vIndex : -1;
  }

  public static int getVIndex(Space space) {
    return getVIndex(space.getV());
  }

  public static int getVDiff(Space currentSpace, Space proposedSpace) {
    Integer currentVIndex = getVIndex(currentSpace);
    Integer proposedVIndex = getVIndex(proposedSpace);

    return proposedVIndex - currentVIndex;
  }
}
