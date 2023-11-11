/*
 * AbstractBeanDefinitionParserTest.java (c) 23 August 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2009.6.7
 * @see com.cp.common.beans.definition.support.AbstractBeanDefinitionParser
 * @see com.cp.common.test.CommonTestCase
 */

package com.cp.common.beans.definition.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import com.cp.common.beans.definition.BeanDefinition;
import com.cp.common.test.CommonTestCase;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.core.io.Resource;

public class AbstractBeanDefinitionParserTest extends CommonTestCase {

  private static final String BEAN_DEFINITIONS_RESOURCE = "/etc/xml/mock-beans.xml";

  private static final File BEAN_DEFINITIONS_FILE = getFile(BEAN_DEFINITIONS_RESOURCE);

  protected <T extends BeanDefinition> AbstractBeanDefinitionParser<T> getBeanDefinitionParser() {
    return new TestBeanDefinitionParser<T>();
  }

  @Test
  public void testParseWithFile() throws Exception {
    Map<String, BeanDefinition> actualBeanDefinitionMap = null;

    assertNull(actualBeanDefinitionMap);

    try {
      actualBeanDefinitionMap = getBeanDefinitionParser().parse(BEAN_DEFINITIONS_FILE);
    }
    catch (Exception e) {
      fail("Calling parse with a non-null, existing bean definitions file threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(actualBeanDefinitionMap);
    assertTrue(actualBeanDefinitionMap.isEmpty());
  }

  @Test
  public void testParseWithNonExistingFile() throws Exception {
    Map<String, BeanDefinition> actualBeanDefinitionMap = null;

    assertNull(actualBeanDefinitionMap);

    try {
      actualBeanDefinitionMap = getBeanDefinitionParser().parse(new File("/mydirectory/nonExistingFile.xml"));
      fail("Calling parse with a non-null, non-existing file should have thrown a FileNotFoundException!");
    }
    catch (FileNotFoundException e) {
      // expected behavior!
    }
    catch (Exception e) {
      fail("Calling parse with a non-null, non-existing file threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualBeanDefinitionMap);
  }

  @Test
  public void testParseWithNullFile() throws Exception {
    Map<String, BeanDefinition> actualBeanDefinitionMap = null;

    assertNull(actualBeanDefinitionMap);

    try {
      actualBeanDefinitionMap = getBeanDefinitionParser().parse((File) null);
      fail("Calling parse with a null file should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean definitions file cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a null file threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualBeanDefinitionMap);
  }

  @Test
  public void testParseWithResource() throws Exception {
    final Resource mockResource = EasyMock.createMock(Resource.class);
    EasyMock.expect(mockResource.getInputStream()).andReturn(new FileInputStream(BEAN_DEFINITIONS_FILE));
    EasyMock.replay((mockResource));

    Map<String, BeanDefinition> actualBeanDefinitionMap = null;

    assertNull(actualBeanDefinitionMap);

    try {
      actualBeanDefinitionMap = getBeanDefinitionParser().parse(mockResource);
    }
    catch (Exception e) {
      fail("Calling parse with a non-null resource threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    EasyMock.verify(mockResource);

    assertNotNull(actualBeanDefinitionMap);
    assertTrue(actualBeanDefinitionMap.isEmpty());
  }

  @Test
  public void testParseWithNonExistingResource() throws Exception {
    final Resource mockResource = EasyMock.createMock(Resource.class);
    EasyMock.expect(mockResource.getInputStream()).andThrow(new IOException("Resource not found!"));
    EasyMock.replay(mockResource);

    Map<String, BeanDefinition> actualBeanDefinitionMap = null;

    assertNull(actualBeanDefinitionMap);

    try {
      actualBeanDefinitionMap = getBeanDefinitionParser().parse(mockResource);
      fail("Calling parse with a non-existing resource should have thrown an IOException!");
    }
    catch (IOException e) {
      assertEquals("Resource not found!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a non-existing resource threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    EasyMock.verify(mockResource);

    assertNull(actualBeanDefinitionMap);
  }

  @Test
  public void testParseWithNullResource() throws Exception {
    Map<String, BeanDefinition> actualBeanDefinitionMap = null;

    assertNull(actualBeanDefinitionMap);

    try {
      actualBeanDefinitionMap = getBeanDefinitionParser().parse((Resource) null);
      fail("Calling parse with a null resource should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean definitions resource cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a null resource threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualBeanDefinitionMap);
  }

  @Test
  public void testParseWithStringPathname() throws Exception {
    Map<String, BeanDefinition> actualBeanDefinitionMap = null;

    assertNull(actualBeanDefinitionMap);

    try {
      actualBeanDefinitionMap = getBeanDefinitionParser().parse(BEAN_DEFINITIONS_RESOURCE);
    }
    catch (Exception e) {
      fail("Calling parse with a non-null, non-empty String pathname threw an unexpected Exception ("
        + e.getMessage() + "!)");
    }

    assertNotNull(actualBeanDefinitionMap);
    assertTrue(actualBeanDefinitionMap.isEmpty());
  }

  @Test
  public void testParseWithEmptyStringPathname() throws Exception {
    Map<String, BeanDefinition> actualBeanDefinitionMap = null;

    assertNull(actualBeanDefinitionMap);

    try {
      actualBeanDefinitionMap = getBeanDefinitionParser().parse(" ");
      fail("Calling parse with an empty String pathname should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The absolute path of the bean definitions file cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with an empty String pathname threw an unexpected Exception ("
        + e.getMessage() + "!)");
    }

    assertNull(actualBeanDefinitionMap);
  }

  @Test
  public void testParseWithNullStringPathname() throws Exception {
    Map<String, BeanDefinition> actualBeanDefinitionMap = null;

    assertNull(actualBeanDefinitionMap);

    try {
      actualBeanDefinitionMap = getBeanDefinitionParser().parse((String) null);
      fail("Calling parse with a null String pathname should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The absolute path of the bean definitions file cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a null String pathname threw an unexpected Exception ("
        + e.getMessage() + "!)");
    }

    assertNull(actualBeanDefinitionMap);
  }

  protected static final class TestBeanDefinitionParser<T extends BeanDefinition> extends AbstractBeanDefinitionParser<T> {

    protected Map<String, T> parse(final InputStream beanDefinitionsInputStream) {
      assertNotNull(beanDefinitionsInputStream);
      return Collections.emptyMap();
    }
  }
}
