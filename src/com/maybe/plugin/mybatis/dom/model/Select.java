package com.maybe.plugin.mybatis.dom.model;

import com.maybe.plugin.mybatis.dom.converter.AliasConverter;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 * 查询语句
 *
 * @author damon4u
 * @version 2018-10-18 17:38
 */
public interface Select extends DMLAndDQLDynamicQueryableDomElement, ResultMapAttributeDomElement {
    
    @NotNull
    @Attribute("resultType")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getResultType();
}
