package com.maybe.plugin.mybatis.dom.model;

import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * Description:
 * 插入语句
 *
 * @author damon4u
 * @version 2018-10-18 17:32
 */
public interface Insert extends DMLAndDQLDynamicQueryableDomElement {
    
    @SubTagList("selectKey")
    List<SelectKey> getSelectKey();
}
