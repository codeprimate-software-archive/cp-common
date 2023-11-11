/*
 * CollectionRendererTest.java (c) 25 October 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 */

package com.cp.common.log4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CollectionRendererTest extends TestCase {

  public CollectionRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CollectionRendererTest.class);
    //suite.addTest(new CollectionRendererTest("testName"));
    return suite;
  }

  public void testDoRender() throws Exception {
    final List<String> animalList = new ArrayList<String>(6);
    animalList.add("Ape");
    animalList.add("Baboon");
    animalList.add("Chimpanzee");
    animalList.add("Gorilla");
    animalList.add("Monkey");
    animalList.add("Orangutang");

    final CollectionRenderer collectionRenderer = new CollectionRenderer();

    assertEquals("{Ape, Baboon, Chimpanzee, Gorilla, Monkey, Orangutang}:java.util.ArrayList",
      collectionRenderer.doRender(animalList));

    final Set<String> emptySet = new HashSet<String>();

    assertTrue(emptySet.isEmpty());
    assertEquals("{}:java.util.HashSet", collectionRenderer.doRender(emptySet));

    // test non-Collection objects
    assertEquals("TEST", collectionRenderer.doRender("TEST"));
    assertEquals("null", collectionRenderer.doRender(null));
  }

  public void testNonCollectionRendering() throws Exception {
    final CollectionRenderer collectionRenderer = new CollectionRenderer();

    assertEquals("TEST", collectionRenderer.doRender("TEST"));
    assertEquals("null", collectionRenderer.doRender(null));
  }

}
