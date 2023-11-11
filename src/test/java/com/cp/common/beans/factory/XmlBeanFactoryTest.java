/*
 * XmlBeanFactoryTest.java (c) 12 November 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John Blum
 * @version 2008.12.27
 * @see com.cp.common.beans.factory.XmlBeanFactory
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.factory;

import com.cp.common.beans.Bean;
import com.cp.common.beans.definition.BeanDeclaration;
import com.cp.common.beans.definition.Scope;
import com.cp.common.beans.definition.support.BeanDefinitionParser;
import com.cp.common.beans.definition.support.XmlBeanDeclarationParser;
import com.cp.common.beans.factory.support.BeanBuilderVisitor;
import com.cp.common.context.config.Config;
import com.cp.common.lang.Destroyable;
import com.cp.common.test.mock.MockBeanImpl;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.SystemException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.TreeMap;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.easymock.EasyMock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class XmlBeanFactoryTest extends TestCase {

  private static final String MOCK_BEANS_XML_FILE = "/etc/xml/mock-beans.xml";

  public XmlBeanFactoryTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(XmlBeanFactoryTest.class);
    return suite;
  }

  public void testInstantiation() throws Exception {
    BeanFactory beanFactory = null;

    assertNull(beanFactory);

    try {
      beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    }
    catch (Exception e) {
      fail("Instantiating an instance of the XmlBeanFactory with a non-null Config object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(beanFactory);
  }

  public void testInstantiationWithNullConfig() throws Exception {
    BeanFactory beanFactory = null;

    assertNull(beanFactory);

    try {
      beanFactory = new XmlBeanFactory(null);
      fail("Instantiating an instance of the XmlBeanFactory with a null Config object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The configuration object cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Instantiating an instance of the XmlBeanFactory with a null Config object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(beanFactory);
  }

  public void testInstantiationWithConfigAndBeanDefinitionsFile() throws Exception {
    final File beanDefinitionsFile = new File(XmlBeanFactoryTest.class.getResource(MOCK_BEANS_XML_FILE).toURI());
    BeanFactory beanFactory = null;

    assertNull(beanFactory);

    try {
      beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class), beanDefinitionsFile);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the XmlBeanFactory with a non-null Config object and a bean definitions file threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(beanFactory);
    assertSame(beanDefinitionsFile, ((XmlBeanFactory) beanFactory).getBeanDefinitionsFile());
  }

  public void testInstantiationWithConfigAndBeanDefinitionsResource() throws Exception {
    final Resource beanDefinitionsResource = new ClassPathResource(MOCK_BEANS_XML_FILE);
    BeanFactory beanFactory = null;

    assertNull(beanFactory);

    try {
      beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class), beanDefinitionsResource);
    }
    catch (Exception e) {
      fail("Instantiating an instance of the XmlBeanFactory with a non-null Config object and a bean definitions resource threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNotNull(beanFactory);
    assertEquals(beanDefinitionsResource.getFile(), ((XmlBeanFactory) beanFactory).getBeanDefinitionsFile());
  }

  public void testInvokeDestroyMethod() throws Exception {
    final TestBeanImpl testBean = new TestBeanImpl();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getDestroyMethod()).andReturn("destroy");
    EasyMock.expectLastCall().times(2);
    EasyMock.replay(mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    assertFalse(testBean.isDestroyCalled());

    beanFactory.invokeDestroyMethod(mockBeanDeclaration, testBean);

    assertTrue(testBean.isDestroyCalled());
    EasyMock.verify(mockBeanDeclaration);
  }

  public void testInvokeDestroyMethodForNonExistingMethod() throws Exception {
    final TestBeanImpl testBean = new TestBeanImpl();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getDestroyMethod()).andReturn("tearDown");
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    assertFalse(testBean.isDestroyCalled());

    try {
      beanFactory.invokeDestroyMethod(mockBeanDeclaration, testBean);
      fail("Calling invokeDestroyMethod on TestBeanImpl with a non-existing method (tearDown) should have thrown a ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("No such public method (tearDown) exists on class (" + testBean.getClass().getName() + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling invokeDestroyMethod on TestBeanImpl with a non-existing method (tearDown) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertFalse(testBean.isDestroyCalled());
    EasyMock.verify(mockBeanDeclaration);
  }

  public void testInvokeDestroyMethodWithNoMethodSpecified() throws Exception {
    final TestBeanImpl testBean = new TestBeanImpl();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getDestroyMethod()).andReturn(null);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    assertFalse(testBean.isDestroyCalled());

    beanFactory.invokeDestroyMethod(mockBeanDeclaration, testBean);

    assertFalse(testBean.isDestroyCalled());
    EasyMock.verify(mockBeanDeclaration);
  }

  public void testInvokeInitMethod() throws Exception {
    final TestBeanImpl testBean = new TestBeanImpl();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getInitMethod()).andReturn("init");
    EasyMock.expectLastCall().times(2);
    EasyMock.replay(mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    assertFalse(testBean.isInitCalled());

    beanFactory.invokeInitMethod(mockBeanDeclaration, testBean);

    assertTrue(testBean.isInitCalled());
    EasyMock.verify(mockBeanDeclaration);
  }

  public void testInvokeInitMethodForNonExistingMethod() throws Exception {
    final TestBeanImpl testBean = new TestBeanImpl();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getInitMethod()).andReturn("setUp");
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    assertFalse(testBean.isInitCalled());

    try {
      beanFactory.invokeInitMethod(mockBeanDeclaration, testBean);
      fail("Calling invokeInitMethod on TestBeanImpl with a non-existing method (setUp) should have thrown a ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("No such public method (setUp) exists on class (" + testBean.getClass().getName() + ")!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling invokeInitMetho on TestBeanImpl with a non-existing method (setUp) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertFalse(testBean.isInitCalled());
    EasyMock.verify(mockBeanDeclaration);
  }

  public void testInvokeInitMethodWithNoMethodSpecified() throws Exception {
    final TestBeanImpl testBean = new TestBeanImpl();

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getInitMethod()).andReturn(" ");
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    assertFalse(testBean.isInitCalled());

    beanFactory.invokeInitMethod(mockBeanDeclaration, testBean);

    assertFalse(testBean.isInitCalled());
    EasyMock.verify(mockBeanDeclaration);
  }

  public void testIsMethodOf() throws Exception {
    final Bean mockBean = new MockBeanImpl();
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    assertFalse(beanFactory.isMethodOf(mockBean, null));
    assertFalse(beanFactory.isMethodOf(mockBean, " "));
    assertTrue(beanFactory.isMethodOf(mockBean, "getValue"));
    assertTrue(beanFactory.isMethodOf(mockBean, "setValue"));
  }

  public void testIsMethodOfWithNullObject() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    boolean isMethodOf = false;

    assertFalse(isMethodOf);

    try {
      isMethodOf = beanFactory.isMethodOf(null, "class");
      fail("Calling isMethodOf with a null Object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The object cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling isMethodOf with a null Object threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertFalse(isMethodOf);
  }

  public void testIsNotMethodOf() throws Exception {
    final Bean mockBean = new MockBeanImpl();
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    boolean isMethodOf = false;

    assertFalse(isMethodOf);

    try {
      isMethodOf = beanFactory.isMethodOf(mockBean, "init");
      fail("Calling isMethodOf on the MockBeanImpl class for method (init) should have thrown a ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("No such public method (init) exists on class (" + MockBeanImpl.class.getName() + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling isMethodOf on the MockBeanImpl class for method (init) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertFalse(isMethodOf);
  }

  public void testGetAliases() throws Exception {
    final String[] expectedNames = { "fakeBean", "mockBean", "simulatedBean", "testBean" };

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getNames()).andReturn(expectedNames);
    EasyMock.expectLastCall().atLeastOnce();
    EasyMock.replay(mockBeanDeclaration);

    final Map<String, BeanDeclaration> mockBeanDeclarationMap = new HashMap<String, BeanDeclaration>(1);
    mockBeanDeclarationMap.put("myBean", mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class)) {
      @Override
      protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
        return mockBeanDeclarationMap;
      }
    };

    String[] actualNames = null;

    assertNull(actualNames);

    try {
      actualNames = beanFactory.getAliases("myBean");
    }
    catch (Exception e) {
      fail("Calling getAliases with bean ID (myBean) threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(actualNames);
    assertEquals(expectedNames, actualNames);
  }

  public void testGetBeanDeclaration() throws Exception {
    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getNames()).andReturn(new String[] { "fakeBean", "mockBean", "simulatedBean", "testBean" });
    EasyMock.expectLastCall().atLeastOnce();
    EasyMock.replay(mockBeanDeclaration);

    final Map<String, BeanDeclaration> mockBeanDeclarationMap = new TreeMap<String, BeanDeclaration>();
    mockBeanDeclarationMap.put("myBean", mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class)) {
      @Override
      protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
        return mockBeanDeclarationMap;
      }
    };

    assertSame(mockBeanDeclaration, beanFactory.getBeanDeclaration("myBean"));
    assertSame(mockBeanDeclaration, beanFactory.getBeanDeclaration("fakeBean"));
    assertSame(mockBeanDeclaration, beanFactory.getBeanDeclaration("mockBean"));
    assertSame(mockBeanDeclaration, beanFactory.getBeanDeclaration("simulatedBean"));
    assertSame(mockBeanDeclaration, beanFactory.getBeanDeclaration("testBean"));

    EasyMock.verify(mockBeanDeclaration);
  }

  public void testGetBeanDeclarationWithEmptyBeanId() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    BeanDeclaration actualBeanDeclaration = null;

    assertNull(actualBeanDeclaration);

    try {
      actualBeanDeclaration = beanFactory.getBeanDeclaration(" ");
      fail("Calling getBeanDeclaration with an empty bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBeanDeclaration with an empty bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualBeanDeclaration);
  }

  public void testGetBeanDeclarationWithNullBeanId() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    BeanDeclaration actualBeanDeclaration = null;

    assertNull(actualBeanDeclaration);

    try {
      actualBeanDeclaration = beanFactory.getBeanDeclaration(null);
      fail("Calling getBeanDeclaration with a null bean ID should have thrown an IllegalArgumentException!");
    }
    catch (IllegalArgumentException e) {
      assertEquals("The bean id cannot be null or empty!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBeanDeclaration with a null bean ID threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(actualBeanDeclaration);
  }

  public void testGetBeanDeclarationForAnUnfoundBean() throws Exception {
    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getNames()).andReturn(new String[] { "fakeBean", "mockBean", "simulatedBean", "testBean" });
    EasyMock.expectLastCall().atLeastOnce();
    EasyMock.replay(mockBeanDeclaration);

    final Map<String, BeanDeclaration> mockBeanDeclarationMap = new TreeMap<String, BeanDeclaration>();
    mockBeanDeclarationMap.put("myBean", mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class)) {
      @Override
      protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
        return mockBeanDeclarationMap;
      }
    };

    BeanDeclaration actualBeanDeclaration = null;

    assertNull(actualBeanDeclaration);

    try {
      actualBeanDeclaration = beanFactory.getBeanDeclaration("unfoundBean");
      fail("Calling getBeanDeclaration for an unfound bean (unfoundBean) should have thrown a BeanNotFoundException!");
    }
    catch (BeanNotFoundException e) {
      assertEquals("The bean identified by id (unfoundBean) cannot be found!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBeanDeclaration for an unfound bean (unfoundBean) threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualBeanDeclaration);

    EasyMock.verify(mockBeanDeclaration);
  }

  public void testGetBeanDeclarationMapForUninitializedBeanFactory() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    Map<String, BeanDeclaration> actualBeanDeclarationMap = null;

    assertNull(actualBeanDeclarationMap);

    try {
      actualBeanDeclarationMap = beanFactory.getBeanDeclarationMap();
      fail("Calling getBeanDeclarationMap for an uninitialized BeanFactory should have thrown an IllegalStateException!");
    }
    catch (IllegalStateException e) {
      assertEquals("The XmlBeanFactory has not been properly initialized with a beans configuration file!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBeanDeclarationMap for an uninitialized BeanFactory threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualBeanDeclarationMap);
  }

  public void testGetBeanDefinitionParser() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.getStringPropertyValue(XmlBeanFactory.BEAN_DEFINITION_PARSER_PROPERTY)).andReturn(
      MockBeanDefinitionParser.class.getName());
    EasyMock.expectLastCall().atLeastOnce();
    EasyMock.replay(mockConfig);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(mockConfig);

    final BeanDefinitionParser<BeanDeclaration> actualBeanDefinitionParser = beanFactory.getBeanDefinitionParser();

    assertTrue(actualBeanDefinitionParser instanceof MockBeanDefinitionParser);

    EasyMock.verify(mockConfig);
  }

  public void testGetBeanDefinitionParserWithMissingResource() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.getStringPropertyValue(XmlBeanFactory.BEAN_DEFINITION_PARSER_PROPERTY)).andThrow(
      new MissingResourceException("The bean definition parser property was not found!", null, XmlBeanFactory.BEAN_DEFINITION_PARSER_PROPERTY));
    EasyMock.expectLastCall().atLeastOnce();
    EasyMock.replay(mockConfig);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(mockConfig);

    final BeanDefinitionParser<BeanDeclaration> actualBeanDefinitionParser = beanFactory.getBeanDefinitionParser();

    assertTrue(actualBeanDefinitionParser instanceof XmlBeanDeclarationParser);

    EasyMock.verify(mockConfig);
  }

  public void testSetBeanDefinitionParser() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    final BeanDefinitionParser<BeanDeclaration> expectedBeanDefinitionParser = new MockBeanDefinitionParser();

    try {
      beanFactory.setBeanDefinitionParser(expectedBeanDefinitionParser);
    }
    catch (Exception e) {
      fail("Calling setBeanDefinitionParser with a non-null BeanDefinitionParser object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertSame(expectedBeanDefinitionParser, beanFactory.getBeanDefinitionParser());
  }

  public void testSetBeanDefinitionParserWithNullObject() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    try {
      beanFactory.setBeanDefinitionParser(null);
      fail("Calling setBeanDefinitionParser with a null object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean definition parser cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setBeanDefinitionParser with a null object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testGetBeanDefinitionsFile() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.getStringPropertyValue(XmlBeanFactory.BEAN_DEFINITIONS_FILE_PROPERTY,
      XmlBeanFactory.DEFAULT_BEAN_DEFINITIONS_FILE)).andReturn(MOCK_BEANS_XML_FILE);
    EasyMock.replay(mockConfig);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(mockConfig);

    final File expectedBeanDefinitionsFile = new File(XmlBeanFactoryTest.class.getResource(MOCK_BEANS_XML_FILE).toURI());
    File actualBeanDefinitionsFile = null;

    assertNull(actualBeanDefinitionsFile);

    try {
      actualBeanDefinitionsFile = beanFactory.getBeanDefinitionsFile();
    }
    catch (Exception e) {
      fail("Calling getBeanDefinitionsFile threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNotNull(actualBeanDefinitionsFile);
    assertEquals(expectedBeanDefinitionsFile, actualBeanDefinitionsFile);
    EasyMock.verify(mockConfig);
  }

  public void testGetBeanDefinitionsFileForAFileNotFound() throws Exception {
    final Config mockConfig = EasyMock.createMock(Config.class);
    EasyMock.expect(mockConfig.getStringPropertyValue(XmlBeanFactory.BEAN_DEFINITIONS_FILE_PROPERTY,
      XmlBeanFactory.DEFAULT_BEAN_DEFINITIONS_FILE)).andReturn(XmlBeanFactory.DEFAULT_BEAN_DEFINITIONS_FILE);
    EasyMock.replay(mockConfig);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(mockConfig);
    File actualBeanDefinitionsFile = null;

    assertNull(actualBeanDefinitionsFile);

    try {
      actualBeanDefinitionsFile = beanFactory.getBeanDefinitionsFile();
      fail("Calling getBeanDefinitionsFile with a non-existing beans XML file should have thrown an FileNotFoundException!");
    }
    catch (FileNotFoundException e) {
      assertEquals("The bean definitions file (" + XmlBeanFactory.DEFAULT_BEAN_DEFINITIONS_FILE
        + ") could not be found!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getBeanDefinitionsFile with a non-existing beans XML file threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualBeanDefinitionsFile);
    EasyMock.verify(mockConfig);
  }

  public void testSetBeanDefinitionsFile() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    final File expectedBeanDefinitionsFile = new File(XmlBeanFactory.DEFAULT_BEAN_DEFINITIONS_FILE);

    try {
      beanFactory.setBeanDefinitionsFile(expectedBeanDefinitionsFile);
    }
    catch (Exception e) {
      fail("Calling setBeanDefinitionsFile with a valid File object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(expectedBeanDefinitionsFile, beanFactory.getBeanDefinitionsFile());
  }

  public void testSetBeanDefinitionsFileWithNullFile() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    try {
      beanFactory.setBeanDefinitionsFile(null);
      fail("Calling setBeanDefinitionsFile with a null File object should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean definitions file cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setBeanDefinitionsFile with a null File object threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testSetBeanDefinitionsResource() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    final File expectedBeanDefinitionsFile = new File(XmlBeanFactoryTest.class.getResource(MOCK_BEANS_XML_FILE).toURI());

    try {
      beanFactory.setBeanDefinitionsResource(new ClassPathResource(MOCK_BEANS_XML_FILE));
    }
    catch (Exception e) {
      fail("Calling setBeanDefinitionsResource with a non-null, existing Resource threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertEquals(expectedBeanDefinitionsFile, beanFactory.getBeanDefinitionsFile());
  }

  public void testSetBeanDefinitionsResourceWithNullResource() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));

    try {
      beanFactory.setBeanDefinitionsResource(null);
      fail("Calling setBeanDefinitionsResource with a null Resource should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean definitions resource cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setBeanDefinitionsResource with a null Resource threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testSetBeanDefinitionsResourceForAResourceNotFound() throws Exception {
    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class));
    final Resource expectedResource = new ClassPathResource(XmlBeanFactory.DEFAULT_BEAN_DEFINITIONS_FILE);

    try {
      beanFactory.setBeanDefinitionsResource(expectedResource);
      fail("Calling setBeanDefinitionsResource with a non-existing Resource should have thrown an ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("Failed to configure the bean factory (" + beanFactory.getClass().getName()
        + ") with bean definitions resource (" + expectedResource + ")!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling setBeanDefinitionsResource with a non-existing Resource threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testGetBeanObjectForNonExistingNonSingletonBean() throws Exception {
    final BeanBuilderVisitor mockBeanBuilderVisitor = EasyMock.createMock(BeanBuilderVisitor.class);
    EasyMock.expect(mockBeanBuilderVisitor.getBean()).andReturn(new TestBeanImpl("mockValue"));
    EasyMock.expect(mockBeanBuilderVisitor.getBean()).andReturn(new TestBeanImpl("testValue"));
    EasyMock.replay(mockBeanBuilderVisitor);

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getNames()).andReturn(new String[] { "fakeBean", "mockBean", "simulatedBean", "testBean" });
    EasyMock.expectLastCall().times(1);
    EasyMock.expect(mockBeanDeclaration.getId()).andReturn("myBean");
    EasyMock.expectLastCall().times(2);
    mockBeanDeclaration.accept(mockBeanBuilderVisitor);
    EasyMock.expectLastCall().times(2);
    EasyMock.expect(mockBeanDeclaration.getInitMethod()).andReturn("init");
    EasyMock.expectLastCall().times(4);
    EasyMock.expect(mockBeanDeclaration.isSingleton()).andReturn(false);
    EasyMock.expectLastCall().times(2);
    EasyMock.replay(mockBeanDeclaration);

    final Map<String, BeanDeclaration> mockBeanDeclarationMap = new HashMap<String, BeanDeclaration>(1);
    mockBeanDeclarationMap.put("myBean", mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class)) {
      @Override
      protected BeanBuilderVisitor getBeanBuilderVisitorImpl(final BeanFactory beanFactory, final Class[] argTypes, final Object[] args) {
        return mockBeanBuilderVisitor;
      }

      @Override
      protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
        return mockBeanDeclarationMap;
      }
    };

    final Bean myBean = beanFactory.getBeanObject("myBean", null, null);

    assertNotNull(myBean);
    assertTrue(myBean instanceof TestBeanImpl);
    assertEquals("mockValue", ((TestBeanImpl) myBean).getValue());

    final Bean mockBean = beanFactory.getBeanObject("mockBean", null, null);

    assertNotNull(mockBean);
    assertTrue(mockBean instanceof TestBeanImpl);
    assertEquals("testValue", ((TestBeanImpl) mockBean).getValue());
    assertNotSame(myBean, mockBean);

    EasyMock.verify(mockBeanBuilderVisitor, mockBeanDeclaration);
  }

  public void testGetBeanObjectForNonExistingSingletonBean() throws Exception {
    final BeanBuilderVisitor mockBeanBuilderVisitor = EasyMock.createMock(BeanBuilderVisitor.class);
    EasyMock.expect(mockBeanBuilderVisitor.getBean()).andReturn(new TestBeanImpl("testValue"));
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBeanBuilderVisitor);

    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getNames()).andReturn(new String[] { "fakeBean", "mockBean", "simulatedBean", "testBean" });
    EasyMock.expectLastCall().atLeastOnce();
    EasyMock.expect(mockBeanDeclaration.getId()).andReturn("myBean");
    EasyMock.expectLastCall().atLeastOnce();
    mockBeanDeclaration.accept(mockBeanBuilderVisitor);
    EasyMock.expectLastCall().once();
    EasyMock.expect(mockBeanDeclaration.getInitMethod()).andReturn("init");
    EasyMock.expectLastCall().times(2);
    EasyMock.expect(mockBeanDeclaration.isSingleton()).andReturn(true);
    EasyMock.expectLastCall().once();
    EasyMock.replay(mockBeanDeclaration);

    final Map<String, BeanDeclaration> mockBeanDeclarationMap = new HashMap<String, BeanDeclaration>(1);
    mockBeanDeclarationMap.put("myBean", mockBeanDeclaration);

    final XmlBeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class)) {
      @Override
      protected BeanBuilderVisitor getBeanBuilderVisitorImpl(final BeanFactory beanFactory, final Class[] argTypes, final Object[] args) {
        return mockBeanBuilderVisitor;
      }

      @Override
      protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
        return mockBeanDeclarationMap;
      }
    };

    final Bean myBean = beanFactory.getBeanObject("myBean", null, null);

    assertNotNull(myBean);
    assertTrue(myBean instanceof TestBeanImpl);
    assertEquals("testValue", ((TestBeanImpl) myBean).getValue());
    assertSame(myBean, beanFactory.getBeanObject("fakeBean", null, null));
    assertSame(myBean, beanFactory.getBeanObject("mockBean", null, null));
    assertSame(myBean, beanFactory.getBeanObject("testBean", null, null));

    EasyMock.verify(mockBeanBuilderVisitor, mockBeanDeclaration);
  }

  public void testGetScope() throws Exception {
    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getScope()).andReturn(Scope.SINGLETON);
    EasyMock.replay(mockBeanDeclaration);

    final BeanDeclaration testBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(testBeanDeclaration.getScope()).andReturn(null);
    EasyMock.replay(testBeanDeclaration);

    final Map<String, BeanDeclaration> mockBeanDeclarationMap = new HashMap<String, BeanDeclaration>(1);
    mockBeanDeclarationMap.put("mockBean", mockBeanDeclaration);
    mockBeanDeclarationMap.put("testBean", testBeanDeclaration);

    final BeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class)) {
      @Override
      protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
        return mockBeanDeclarationMap;
      }
    };

    assertEquals(Scope.SINGLETON, beanFactory.getScope("mockBean"));
    assertEquals(Scope.PROTOTYPE, beanFactory.getScope("testBean"));
    EasyMock.verify(mockBeanDeclaration);
    EasyMock.verify(testBeanDeclaration);
  }

  public void testGetType() throws Exception {
    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getType()).andReturn(MockBeanImpl.class);
    EasyMock.replay(mockBeanDeclaration);

    final Map<String, BeanDeclaration> mockBeanDeclarationMap = new HashMap<String, BeanDeclaration>(1);
    mockBeanDeclarationMap.put("mockBean", mockBeanDeclaration);

    final BeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class)) {
      @Override
      protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
        return mockBeanDeclarationMap;
      }
    };

    assertEquals(MockBeanImpl.class, beanFactory.getType("mockBean"));
    EasyMock.verify(mockBeanDeclaration);
  }

  public void testGetTypeWithUnfoundClassType() throws Exception {
    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getClassName()).andReturn("non.existing.bean.Class");
    EasyMock.expectLastCall().times(2);
    EasyMock.expect(mockBeanDeclaration.getType()).andThrow(new ClassNotFoundException("Class Not Found!"));
    EasyMock.replay(mockBeanDeclaration);

    final Map<String, BeanDeclaration> mockBeanDeclarationMap = new HashMap<String, BeanDeclaration>(1);
    mockBeanDeclarationMap.put("mockBean", mockBeanDeclaration);

    final BeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class)) {
      @Override
      protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
        return mockBeanDeclarationMap;
      }
    };

    Class actualType = null;

    assertNull(actualType);

    try {
      actualType = beanFactory.getType("mockBean");
      fail("Calling getType for bean (mockBean) with an unfound Class type should have thrown a SystemException!");
    }
    catch (SystemException e) {
      assertEquals("The fully qualified class name (non.existing.bean.Class) of bean identified by id (mockBean) cannot be found in the CLASSPATH!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling getType for bean (mockBean) with an unfound Class type threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }

    assertNull(actualType);
    EasyMock.verify(mockBeanDeclaration);
  }

  public void testContainsBean() throws Exception {
    final BeanDeclaration mockBeanDeclaration = EasyMock.createMock(BeanDeclaration.class);
    EasyMock.expect(mockBeanDeclaration.getNames()).andReturn(new String[] { "fakeBean", "mockBean", "simulatedBean", "testBean" });
    EasyMock.expectLastCall().atLeastOnce();
    EasyMock.replay(mockBeanDeclaration);

    final Map<String, BeanDeclaration> mockBeanDeclarationMap = new HashMap<String, BeanDeclaration>(1);
    mockBeanDeclarationMap.put("myBean", mockBeanDeclaration);

    final BeanFactory beanFactory = new XmlBeanFactory(EasyMock.createMock(Config.class)) {
      @Override
      protected Map<String, BeanDeclaration> getBeanDeclarationMap() {
        return mockBeanDeclarationMap;
      }
    };

    assertTrue(beanFactory.containsBean("myBean"));
    assertTrue(beanFactory.containsBean("mockBean"));
    assertTrue(beanFactory.containsBean("testBean"));
    assertFalse(beanFactory.containsBean("theBean"));
    assertFalse(beanFactory.containsBean("FakeBean"));
    assertFalse(beanFactory.containsBean("SimBean"));

    EasyMock.verify(mockBeanDeclaration);
  }

  public static final class MockBeanDefinitionParser implements BeanDefinitionParser<BeanDeclaration> {

    public Map<String, BeanDeclaration> parse(final File beanDefinitionsFile) throws FileNotFoundException {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public Map<String, BeanDeclaration> parse(final Resource beanDefinitionsResource) throws IOException {
      throw new UnsupportedOperationException("Not Implemented!");
    }

    public Map<String, BeanDeclaration> parse(final String beanDefinitionsPathname) {
      throw new UnsupportedOperationException("Not Implemented!");
    }
  }

  public static final class TestBeanImpl extends MockBeanImpl implements Destroyable {

    private boolean destroyCalled = false;
    private boolean initCalled = false;

    private Object value;

    public TestBeanImpl() {
    }

    public TestBeanImpl(final Object value) {
      this.value = value;
    }

    public boolean isDestroyCalled() {
      return destroyCalled;
    }

    public boolean isInitCalled() {
      return initCalled;
    }

    public boolean isDestroyed() {
      return destroyCalled;
    }

    public void destroy() {
      destroyCalled = true;
      setValue(null);
    }

    public void init() {
      initCalled = true;
      setValue(value);
    }

    @Override
    public void reset() {
      super.reset();
      this.destroyCalled = false;
      this.initCalled = false;
    }
  }

}
