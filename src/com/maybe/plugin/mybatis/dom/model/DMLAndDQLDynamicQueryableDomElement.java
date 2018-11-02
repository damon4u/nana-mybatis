package com.maybe.plugin.mybatis.dom.model;

import com.maybe.plugin.mybatis.dom.converter.AliasConverter;
import com.maybe.plugin.mybatis.dom.converter.DaoMethodConverter;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 * 增删改查语句
 *
 * @author damon4u
 * @version 2018-10-18 17:19
 */
public interface DMLAndDQLDynamicQueryableDomElement extends DynamicQueryableDomElement, IdDomElement {

    /**
     * 重写id属性，提供转换器，增删改查会实现这个接口，用来对应Dao层方法
     */
    @Attribute("id")
    @Convert(DaoMethodConverter.class)
    GenericAttributeValue<String> getId();
    
    @NotNull
    @Attribute("parameterType")
    @Convert(AliasConverter.class)
    GenericAttributeValue<PsiClass> getParameterType();
}
