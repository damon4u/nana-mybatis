package com.maybe.plugin.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 * resultMap属性值
 *
 * @author damon4u
 * @version 2018-10-18 17:56
 */
public interface ResultMapAttributeDomElement extends DomElement {
    
    @NotNull
    @Attribute("resultMap")
    // TODO: converter
    GenericAttributeValue<ResultMap> getResultMap();
}
