package com.maybe.plugin.mybatis.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-19 15:53
 */
public class ResultPropertyReferenceSet extends ReferenceSetBase<PsiReference> {

    public ResultPropertyReferenceSet(String text, @NotNull PsiElement element, int offset) {
        super(text, element, offset, ReferenceSetBase.DOT_SEPARATOR);
    }

    @Override
    protected PsiReference createReference(TextRange range, int index) {
        XmlAttributeValue element = (XmlAttributeValue) getElement();
        return null == element ? null : new ContextPsiFieldReference(element, range, index);
    }
}
