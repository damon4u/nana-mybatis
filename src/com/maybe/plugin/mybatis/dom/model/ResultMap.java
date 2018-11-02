package com.maybe.plugin.mybatis.dom.model;

import com.maybe.plugin.mybatis.dom.converter.AliasConverter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 * resultMap标签
 *
 * @author damon4u
 * @version 2018-10-18 18:19
 */
public interface ResultMap extends ResultMapBaseDomElement, IdDomElement {
    
    @NotNull
    @Attribute("extends")
    // TODO: converter
    GenericAttributeValue<XmlAttributeValue> getExtends();
    
    @NotNull
    @Attribute("type")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getType();
}
