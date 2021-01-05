package com.grahambartley.terminalchess.utils;

import com.grahambartley.terminalchess.board.Space;
import com.grahambartley.terminalchess.constants.HorizontalSpaceIndex;
import com.grahambartley.terminalchess.constants.VerticalSpaceIndex;

import static java.util.Objects.nonNull;

public class CommonUtil {
  public static int getHIndex(Space space) {
    Integer hIndex = HorizontalSpaceIndex.getIndexByName(space.getH());

    return nonNull(hIndex) ? hIndex : 0;
  }

  public static int getHDiff(Space currentSpace, Space proposedSpace) {
    Integer currentHIndex = getHIndex(currentSpace);
    Integer proposedHIndex = getHIndex(proposedSpace);

    return proposedHIndex - currentHIndex;
  }

  public static int getVIndex(Space space) {
    Integer vIndex = VerticalSpaceIndex.getIndexByName(space.getV());

    return nonNull(vIndex) ? vIndex : 0;
  }

  public static int getVDiff(Space currentSpace, Space proposedSpace) {
    Integer currentVIndex = getVIndex(currentSpace);
    Integer proposedVIndex = getVIndex(proposedSpace);

    return proposedVIndex - currentVIndex;
  }
}
