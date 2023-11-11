/*
 * XmlBeanDeclarationParser.java (c) 24 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.9.3
 * @see com.cp.common.beans.definition.BeanDeclaration
 * @see com.cp.common.beans.definition.BeanDefinitionsFactory
 * @see com.cp.common.beans.definition.ListenerDeclaration
 * @see com.cp.common.beans.definition.PropertyDelcaration
 * @see com.cp.common.beans.definition.Scope
 * @see com.cp.common.beans.definition.support.AbstractBeanDefinitionParser
 */

package com.cp.common.beans.definition.support;

import com.cp.common.beans.definition.BeanDeclaration;
import com.cp.common.beans.definition.BeanDefinition;
import com.cp.common.beans.definition.BeanDefinitionsFactory;
import com.cp.common.beans.definition.InvocationArgument;
import com.cp.common.beans.definition.ListenerDeclaration;
import com.cp.common.beans.definition.PropertyDeclaration;
import com.cp.common.beans.definition.Scope;
import com.cp.common.lang.Assert;
import com.cp.common.lang.ObjectUtil;
import com.cp.common.lang.StringUtil;
import com.cp.common.lang.Visitable;
import com.cp.common.util.AbstractVisitableCollection;
import com.cp.common.util.ConfigurationException;
import com.cp.common.util.ParsingException;
import com.cp.common.util.VisitableCollection;
import com.cp.common.util.Visitor;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlBeanDeclarationParser extends AbstractBeanDefinitionParser<BeanDeclaration> {

  protected static final Scope DEFAULT_BEAN_SCOPE = Scope.PROTOTYPE;
  protected static final Scope DEFAULT_LISTENER_SCOPE = Scope.PROTOTYPE;

  // tags
  protected static final String BEAN_TAG = "bean";
  protected static final String CONSTRUCTOR_ARG_TAG = "constructor-arg";
  protected static final String LISTENER_TAG = "listener";
  protected static final String PROPERTY_TAG = "property";

  // tag attributes
  protected static final String CLASS_ATTRIBUTE = "class";
  protected static final String DESTROY_METHOD_ATTRIBUTE = "destroy-method";
  protected static final String FORMAT_PATTERN_ATTRIBUTE = "format-pattern";
  protected static final String ID_ATTRIBUTE = "id";
  protected static final String INIT_METHOD_ATTRIBUTE = "init-method";
  protected static final String LAZY_INIT_ATTRIBUTE = "lazy-init";
  protected static final String NAME_ATTRIBUTE = "name";
  protected static final String REFERENCE_ID_ATTRIBUTE = "refid";
  protected static final String SCOPE_ATTRIBUTE = "scope";
  protected static final String TYPE_ATTRIBUTE = "type";
  protected static final String VALUE_ATTRIBUTE = "value";

  /**
   * Parses the specified bean definitions input stream and creates a mapping of bean IDs to BeanDefinition objects.
   * @param beanDefinitionsInputStream an InputStream as the source of the bean definitions to parse.
   * @return a mapping of bean IDs to BeanDefinition objects specified in the bean definition input stream.
   */
  @Override
  protected Map<String, BeanDeclaration> parse(final InputStream beanDefinitionsInputStream) {
    Assert.notNull(beanDefinitionsInputStream, "The input stream to the bean definitions source cannot be null!");

    try {
      final Map<String, BeanDeclaration> beanMap = new TreeMap<String, BeanDeclaration>();

      // TODO in the future, create abstraction for the usage of the dom4j API & Framework
      final SAXReader reader = new SAXReader();
      final Document beanDefinitionsDocument = reader.read(beanDefinitionsInputStream) ;

      for (Iterator it = beanDefinitionsDocument.getRootElement().elementIterator(BEAN_TAG); it.hasNext(); ) {
        final BeanDeclaration beanDefinition = parseBeanElement((Element) it.next());
        beanMap.put(beanDefinition.getId(), beanDefinition);
      }

      validate(beanMap);

      return beanMap;
    }
    catch (Exception e) {
      logger.error("Failed to parse bean definitions from the input stream!", e);
      throw new ParsingException("Failed to parse bean definitions from the input stream!", e);
    }
  }

  /**
   * Parses a bean tag element in the bean definitions XML file.
   * @param beanElement the bean definition specified as a bean tag in the XML file.
   * @return a BeanDefinition object from the parsed bean tag in the XML file.
   */
  protected BeanDeclaration parseBeanElement(final Element beanElement) {
    Assert.notNull(beanElement, "The bean element cannot be null!");

    final String beanClass = beanElement.attributeValue(CLASS_ATTRIBUTE);
    final String beanDestroyMethod = beanElement.attributeValue(DESTROY_METHOD_ATTRIBUTE);
    final String beanId = beanElement.attributeValue(ID_ATTRIBUTE);
    final String beanInitMethod = beanElement.attributeValue(INIT_METHOD_ATTRIBUTE);
    final String beanLazyInit = beanElement.attributeValue(LAZY_INIT_ATTRIBUTE);
    final String beanName = beanElement.attributeValue(NAME_ATTRIBUTE);
    final String beanScope = beanElement.attributeValue(SCOPE_ATTRIBUTE);

    final BeanDeclaration beanDeclaration = BeanDefinitionsFactory.getBeanDeclaration(beanId, beanClass);

    beanDeclaration.setDestroyMethod(beanDestroyMethod);
    beanDeclaration.setInitMethod(beanInitMethod);
    beanDeclaration.setLazyInit(Boolean.valueOf(beanLazyInit));
    beanDeclaration.setName(beanName);
    beanDeclaration.setScope(ObjectUtil.getDefaultValue(Scope.getScopeByDescription(beanScope), DEFAULT_BEAN_SCOPE));

    for (final Iterator it = beanElement.elementIterator(CONSTRUCTOR_ARG_TAG); it.hasNext(); ) {
      beanDeclaration.add(parseConstructorArgument((Element) it.next()));
    }

    for (final Iterator it = beanElement.elementIterator(LISTENER_TAG); it.hasNext(); ) {
      beanDeclaration.add(parseListenerElement((Element) it.next()));
    }

    for (final Iterator it = beanElement.elementIterator(PROPERTY_TAG); it.hasNext(); ) {
      beanDeclaration.add(parsePropertyElement((Element) it.next()));
    }

    return beanDeclaration;
  }

  /**
   * Parses a constructor-arg tag element inside a bean tag element in the bean definitions XML file.
   * @param constructorArgumentElement the constructor argument definition specified as a constructor-arg tag
   * in the XML file.
   * @return an InvocationArgument object form the parsed constructor-arg tag in the XML file.
   */
  protected InvocationArgument parseConstructorArgument(final Element constructorArgumentElement) {
    Assert.notNull(constructorArgumentElement, "The constructor argument element cannot be null!");

    final String constructorArgumentFormatPattern = constructorArgumentElement.attributeValue(FORMAT_PATTERN_ATTRIBUTE);
    final String constructorArgumentReferenceId = constructorArgumentElement.attributeValue(REFERENCE_ID_ATTRIBUTE);
    final String constructorArgumentType = constructorArgumentElement.attributeValue(TYPE_ATTRIBUTE);
    final String constructorArgumentValue = constructorArgumentElement.attributeValue(VALUE_ATTRIBUTE);

    final InvocationArgument constructorArgument = BeanDefinitionsFactory.getInvocationArgument(
      constructorArgumentType, constructorArgumentValue);

    constructorArgument.setFormatPattern(constructorArgumentFormatPattern);
    constructorArgument.setRefId(constructorArgumentReferenceId);

    return constructorArgument;
  }

  /**
   * Parses a listener tag element inside a bean tag element in the bean definitions XML file.
   * @param listenerElement the listener definition specified as a listener tag in the XML file.
   * @return a ListenerDefintion object from the parsed listener tag in the XML file.
   */
  protected ListenerDeclaration parseListenerElement(final Element listenerElement) {
    Assert.notNull(listenerElement, "The listener element cannot be null!");

    final String listenerClass = listenerElement.attributeValue(CLASS_ATTRIBUTE);
    final String listenerScope = listenerElement.attributeValue(SCOPE_ATTRIBUTE);

    final ListenerDeclaration listenerDeclaration = BeanDefinitionsFactory.getListenerDeclaration(listenerClass);

    listenerDeclaration.setScope(ObjectUtil.getDefaultValue(Scope.getScopeByDescription(listenerScope), DEFAULT_LISTENER_SCOPE));

    for (final Iterator it = listenerElement.elementIterator(CONSTRUCTOR_ARG_TAG); it.hasNext(); ) {
      listenerDeclaration.add(parseConstructorArgument((Element) it.next()));
    }

    for (final Iterator it = listenerElement.elementIterator(PROPERTY_TAG); it.hasNext(); ) {
      final Element propertyElement = (Element) it.next();
      listenerDeclaration.add(propertyElement.attributeValue(NAME_ATTRIBUTE));
    }

    return listenerDeclaration;
  }

  /**
   * Parses a property tag element inside a bean tag element in the bean definitions XML file.
   * @param propertyElement the property declaration specified as a property tag in the XML file.
   * @return a PropertyDelcaration object from the parsed property tag in the XML file.
   */
  protected PropertyDeclaration parsePropertyElement(final Element propertyElement) {
    Assert.notNull(propertyElement, "The property element cannot be null!");

    final String propertyFormatPattern = propertyElement.attributeValue(FORMAT_PATTERN_ATTRIBUTE);
    final String propertyName = propertyElement.attributeValue(NAME_ATTRIBUTE);
    final String propertyRefId = propertyElement.attributeValue(REFERENCE_ID_ATTRIBUTE);
    final String propertyValue = propertyElement.attributeValue(VALUE_ATTRIBUTE);

    final PropertyDeclaration propertyDeclaration = BeanDefinitionsFactory.getPropertyDeclaration(propertyName);

    propertyDeclaration.setFormatPattern(propertyFormatPattern);
    propertyDeclaration.setRefId(propertyRefId);
    propertyDeclaration.setValue(propertyValue);

    return propertyDeclaration;
  }

  /**
   * Validates the bean mapping of bean ids to BeanDefinition objects.
   * @param beanMap the Map object containing the mapping of bean ids to BeanDefinitions.
   */
  protected void validate(final Map<String, BeanDeclaration> beanMap) {
    validateConstructorArgumentReferenceIds(beanMap);
    validatePropertyReferenceIds(beanMap);
  }

  /**
   * Validates the bean definition/declaration constructor argument reference ids to other beans defined/declared
   * in the bean configuration file.
   * @param beanMap the Map object containing the mapping of bean ids to BeanDefinitions.
   */
  protected void validateConstructorArgumentReferenceIds(final Map<String, BeanDeclaration> beanMap) {
    Assert.notNull(beanMap, "The bean map for validating constructor argument bean references cannot be null!");

    for (final BeanDeclaration beanDeclaration : beanMap.values()) {
      int index = 0;

      for (final InvocationArgument constructorArgument : beanDeclaration.getInvocationArguments()) {
        final String beanReferenceId = constructorArgument.getRefId();

        index++;

        if (StringUtil.isNotEmpty(beanReferenceId) && !beanMap.containsKey(beanReferenceId)) {
          throw new ConfigurationException("The bean reference id (" + beanReferenceId + ") for constructor argument ("
            + index + ") of bean declaration (" + beanDeclaration.getId()
            + ") is not a valid bean id in the beans configuration!");
        }
      }
    }
  }

  /**
   * Validates the bean definition/declaration property reference ids to other beans defined/declared
   * in the bean configuration file.
   * @param beanMap the Map object containing the mapping of bean ids to BeanDefinitions.
   */
  protected void validatePropertyReferenceIds(final Map<String, BeanDeclaration> beanMap) {
    Assert.notNull(beanMap, "The bean map for validating property bean references cannot be null!");

    final Collection<BeanDeclaration> beanDefinitionCollection = beanMap.values();

    final VisitableCollection<BeanDeclaration> visitableCollection = AbstractVisitableCollection.getVisitableList(
      beanDefinitionCollection.toArray(new BeanDeclaration[beanDefinitionCollection.size()]));

    final ValidateReferenceIdVisitor visitor = new ValidateReferenceIdVisitor(beanMap.keySet());

    visitableCollection.accept(visitor);
  }

  /**
   * Visitor class used to walk the beans definition objects to validate the integrity of the bean
   * definitions/declarations as well as the references within.
   */
  protected static final class ValidateReferenceIdVisitor implements Visitor {

    private final Set<String> validBeanIdSet;

    public ValidateReferenceIdVisitor(final Set<String> beanIdSet) {
      Assert.notEmpty(beanIdSet, "The bean id set cannot be null or empty!");
      this.validBeanIdSet = beanIdSet;
    }

    private String getBeanName(final BeanDefinition beanDefinition) {
      return (ObjectUtil.isNull(beanDefinition) ? null : beanDefinition.getId());
    }

    public void visit(final Visitable visitableObject) {
      if (visitableObject instanceof PropertyDeclaration) {
        final PropertyDeclaration propertyDeclaration = (PropertyDeclaration) visitableObject;
        final String refId = propertyDeclaration.getRefId();

        if (ObjectUtil.isNotNull(refId) && !validBeanIdSet.contains(refId)) {
          throw new ConfigurationException("The bean reference id (" + refId + ") for property ("
            + propertyDeclaration.getName() + ") on bean (" + getBeanName(propertyDeclaration.getBeanDefinition())
            + ") is not a valid bean id in the beans configuration!");
        }
      }
    }

  }

}
