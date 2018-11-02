package com.maybe.plugin.mybatis.dom.model;

import com.maybe.plugin.mybatis.dom.converter.PropertyConverter;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * Description:
 * 包含property属性的元素
 *
 * @author damon4u
 * @version 2018-10-18 17:52
 */
public interface PropertyAttributeDomElement extends DomElement {
    
    @Attribute("property")
    @Convert(PropertyConverter.class)
    GenericAttributeValue<XmlAttributeValue> getProperty();
}
