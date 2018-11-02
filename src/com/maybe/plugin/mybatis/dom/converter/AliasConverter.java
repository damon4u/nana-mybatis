package com.maybe.plugin.mybatis.dom.converter;

import com.maybe.plugin.mybatis.alias.AliasClassReference;
import com.maybe.plugin.mybatis.alias.AliasService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.CustomReferenceConverter;
import com.intellij.util.xml.DomJavaUtil;
import com.intellij.util.xml.GenericDomValue;
import com.intellij.util.xml.PsiClassConverter;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-11-01 21:31
 */
public class AliasConverter extends ConverterAdaptor<PsiClass> implements CustomReferenceConverter<PsiClass> {
    
    private PsiClassConverter delegate = new PsiClassConverter();

    @Nullable
    @Override
    public PsiClass fromString(@Nullable String s, ConvertContext context) {
        if (StringUtil.isEmptyOrSpaces(s)) {
            return null;
        }
        Project project = context.getProject();
        if (!s.contains(String.valueOf(ReferenceSetBase.DOT_SEPARATOR))) {
            return AliasService.getInstance(project).findPsiClass(context.getXmlElement(), s).orElse(null);
        }
        return DomJavaUtil.findClass(s.trim(), context.getFile(), context.getModule(), GlobalSearchScope.allScope(project));
    }

    @Nullable
    @Override
    public String toString(@Nullable PsiClass psiClass, ConvertContext context) {
        return delegate.toString(psiClass, context);
    }

    @NotNull
    @Override
    public PsiReference[] createReferences(GenericDomValue<PsiClass> value, PsiElement element, ConvertContext context) {
        XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) element;
        String aliasValue = (xmlAttributeValue).getValue();
        if (StringUtils.isBlank(aliasValue)) {
            return new PsiReference[0];
        }
        if (aliasValue.contains(String.valueOf(ReferenceSetBase.DOT_SEPARATOR))) {
            return delegate.createReferences(value, element, context);
        }
        return new PsiReference[]{new AliasClassReference(xmlAttributeValue)};
    }
    
}
