package com.maybe.plugin.mybatis.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * Description:
 * mapper中的resultMap标签内元素基本属性
 *
 * @author damon4u
 * @version 2018-10-18 17:47
 */
public interface ResultMapBaseDomElement extends DomElement {
    
    @SubTag("constructor")
    Constructor getConstructor();
    
    @SubTagList("id")
    List<Id> getIds();
    
    @SubTagList("result")
    List<Result> getResults();
    
    @SubTagList("association")
    List<Association> getAssociations();
    
    @SubTagList("collection")
    List<Collection> getCollections();
    
    @SubTag("discriminator")
    Discriminator getDiscriminator();
    
}
