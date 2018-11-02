package com.maybe.plugin.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;

/**
 * Description:
 * 动态查询语句中的include子tag
 *
 * @author damon4u
 * @version 2018-10-18 16:58
 */
public interface Include extends DomElement {

    /**
     * include引用标签id
     * @return 引用标签
     */
    @Attribute("refid")
    // TODO: converter
    GenericAttributeValue<Sql> getRefId();
}
