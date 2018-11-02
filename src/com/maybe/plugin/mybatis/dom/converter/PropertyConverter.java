package com.maybe.plugin.mybatis.dom.converter;

import com.maybe.plugin.mybatis.reference.ResultPropertyReferenceSet;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.CustomReferenceConverter;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.GenericAttributeValue;
import com.intellij.util.xml.GenericDomValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-19 15:56
 */
public class PropertyConverter extends ConverterAdaptor<XmlAttributeValue> implements CustomReferenceConverter<XmlAttributeValue> {

    /**
     * converter实现CustomReferenceConverter，这样能通过实现createReferences创建引用关系
     * @param value
     * @param element
     * @param context
     * @return
     */
    @NotNull
    @Override
    public PsiReference[] createReferences(GenericDomValue<XmlAttributeValue> value, PsiElement element, ConvertContext context) {
        String stringValue = value.getStringValue();
        if (stringValue == null) {
            return PsiReference.EMPTY_ARRAY;
        }
        return new ResultPropertyReferenceSet(stringValue, element, ElementManipulators.getOffsetInElement(element)).getPsiReferences();
    }

    @Nullable
    @Override
    public XmlAttributeValue fromString(@Nullable String s, ConvertContext context) {
        DomElement ctxElement = context.getInvocationElement();
        return ctxElement instanceof GenericAttributeValue ? ((GenericAttributeValue) ctxElement).getXmlAttributeValue() : null;
    }

}
