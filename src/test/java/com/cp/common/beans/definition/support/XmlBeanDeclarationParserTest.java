/*
 * XmlBeanDeclarationParserTest.java (c) 2 January 2008
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.3
 * @see com.cp.common.beans.definition.support.XmlBeanDeclarationParser
 * @see junit.framework.TestCase
 */

package com.cp.common.beans.definition.support;

import com.cp.common.beans.definition.BeanDeclaration;
import com.cp.common.beans.definition.BeanDefinition;
import com.cp.common.beans.definition.BeanDefinitionsFactory;
import com.cp.common.beans.definition.InvocationArgument;
import com.cp.common.beans.definition.ListenerDeclaration;
import com.cp.common.beans.definition.ListenerDefinition;
import com.cp.common.beans.definition.Parameterizable;
import com.cp.common.beans.definition.PropertyDeclaration;
import com.cp.common.beans.definition.Scope;
import static com.cp.common.beans.definition.support.XmlBeanDeclarationParser.*;
import com.cp.common.test.mock.MockBeanImpl;
import com.cp.common.test.util.TestUtil;
import com.cp.common.util.ConfigurationException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.dom4j.Element;
import org.easymock.EasyMock;
import org.springframework.core.io.ClassPathResource;

public class XmlBeanDeclarationParserTest extends TestCase {

  private static final String MOCK_BEANS_XML_FILE = "/etc/xml/mock-beans.xml";

  private static XmlBeanDeclarationParser parser;

  public XmlBeanDeclarationParserTest(final String testName) {
    super(testName);
  }

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(XmlBeanDeclarationParserTest.class);
    //suite.addTest(new XmlBeanDeclarationParserTest("testName"));
    return new DefaultXmlBeanDeclarationParserTestSetup(suite);
  }

  protected BeanDeclaration addBeanDeclaration(final Map<String, BeanDeclaration> beanMap,
                                               final String beanId,
                                               final String className) {
    final BeanDeclaration bean = BeanDefinitionsFactory.getBeanDeclaration(beanId, className);
    beanMap.put(beanId, bean);
    return bean;
  }

  protected void addConstructorArgument(final Parameterizable parameterizable,
                                        final String formatPattern,
                                        final String refId,
                                        final String type,
                                        final String value) {
    final InvocationArgument constructorArgument = BeanDefinitionsFactory.getInvocationArgument(type, value);
    constructorArgument.setFormatPattern(formatPattern);
    constructorArgument.setRefId(refId);
    parameterizable.add(constructorArgument);
  }

  protected void addListenerDeclaration(final BeanDeclaration beanDefinition,
                                        final String className,
                                        final Scope scope,
                                        final String... properties) {
    final ListenerDeclaration listener = BeanDefinitionsFactory.getListenerDeclaration(className);
    listener.setScope(scope);

    for (final String property : properties) {
      listener.add(property);
    }

    beanDefinition.add(listener);
  }

  protected void addPropertyDeclaration(final BeanDeclaration beanDefinition,
                                        final String propertyName,
                                        final String value,
                                        final String refId,
                                        final String formatPattern) {
    final PropertyDeclaration property = BeanDefinitionsFactory.getPropertyDeclaration(propertyName);
    property.setValue(value);
    property.setRefId(refId);
    property.setFormatPattern(formatPattern);
    beanDefinition.add(property);
  }

  protected void assertEquals(final Map<String, BeanDefinition> expectedBeanMap, final Map<String, BeanDefinition> actualBeanMap) {
    assertNotNull("The expected bean map cannot be null!", expectedBeanMap);
    assertNotNull("The actual bean map cannot be null!", actualBeanMap);
    assertEquals("The expected and actual bean map are not the same size!", expectedBeanMap.size(), actualBeanMap.size());

    for (final String beanId : expectedBeanMap.keySet()) {
      assertEquals(expectedBeanMap.get(beanId), actualBeanMap.get(beanId));
    }
  }

  protected void assertEquals(final BeanDeclaration expectedBeanDefinition, final BeanDeclaration actualBeanDefinition) {
    assertNotNull("The expected bean definition cannot be null!", expectedBeanDefinition);
    assertNotNull("The actual bean definition cannot be null!", actualBeanDefinition);

    TestUtil.assertNullEquals(expectedBeanDefinition.getClassName(), actualBeanDefinition.getClassName());
    TestUtil.assertNullEquals(expectedBeanDefinition.getDestroyMethod(), actualBeanDefinition.getDestroyMethod());
    TestUtil.assertNullEquals(expectedBeanDefinition.getId(), actualBeanDefinition.getId());
    TestUtil.assertNullEquals(expectedBeanDefinition.getInitMethod(), actualBeanDefinition.getInitMethod());
    TestUtil.assertNullEquals(expectedBeanDefinition.isLazyInit(), actualBeanDefinition.isLazyInit());
    TestUtil.assertNullEquals(expectedBeanDefinition.getName(), actualBeanDefinition.getName());
    TestUtil.assertNullEquals(expectedBeanDefinition.getScope(), actualBeanDefinition.getScope());
//    TestUtil.assertNullEquals(expectedBeanDefinition.getType(), actualBeanDefinition.getType());

    int index = 0;

    for (final InvocationArgument expectedConstructorArgument : expectedBeanDefinition.getInvocationArguments()) {
      assertEquals(expectedConstructorArgument, actualBeanDefinition.getInvocationArgument(index++));
    }

    for (final ListenerDefinition expectedListener : expectedBeanDefinition.getListeners()) {
      assertEquals(expectedListener, actualBeanDefinition.getListenerDefinition(expectedListener.getClassName()));
    }

    for (final PropertyDeclaration expectedProperty : expectedBeanDefinition.getProperties()) {
      assertEquals(expectedProperty, actualBeanDefinition.getPropertyDefinition(expectedProperty.getName()));
    }
  }

  protected void assertEquals(final InvocationArgument expectedConstructorArgument, final InvocationArgument actualConstructorArgument) {
    assertNotNull("The expected constructor argument cannot be null!", expectedConstructorArgument);
    assertNotNull("The actual constructor argument cannot be null!", actualConstructorArgument);

    TestUtil.assertNullEquals(expectedConstructorArgument.getFormatPattern(), actualConstructorArgument.getFormatPattern());
    TestUtil.assertNullEquals(expectedConstructorArgument.getRefId(), actualConstructorArgument.getRefId());
    TestUtil.assertNullEquals(expectedConstructorArgument.getStringType(), actualConstructorArgument.getStringType());
    TestUtil.assertNullEquals(expectedConstructorArgument.getStringValue(), actualConstructorArgument.getStringValue());
  }

  protected void assertEquals(final ListenerDeclaration expectedListener, final ListenerDeclaration actualListener) {
    assertNotNull("The expected listener cannot be null!", expectedListener);
    assertNotNull("The actual listener cannot be null!", actualListener);

    TestUtil.assertNullEquals(expectedListener.getClassName(), actualListener.getClassName());
    TestUtil.assertNullEquals(expectedListener.getScope(), actualListener.getScope());

    int index = 0;

    for (final InvocationArgument expectedConstructorArgument : expectedListener.getInvocationArguments()) {
      assertEquals(expectedConstructorArgument, actualListener.getInvocationArgument(index++));
    }

    for (final String propertyName : expectedListener.getProperties()) {
      assertTrue("Expected actual listener to contain property (" + propertyName + ")!", actualListener.contains(propertyName));
    }
  }

  protected void assertEquals(final PropertyDeclaration expectedProperty, final PropertyDeclaration actualProperty) {
    assertNotNull("The expected property cannot be null!", expectedProperty);
    assertNotNull("The actual property cannot be null!", actualProperty);

    TestUtil.assertNullEquals(expectedProperty.getClassName(), actualProperty.getClassName());
    TestUtil.assertNullEquals("Expected format pattern (" + expectedProperty.getFormatPattern() + ") but was ("
      + actualProperty.getFormatPattern() + ")!", expectedProperty.getFormatPattern(), actualProperty.getFormatPattern());
    TestUtil.assertNullEquals(expectedProperty.getName(), actualProperty.getName());
    TestUtil.assertNullEquals(expectedProperty.getRefId(), actualProperty.getRefId());
    TestUtil.assertNullEquals(expectedProperty.getValue(), actualProperty.getValue());
  }

  protected Element createMockBeanElement(final String classAttribute,
                                          final String destroyMethod,
                                          final String id,
                                          final String initMethod,
                                          final String lazyInit,
                                          final String name,
                                          final String scope)
  {
    final Element mockBeanElement = EasyMock.createMock(Element.class);
    EasyMock.expect(mockBeanElement.attributeValue(CLASS_ATTRIBUTE)).andReturn(classAttribute);
    EasyMock.expect(mockBeanElement.attributeValue(DESTROY_METHOD_ATTRIBUTE)).andReturn(destroyMethod);
    EasyMock.expect(mockBeanElement.attributeValue(ID_ATTRIBUTE)).andReturn(id);
    EasyMock.expect(mockBeanElement.attributeValue(INIT_METHOD_ATTRIBUTE)).andReturn(initMethod);
    EasyMock.expect(mockBeanElement.attributeValue(LAZY_INIT_ATTRIBUTE)).andReturn(lazyInit);
    EasyMock.expect(mockBeanElement.attributeValue(NAME_ATTRIBUTE)).andReturn(name);
    EasyMock.expect(mockBeanElement.attributeValue(SCOPE_ATTRIBUTE)).andReturn(scope);
    return mockBeanElement;
  }

  protected Element createMockConstructorArgumentElement(final String formatPattern,
                                                         final String referenceId,
                                                         final String type,
                                                         final String value)
  {
    final Element mockConstructorArgumentElement = EasyMock.createMock(Element.class);
    EasyMock.expect(mockConstructorArgumentElement.attributeValue(FORMAT_PATTERN_ATTRIBUTE)).andReturn(formatPattern);
    EasyMock.expect(mockConstructorArgumentElement.attributeValue(REFERENCE_ID_ATTRIBUTE)).andReturn(referenceId);
    EasyMock.expect(mockConstructorArgumentElement.attributeValue(TYPE_ATTRIBUTE)).andReturn(type);
    EasyMock.expect(mockConstructorArgumentElement.attributeValue(VALUE_ATTRIBUTE)).andReturn(value);
    EasyMock.replay(mockConstructorArgumentElement);
    return mockConstructorArgumentElement;
  }

  protected Element createMockListenerElement(final String className,
                                              final String scope,
                                              final Element[] constructorArgumentElements,
                                              final Element[] propertyElements,
                                              final TestUtil.Verifier verifier)
  {
    final Element mockListenerElement = EasyMock.createMock(Element.class);
    EasyMock.expect(mockListenerElement.attributeValue(CLASS_ATTRIBUTE)).andReturn(className);
    EasyMock.expect(mockListenerElement.attributeValue(SCOPE_ATTRIBUTE)).andReturn(scope);

    final Iterator mockConstructorArgumentElementIterator = EasyMock.createMock(Iterator.class);
    EasyMock.expect(mockListenerElement.elementIterator(CONSTRUCTOR_ARG_TAG)).andReturn(mockConstructorArgumentElementIterator);
    verifier.add(mockConstructorArgumentElementIterator);

    for (final Element constructorArgumentElement : constructorArgumentElements) {
      EasyMock.expect(mockConstructorArgumentElementIterator.hasNext()).andReturn(true);
      EasyMock.expect(mockConstructorArgumentElementIterator.next()).andReturn(constructorArgumentElement);
    }

    EasyMock.expect(mockConstructorArgumentElementIterator.hasNext()).andReturn(false);
    EasyMock.replay(mockConstructorArgumentElementIterator);

    final Iterator mockPropertyElementIterator = EasyMock.createMock(Iterator.class);
    EasyMock.expect(mockListenerElement.elementIterator(PROPERTY_TAG)).andReturn(mockPropertyElementIterator);
    verifier.add(mockPropertyElementIterator);

    for (final Element propertyElement : propertyElements) {
      EasyMock.expect(mockPropertyElementIterator.hasNext()).andReturn(true);
      EasyMock.expect(mockPropertyElementIterator.next()).andReturn(propertyElement);
    }

    EasyMock.expect(mockPropertyElementIterator.hasNext()).andReturn(false);
    EasyMock.replay(mockPropertyElementIterator);
    EasyMock.replay(mockListenerElement);

    return mockListenerElement;
  }

  protected Element createMockPropertyElement(final String propertyName) {
    return createMockPropertyElement(propertyName, null, null, null);
  }

  protected Element createMockPropertyElement(final String propertyName,
                                              final String formatPattern,
                                              final String referenceId,
                                              final String value)
  {
    final Element mockPropertyElement = EasyMock.createMock(Element.class);
    EasyMock.expect(mockPropertyElement.attributeValue(NAME_ATTRIBUTE)).andReturn(propertyName);
    EasyMock.expectLastCall().atLeastOnce();
    EasyMock.expect(mockPropertyElement.attributeValue(FORMAT_PATTERN_ATTRIBUTE)).andReturn(formatPattern);
    EasyMock.expectLastCall().anyTimes();
    EasyMock.expect(mockPropertyElement.attributeValue(REFERENCE_ID_ATTRIBUTE)).andReturn(referenceId);
    EasyMock.expectLastCall().anyTimes();
    EasyMock.expect(mockPropertyElement.attributeValue(VALUE_ATTRIBUTE)).andReturn(value);
    EasyMock.expectLastCall().anyTimes();
    EasyMock.replay(mockPropertyElement);
    return mockPropertyElement;
  }

  protected XmlBeanDeclarationParser getParser() {
    return parser;
  }

  public void testParse() throws Exception {
    final Map<String, BeanDeclaration> expectedBeanMap = new HashMap<String, BeanDeclaration>(5);

    final BeanDeclaration householdBean = addBeanDeclaration(expectedBeanMap, "householdBean", "com.cp.common.test.mock.MockHousehold");
    householdBean.setInitMethod("init");
    householdBean.setDestroyMethod("destroy");
    householdBean.setScope(Scope.SINGLETON);
    addConstructorArgument(householdBean, null, null, "java.lang.Integer", "1");
    addListenerDeclaration(householdBean, "com.cp.common.beans.event.RequiredFieldVetoableChangeListener", Scope.SINGLETON, "number");
    addPropertyDeclaration(householdBean, "address", null, "addressBean", null);
    addPropertyDeclaration(householdBean, "number", "0123456789", null, null);
    addPropertyDeclaration(householdBean, "phoneNumber", null, "phoneNumberBean", null);

    final BeanDeclaration addressBean = addBeanDeclaration(expectedBeanMap, "addressBean", "com.cp.common.test.mock.MockAddress");
    addressBean.setInitMethod("init");
    addressBean.setScope(Scope.SINGLETON);
    addListenerDeclaration(addressBean, "com.cp.common.beans.event.RequiredFieldVetoableChangeListener", Scope.SINGLETON, "street1", "city", "state", "zip");
    addPropertyDeclaration(addressBean, "street1", "100 Main St.", null, null);
    addPropertyDeclaration(addressBean, "street2", "Apt. 22A", null, null);
    addPropertyDeclaration(addressBean, "city", "Portland", null, null);
    addPropertyDeclaration(addressBean, "county", "Multnomah", null, null);
    addPropertyDeclaration(addressBean, "state", "Oregon", null, null);
    addPropertyDeclaration(addressBean, "zip", "12345", null, null);
    addPropertyDeclaration(addressBean, "zipExt", "4444", null, null);

    final BeanDeclaration phoneNumberBean = addBeanDeclaration(expectedBeanMap, "phoneNumberBean", "com.cp.common.test.mock.MockPhoneNumber");
    phoneNumberBean.setInitMethod("init");
    phoneNumberBean.setScope(Scope.SINGLETON);
    addListenerDeclaration(phoneNumberBean, "com.cp.common.beans.event.RequiredFieldVetoableChangeListener", Scope.SINGLETON, "areaCode", "prefix", "suffix");
    addPropertyDeclaration(phoneNumberBean, "areaCode", "503", null, null);
    addPropertyDeclaration(phoneNumberBean, "prefix", "555", null, null);
    addPropertyDeclaration(phoneNumberBean, "suffix", "1234", null, null);
    addPropertyDeclaration(phoneNumberBean, "ext", "4444", null, null);

    final BeanDeclaration consumerBean = addBeanDeclaration(expectedBeanMap, "consumerBean", "com.cp.common.test.mock.MockConsumer");
    consumerBean.setLazyInit(true);
    consumerBean.setScope(Scope.PROTOTYPE);
    addListenerDeclaration(consumerBean, "com.cp.common.beans.event.RequiredFieldVetoableChangeListener", Scope.SINGLETON, "firstName", "lastName");
    addPropertyDeclaration(consumerBean, "dateOfBirth", "05/27/1974", null, "MM/dd/yyyy");
    addPropertyDeclaration(consumerBean, "firstName", "Jon", null, null);
    addPropertyDeclaration(consumerBean, "household", null, "householdBean", null);
    addPropertyDeclaration(consumerBean, "lastName", "Bloom", null, null);

    Map<String, BeanDeclaration> actualBeanMap = null;

    try {
      actualBeanMap = getParser().parse(new ClassPathResource(MOCK_BEANS_XML_FILE));
    }
    catch (Exception e) {
      fail("Failed to parse the mock beans xml file (" + MOCK_BEANS_XML_FILE + "): " + e.getMessage());
    }

    assertNotNull(actualBeanMap);
    assertEquals(expectedBeanMap, actualBeanMap);
  }

  public void testParseWithNullInputStream() throws Exception {
    Map<String, BeanDeclaration> beanMap = null;

    assertNull(beanMap);

    try {
      beanMap = getParser().parse((InputStream) null);
      fail("Calling parse with a null InputStream should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The input stream to the bean definitions source cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parse with a null InputStream threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(beanMap);
  }

  public void testParseBeanElement() throws Exception {
    final Element mockBeanElement = createMockBeanElement("com.cp.common.test.mock.MockBeanImpl", "destroy", "mockBean",
      "init", "true", "bean, testBean", "singleton");

    // TODO should we verify the mock constructor argument element?
    final Iterator mockConstructorArgumentElementIterator = EasyMock.createMock(Iterator.class);
    EasyMock.expect(mockBeanElement.elementIterator(CONSTRUCTOR_ARG_TAG)).andReturn(mockConstructorArgumentElementIterator);
    EasyMock.expect(mockConstructorArgumentElementIterator.hasNext()).andReturn(true);
    EasyMock.expect(mockConstructorArgumentElementIterator.next()).andReturn(createMockConstructorArgumentElement(null, null, "java.lang.Integer", "1"));
    EasyMock.expect(mockConstructorArgumentElementIterator.hasNext()).andReturn(false);
    EasyMock.replay(mockConstructorArgumentElementIterator);

    // TODO should we verify the mock listener element?
    final Iterator mockListenerElementIterator = EasyMock.createMock(Iterator.class);
    EasyMock.expect(mockBeanElement.elementIterator(LISTENER_TAG)).andReturn(mockListenerElementIterator);
    EasyMock.expect(mockListenerElementIterator.hasNext()).andReturn(true);
    EasyMock.expect(mockListenerElementIterator.next()).andReturn(createMockListenerElement("com.mycompany.mypackage.MyListener",
      "singleton", new Element[0], new Element[0], TestUtil.getNoOpVerifier()));
    EasyMock.expect(mockListenerElementIterator.hasNext()).andReturn(false);
    EasyMock.replay(mockListenerElementIterator);

    // TODO should we verify the mock property element?
    final Iterator mockPropertyElementIterator = EasyMock.createMock(Iterator.class);
    EasyMock.expect(mockBeanElement.elementIterator(PROPERTY_TAG)).andReturn(mockPropertyElementIterator);
    EasyMock.expect(mockPropertyElementIterator.hasNext()).andReturn(true);
    EasyMock.expect(mockPropertyElementIterator.next()).andReturn(createMockPropertyElement("myProperty"));
    EasyMock.expect(mockPropertyElementIterator.hasNext()).andReturn(false);
    EasyMock.replay(mockPropertyElementIterator);
    EasyMock.replay(mockBeanElement);

    final InvocationArgument expectedConstructorArgument = BeanDefinitionsFactory.getInvocationArgument("java.lang.Integer", "1");

    final ListenerDeclaration expectedListenerDeclaration = BeanDefinitionsFactory.getListenerDeclaration("com.mycompany.mypackage.MyListener");
    expectedListenerDeclaration.setScope(Scope.SINGLETON);

    final PropertyDeclaration expectedPropertyDeclaration = BeanDefinitionsFactory.getPropertyDeclaration("myProperty");

    final BeanDeclaration actualBeanDefinition = getParser().parseBeanElement(mockBeanElement);

    EasyMock.verify(mockBeanElement, mockConstructorArgumentElementIterator, mockListenerElementIterator,
      mockPropertyElementIterator);

    assertNotNull(actualBeanDefinition);
    assertEquals("com.cp.common.test.mock.MockBeanImpl", actualBeanDefinition.getClassName());
    assertEquals("destroy", actualBeanDefinition.getDestroyMethod());
    assertEquals("mockBean", actualBeanDefinition.getId());
    assertEquals("init", actualBeanDefinition.getInitMethod());
    assertTrue(actualBeanDefinition.isLazyInit());
    assertEquals("bean, testBean", actualBeanDefinition.getName());
    assertEquals(Scope.SINGLETON, actualBeanDefinition.getScope());
    assertEquals(MockBeanImpl.class, actualBeanDefinition.getType());
    assertFalse(actualBeanDefinition.getInvocationArguments().isEmpty());
    assertEquals(1, actualBeanDefinition.getInvocationArguments().size());
    assertEquals(expectedConstructorArgument, actualBeanDefinition.getInvocationArgument(0));
    assertFalse(actualBeanDefinition.getListeners().isEmpty());
    assertEquals(1, actualBeanDefinition.getListeners().size());
    assertEquals(expectedListenerDeclaration, actualBeanDefinition.getListenerDefinition("com.mycompany.mypackage.MyListener"));
    assertFalse(actualBeanDefinition.getProperties().isEmpty());
    assertEquals(1, actualBeanDefinition.getProperties().size());
    assertEquals(expectedPropertyDeclaration, actualBeanDefinition.getPropertyDefinition("myProperty"));
  }

  public void testParseBeanElementWithNullElement() throws Exception {
    BeanDeclaration beanDeclaration = null;

    assertNull(beanDeclaration);

    try {
      beanDeclaration = getParser().parseBeanElement(null);
      fail("Calling parseBeanElement with a null Element should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean element cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parseBeanElement with a null Element threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(beanDeclaration);
  }

  public void testParseConstructorArgumentElement() throws Exception {
    final Element mockConstructorArgumentElement = createMockConstructorArgumentElement("yyyy.MM.dd", "myBean",
      "java.util.Calendar", "2008.09.01");

    final InvocationArgument actualConstructorArgument = getParser().parseConstructorArgument(mockConstructorArgumentElement);

    assertNotNull(actualConstructorArgument);
    assertEquals("yyyy.MM.dd", actualConstructorArgument.getFormatPattern());
    assertEquals("myBean", actualConstructorArgument.getRefId());
    assertEquals("java.util.Calendar", actualConstructorArgument.getStringType());
    assertEquals("2008.09.01", actualConstructorArgument.getStringValue());
  }

  public void testParseConstructorArgumentElementWithNullElement() throws Exception {
    InvocationArgument constructorArgument = null;

    assertNull(constructorArgument);

    try {
      constructorArgument = getParser().parseConstructorArgument(null);
      fail("Calling parseConstructorArgument with a null Element should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The constructor argument element cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parseConstructorArgument with a null Element threw an unexpected Exception ("
        + e.getMessage() + "!)");
    }

    assertNull(constructorArgument);
  }

  public void testParseListenerElement() throws Exception {
    final TestUtil.Verifier verifier = TestUtil.getEasyMockVerifier();

    final Element[] mockConstructorArguments = {
      createMockConstructorArgumentElement(null, "this", "com.cp.common.beans.Bean", "null")
    };

    final Element[] mockPropertyElements = {
      createMockPropertyElement("firstName"),
      createMockPropertyElement("lastName"),
      createMockPropertyElement("dateOfBirth")
    };

    final Element mockListenerElement = createMockListenerElement("com.cp.common.beans.event.RequiredVetoableChangeListener",
      "singleton", mockConstructorArguments, mockPropertyElements, verifier);

    final InvocationArgument expectedConstructorArgument = BeanDefinitionsFactory.getInvocationArgument("com.cp.common.beans.Bean", "null");
    expectedConstructorArgument.setFormatPattern(null);
    expectedConstructorArgument.setRefId("this");

    final ListenerDeclaration actualListenerDefinition = getParser().parseListenerElement(mockListenerElement);

    EasyMock.verify(mockListenerElement);
    verifier.verify();

    assertNotNull(actualListenerDefinition);
    assertEquals("com.cp.common.beans.event.RequiredVetoableChangeListener", actualListenerDefinition.getClassName());
    assertEquals(Scope.SINGLETON, actualListenerDefinition.getScope());
    assertFalse(actualListenerDefinition.getInvocationArguments().isEmpty());
    assertEquals(1, actualListenerDefinition.getInvocationArguments().size());
    assertEquals(expectedConstructorArgument, actualListenerDefinition.getInvocationArgument(0));
    assertTrue(actualListenerDefinition.contains("firstName"));
    assertTrue(actualListenerDefinition.contains("lastName"));
    assertTrue(actualListenerDefinition.contains("dateOfBirth"));
    assertFalse(actualListenerDefinition.contains("ssn"));
  }

  public void testParseListenerElementWithNullElement() throws Exception {
    ListenerDeclaration listenerDeclaration = null;

    assertNull(listenerDeclaration);

    try {
      listenerDeclaration = getParser().parseListenerElement(null);
      fail("Calling parseListenerElement with a null Element should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The listener element cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parseListenerElement with a nullElement threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(listenerDeclaration);
  }

  public void testParsePropertyElement() throws Exception {
    final Element mockPropertyElement = createMockPropertyElement("dateOfBirth", "MM/dd/yyyy hh:mm:ss A", "null",
      "01/03/3008 12:13:30 AM");

    final PropertyDeclaration actualPropertyDeclaration = getParser().parsePropertyElement(mockPropertyElement);

    EasyMock.verify(mockPropertyElement);

    assertNotNull(actualPropertyDeclaration);
    assertEquals(String.class.getName(), actualPropertyDeclaration.getClassName());
    assertEquals("MM/dd/yyyy hh:mm:ss A", actualPropertyDeclaration.getFormatPattern());
    assertEquals("dateOfBirth", actualPropertyDeclaration.getName());
    assertEquals("null", actualPropertyDeclaration.getRefId());
    assertEquals("01/03/3008 12:13:30 AM", actualPropertyDeclaration.getValue());
  }

  public void testParsePropertyElementWithNullElement() throws Exception {
    PropertyDeclaration propertyDeclaration = null;

    try {
      propertyDeclaration = getParser().parsePropertyElement(null);
      fail("Calling parsePropertyElement with a null Element should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The property element cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling parsePropertyElement with a null Element threw an unexpected Exception (" + e.getMessage() + ")!");
    }

    assertNull(propertyDeclaration);
  }

  public void testValidateConstructorArgumentReferenceIds() throws Exception {
    final Map<String, BeanDeclaration> beanMap = new HashMap<String, BeanDeclaration>(2);
    addBeanDeclaration(beanMap, "household", "com.cp.common.test.mock.MockHousehold");

    final BeanDeclaration personBean = addBeanDeclaration(beanMap, "person", "com.cp.common.test.mock.MockPerson");
    addConstructorArgument(personBean, null, "household", "com.cp.common.test.mock.MockHousehold", "null");

    try {
      getParser().validateConstructorArgumentReferenceIds(beanMap);
    }
    catch (Exception e) {
      fail("Calling validateConstructorArgumentReferenceIds threw an unexpected Exception (" + e.getMessage() + ")!");
    }
  }

  public void testValidateConstructorArgumentReferenceIdsWithInvalidBeanReference() throws Exception {
    final Map<String, BeanDeclaration> beanMap = new HashMap<String, BeanDeclaration>(1);

    final BeanDeclaration personBean = addBeanDeclaration(beanMap, "person", "com.cp.common.test.mock.MockPerson");
    addConstructorArgument(personBean, null, "household", "com.cp.common.test.mock.MockHousehold", "null");

    try {
      getParser().validateConstructorArgumentReferenceIds(beanMap);
      fail("Calling validateConstructorArgumentReferenceIds with a bean map having an invalid bean reference should have thrown a ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("The bean reference id (household) for constructor argument (1) of bean declaration (person) is not a valid bean id in the beans configuration!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling validateConstructorArgumentReferenceIds with a bean map having an invalid bean reference threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testValidateConstructorArgumentReferenceIdsWithNullBeanMap() throws Exception {
    try {
      getParser().validateConstructorArgumentReferenceIds(null);
      fail("Calling validateConstructorArgumentReferenceIds with a null bean map should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean map for validating constructor argument bean references cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling validateConstructorArgumentReferenceIds with a null bean map threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testValidatePropertyReferenceIds() throws Exception {
    final Map<String, BeanDeclaration> beanMap = new HashMap<String, BeanDeclaration>(3);

    final BeanDeclaration householdBean = addBeanDeclaration(beanMap, "household", "com.cp.common.test.mock.MockHousehold");
    addPropertyDeclaration(householdBean, "address", null, "address", null);
    addPropertyDeclaration(householdBean, "number", "0123456789", null, null);
    addPropertyDeclaration(householdBean, "phoneNumber", null, "phoneNumber", null);

    final BeanDeclaration addressBean = addBeanDeclaration(beanMap, "address", "com.cp.common.test.mock.MockAddress");
    addPropertyDeclaration(addressBean, "literalAddress", "100 Main St. Portland, OR 97205", null, null);

    final BeanDeclaration phoneNumberBean = addBeanDeclaration(beanMap, "phoneNumber", "com.cp.common.test.mock.MockPhoneNumber");
    addPropertyDeclaration(phoneNumberBean, "literalPhoneNumber", "503-555-1234", null, null);

    final BeanDeclaration consumerBean = addBeanDeclaration(beanMap, "consumer", "com.cp.common.test.mock.MockConsumer");
    addPropertyDeclaration(consumerBean, "firstName", "Jon", null, null);
    addPropertyDeclaration(consumerBean, "household", null, "household", null);
    addPropertyDeclaration(consumerBean, "lastName", "Bloom", null, null);

    try {
      getParser().validatePropertyReferenceIds(beanMap);
    }
    catch (Exception e) {
      fail("Calling validatePropertyReferenceIds with the bean map having a the household, address, phoneNumber and consumer beans with valid bean references threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testValidatePropertyReferenceIdsWithInvalidBeanReferences() throws Exception {
    final Map<String, BeanDeclaration> beanMap = new HashMap<String, BeanDeclaration>(1);

    final BeanDeclaration householdBean = addBeanDeclaration(beanMap, "household", "com.cp.common.test.mock.MockHousehold");
    addPropertyDeclaration(householdBean, "address", null, "address", null);
    addPropertyDeclaration(householdBean, "number", "0123456789", null, null);
    addPropertyDeclaration(householdBean, "phoneNumber", null, "phoneNumber", null);

    try {
      getParser().validatePropertyReferenceIds(beanMap);
      fail("Calling validatePropertyReferenceIds on a bean map with invalid bean references should have thrown a ConfigurationException!");
    }
    catch (ConfigurationException e) {
      assertEquals("The bean reference id (address) for property (address) on bean (household) is not a valid bean id in the beans configuration!",
        e.getMessage());
    }
    catch (Exception e) {
      fail("Calling validatePropertyReferenceIds on a bean map with invalid bean references threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  public void testValidatePropertyReferenceIdsWithNullBeanMap() throws Exception {
    try {
      getParser().validatePropertyReferenceIds(null);
      fail("Calling validatePropertyReferenceIds with a null bean map should have thrown a NullPointerException!");
    }
    catch (NullPointerException e) {
      assertEquals("The bean map for validating property bean references cannot be null!", e.getMessage());
    }
    catch (Exception e) {
      fail("Calling validatePropertyReferenceIds with a null bean map threw an unexpected Exception ("
        + e.getMessage() + ")!");
    }
  }

  private static final class DefaultXmlBeanDeclarationParserTestSetup extends TestSetup {

    public DefaultXmlBeanDeclarationParserTestSetup(final Test test) {
      super(test);
    }

    @Override
    protected void setUp() throws Exception {
      super.setUp();
      parser = new XmlBeanDeclarationParser();
    }

    @Override
    protected void tearDown() throws Exception {
      super.tearDown();
      parser = null;
    }
  }

}
