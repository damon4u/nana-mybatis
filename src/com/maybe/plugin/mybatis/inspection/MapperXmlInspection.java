package com.maybe.plugin.mybatis.inspection;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.highlighting.BasicDomElementsInspection;
import com.intellij.util.xml.highlighting.DomElementAnnotationHolder;
import com.intellij.util.xml.highlighting.DomHighlightingHelper;

/**
 * Description:
 * 
 * 检查mapper文件中的语句有没有对应的dao层方法 
 * 继承自BasicDomElementsInspection，依赖于DOM解析过程中的规则，
 * 需要为ParameteredDynamicQueryableDomElement中的id添加DaoMethodConverter，绑定对应关系
 *
 * @author damon4u
 * @version 2018-10-25 21:49
 */
public class MapperXmlInspection extends BasicDomElementsInspection<DomElement> {

    public MapperXmlInspection() {
        super(DomElement.class);
    }

    @Override
    protected void checkDomElement(DomElement element, DomElementAnnotationHolder holder, DomHighlightingHelper helper) {
        super.checkDomElement(element, holder, helper);
    }

}
