package com.maybe.plugin.mybatis.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * Description:
 * resultMap中的constructor标签
 *
 * @author damon4u
 * @version 2018-10-18 17:48
 */
public interface Constructor extends DomElement {

    @SubTagList("arg")
    List<Arg> getArgs();

    @SubTagList("idArg")
    List<IdArg> getIdArgs();
}
