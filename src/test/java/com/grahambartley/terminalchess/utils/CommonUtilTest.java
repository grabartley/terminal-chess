package com.grahambartley.terminalchess.utils;

import com.grahambartley.terminalchess.board.Space;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommonUtilTest {
  @Test
  public void testGetHIndex_valid() {
    Space space = new Space("2", "C", null);
    int expected = 1;

    int actual = CommonUtil.getHIndex(space);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetHIndex_invalid() {
    Space space = new Space("-2", "Z", null);
    int expected = 0;

    int actual = CommonUtil.getHIndex(space);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetHDiff() {
    Space space1 = new Space("2", "C", null);
    Space space2 = new Space("4", "C", null);
    int expected = 2;

    int actual = CommonUtil.getHDiff(space1, space2);

    assertEquals(expected, actual);

    space2.setH("3");
    space2.setV("D");
    expected = 1;

    actual = CommonUtil.getHDiff(space1, space2);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetVIndex_valid() {
    Space space = new Space("2", "C", null);
    int expected = 2;

    int actual = CommonUtil.getVIndex(space);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetVIndex_invalid() {
    Space space = new Space("-2", "Z", null);
    int expected = 0;

    int actual = CommonUtil.getVIndex(space);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetVDiff() {
    Space space1 = new Space("2", "C", null);
    Space space2 = new Space("2", "E", null);
    int expected = 2;

    int actual = CommonUtil.getVDiff(space1, space2);

    assertEquals(expected, actual);

    space2.setH("3");
    space2.setV("D");
    expected = 1;

    actual = CommonUtil.getVDiff(space1, space2);

    assertEquals(expected, actual);
  }
}
