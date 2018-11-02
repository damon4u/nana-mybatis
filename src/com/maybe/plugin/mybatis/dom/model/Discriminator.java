package com.maybe.plugin.mybatis.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.Required;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-18 18:14
 */
public interface Discriminator extends DomElement {
    
    @Required
    @SubTagList("case")
    List<Case> getCases();
}
