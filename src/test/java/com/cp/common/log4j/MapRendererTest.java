/*
 * MapRendererTest.java (c) 25 October 2006
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2006.10.25
 */

package com.cp.common.log4j;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MapRendererTest extends TestCase {

  public MapRendererTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(MapRendererTest.class);
    //suite.addTest(new MapRendererTest("testName"));
    return suite;
  }

  public void testDoRenderer() throws Exception {
    final Map<String, Object> personRecord = new TreeMap<String, Object>();
    personRecord.put("personId", new Integer(1));
    personRecord.put("firstName", "Jon");
    personRecord.put("lastName", "Doe");

    final MapRenderer mapRenderer = new MapRenderer();

    assertEquals("[{key = firstName, value = Jon}, {key = lastName, value = Doe}, {key = personId, value = 1}]:java.util.TreeMap",
      mapRenderer.doRender(personRecord));

    final Map<String, Object> emptyMap = new HashMap<String, Object>();

    assertTrue(emptyMap.isEmpty());
    assertEquals("[]:java.util.HashMap", mapRenderer.doRender(emptyMap));
  }

  public void testNonMapRendering() throws Exception {
    final MapRenderer mapRenderer = new MapRenderer();

    assertEquals("TEST", mapRenderer.doRender("TEST"));
    assertEquals("null", mapRenderer.doRender(null));
  }

}
