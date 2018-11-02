package com.maybe.plugin.mybatis.dom.model;

import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.NameValue;
import com.intellij.util.xml.Required;

/**
 * Description:
 * 定义一个基本dom元素，包含id属性
 *
 * @author damon4u
 * @version 2018-10-18 15:38
 */
public interface IdDomElement extends DomElement {

    /**
     * 必须包含一个id属性
     * Required注解标示必须要有该属性
     * NameValue注解该方法，那么外部获取IdDomElement的标示时，返回该方法的结果
     * Attribute注解标示xml属性
     * @return 返回GenericAttributeValue而不是String可以方便重命名
     */
    @Required
    @NameValue
    @Attribute("id")
    GenericAttributeValue<String> getId();
}
