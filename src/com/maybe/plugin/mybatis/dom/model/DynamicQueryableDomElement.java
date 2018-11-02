package com.maybe.plugin.mybatis.dom.model;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Description:
 * mapper中的动态查询语句
 * http://www.mybatis.org/mybatis-3/dynamic-sql.html
 *
 * @author damon4u
 * @version 2018-10-18 16:55
 */
public interface DynamicQueryableDomElement extends DomElement {
    
    @NotNull
    @SubTagList("include")
    List<Include> getIncludes();
    
    @NotNull
    @SubTagList("trim")
    List<Trim> getTrims();
    
    @NotNull
    @SubTagList("where")
    List<Where> getWheres();
    
    @NotNull
    @SubTagList("set")
    List<Set> getSets();
    
    @NotNull
    @SubTagList("foreach")
    List<Foreach> getForeachs();
    
    @NotNull
    @SubTagList("choose")
    List<Choose> getChooses();
    
    @NotNull
    @SubTagList("if")
    List<If> getIfs();
    
    @SubTagList("bind")
    List<Bind> getBinds();
}
