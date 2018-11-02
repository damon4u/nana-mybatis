package com.maybe.plugin.mybatis.dom.model;

import com.maybe.plugin.mybatis.dom.converter.AliasConverter;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-18 17:54
 */
public interface Collection extends ResultMapBaseDomElement, ResultMapAttributeDomElement, PropertyAttributeDomElement {
    
    @NotNull
    @Attribute("ofType")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getOfType();
}
