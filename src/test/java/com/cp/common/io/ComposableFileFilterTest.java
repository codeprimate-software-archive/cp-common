/*
 * ComposableFileFilterTest.java (c) 13 December 2003
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2009.8.13
 * @see com.cp.common.io.CommonIOTestCase
 * @see com.cp.common.io.ComposableFileFilter
 */

package com.cp.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ComposableFileFilterTest extends CommonIOTestCase {

  private FileFilter getTestFileFilter() {
    return pathname -> false;
  }

  private FileFilter getTestFileFilter(Set<File> fileSet) {
    return pathname -> fileSet.contains(pathname);
  }

  @Test
  public void testAccept() throws Exception {
    final File a = createFile("/a");
    final File b = createFile("/home/user/b");
    final File c = createFile("/local/home/user/c");
    final File d = createFile("/remote/home/user/d");

    final Set<File> fileSet1 = new HashSet<File>(2);
    fileSet1.add(a);
    fileSet1.add(c);

    final FileFilter fileFilter1 = ComposableFileFilter.add(null, getTestFileFilter(fileSet1));

    assertNotNull(fileFilter1);
    assertTrue(fileFilter1.accept(a));
    assertFalse(fileFilter1.accept(b));
    assertTrue(fileFilter1.accept(c));
    assertFalse(fileFilter1.accept(d));

    final Set<File> fileSet2 = new HashSet<File>(3);
    fileSet2.add(a);
    fileSet2.add(b);
    fileSet2.add(d);

    final FileFilter fileFilter2 = ComposableFileFilter.add(getTestFileFilter(fileSet2), null);

    assertNotNull(fileFilter2);
    assertTrue(fileFilter2.accept(a));
    assertTrue(fileFilter2.accept(b));
    assertFalse(fileFilter2.accept(c));
    assertTrue(fileFilter2.accept(d));

    final FileFilter fileFilter3 = ComposableFileFilter.add(fileFilter1, fileFilter2);

    assertNotNull(fileFilter3);
    assertTrue(fileFilter3.accept(a));
    assertFalse(fileFilter3.accept(b));
    assertFalse(fileFilter3.accept(c));
    assertFalse(fileFilter3.accept(d));
  }

  @Test
  public void testAdd() throws Exception {
    final FileFilter a = getTestFileFilter();
    final FileFilter b = getTestFileFilter();

    assertEquals(a, ComposableFileFilter.add(null, a));
    assertEquals(a, ComposableFileFilter.add(a, null));
    assertTrue(ComposableFileFilter.add(a, b) instanceof ComposableFileFilter);
  }

  @Test
  public void testContains() throws Exception {
    final FileFilter a = getTestFileFilter();
    final FileFilter b = getTestFileFilter();
    final FileFilter c = getTestFileFilter();
    final FileFilter d = getTestFileFilter();

    final ComposableFileFilter filterComposition1 = (ComposableFileFilter) ComposableFileFilter.add(a, b);
    final ComposableFileFilter filterComposition2 = (ComposableFileFilter) ComposableFileFilter.add(filterComposition1, c);

    assertTrue(filterComposition1.contains(a));
    assertTrue(filterComposition1.contains(b));
    assertFalse(filterComposition1.contains(c));
    assertFalse(filterComposition1.contains(filterComposition2));
    assertTrue(filterComposition2.contains(a));
    assertTrue(filterComposition2.contains(b));
    assertTrue(filterComposition2.contains(c));
    assertTrue(filterComposition2.contains(filterComposition1));

    final ComposableFileFilter filterComposition3 = (ComposableFileFilter) ComposableFileFilter.add(filterComposition2, d);

    assertTrue(filterComposition1.contains(a));
    assertTrue(filterComposition1.contains(b));
    assertFalse(filterComposition1.contains(c));
    assertFalse(filterComposition1.contains(d));
    assertFalse(filterComposition1.contains(filterComposition1));
    assertFalse(filterComposition1.contains(filterComposition2));
    assertFalse(filterComposition1.contains(filterComposition3));
    assertTrue(filterComposition2.contains(a));
    assertTrue(filterComposition2.contains(b));
    assertTrue(filterComposition2.contains(c));
    assertFalse(filterComposition2.contains(d));
    assertTrue(filterComposition2.contains(filterComposition1));
    assertFalse(filterComposition2.contains(filterComposition2));
    assertFalse(filterComposition2.contains(filterComposition3));
    assertTrue(filterComposition3.contains(a));
    assertTrue(filterComposition3.contains(b));
    assertTrue(filterComposition3.contains(c));
    assertTrue(filterComposition3.contains(d));
    assertTrue(filterComposition3.contains(filterComposition1));
    assertTrue(filterComposition3.contains(filterComposition2));
    assertFalse(filterComposition3.contains(filterComposition3));

    // Test a random FileFilter object for containment by the filterChain.
    assertFalse(filterComposition3.contains(getTestFileFilter()));
  }

  @Test
  public void testRemove() throws Exception {
    final FileFilter a = getTestFileFilter();
    final FileFilter b = getTestFileFilter();
    final FileFilter c = getTestFileFilter();
    final FileFilter d = getTestFileFilter();
    final FileFilter e = getTestFileFilter();
    final FileFilter f = getTestFileFilter();
    final FileFilter g = getTestFileFilter();

    final ComposableFileFilter filterComposition1 = (ComposableFileFilter) ComposableFileFilter.add(a, b);
    final ComposableFileFilter filterComposition2 = (ComposableFileFilter) ComposableFileFilter.add(filterComposition1, c);
    final ComposableFileFilter filterComposition3 = (ComposableFileFilter) ComposableFileFilter.add(filterComposition2, d);
    final ComposableFileFilter filterComposition4 = (ComposableFileFilter) ComposableFileFilter.add(filterComposition3, e);
    final ComposableFileFilter filterComposition5 = (ComposableFileFilter) ComposableFileFilter.add(filterComposition4, f);
    ComposableFileFilter filterComposition6 = (ComposableFileFilter) ComposableFileFilter.add(filterComposition5, g);

    assertTrue(filterComposition6.contains(a));
    assertTrue(filterComposition6.contains(b));
    assertTrue(filterComposition6.contains(c));
    assertTrue(filterComposition6.contains(d));
    assertTrue(filterComposition6.contains(e));
    assertTrue(filterComposition6.contains(f));
    assertTrue(filterComposition6.contains(g));

    filterComposition6 = (ComposableFileFilter) filterComposition6.remove(d);

    assertTrue(filterComposition6.contains(a));
    assertTrue(filterComposition6.contains(b));
    assertTrue(filterComposition6.contains(c));
    assertFalse(filterComposition6.contains(d));
    assertTrue(filterComposition6.contains(e));
    assertTrue(filterComposition6.contains(f));
    assertTrue(filterComposition6.contains(g));

    filterComposition6 = (ComposableFileFilter) filterComposition6.remove(filterComposition1);

    assertFalse(filterComposition6.contains(a));
    assertFalse(filterComposition6.contains(b));
    assertTrue(filterComposition6.contains(c));
    assertFalse(filterComposition6.contains(d));
    assertTrue(filterComposition6.contains(e));
    assertTrue(filterComposition6.contains(f));
    assertTrue(filterComposition6.contains(g));

    filterComposition6 = (ComposableFileFilter) filterComposition6.remove(g);

    assertFalse(filterComposition6.contains(a));
    assertFalse(filterComposition6.contains(b));
    assertTrue(filterComposition6.contains(c));
    assertFalse(filterComposition6.contains(d));
    assertTrue(filterComposition6.contains(e));
    assertTrue(filterComposition6.contains(f));
    assertFalse(filterComposition6.contains(g));
  }
}
