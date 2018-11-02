package com.maybe.plugin.mybatis.dom.model;

import com.maybe.plugin.mybatis.dom.converter.AliasConverter;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 * 插入语句中的selectkey标签，用来生成主键
 *
 * @author damon4u
 * @version 2018-10-18 17:32
 */
public interface SelectKey extends DomElement {
    
    @NotNull
    @Attribute("resultType")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getResultType();
}
