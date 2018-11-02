package com.maybe.plugin.mybatis.alias;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttributeValue;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-11-01 21:38
 */
public class AliasClassReference extends PsiReferenceBase<XmlAttributeValue> {
    
    private Function<Alias, String> FUN = new Function<Alias, String>() {
        @Override
        public String apply(Alias alias) {
            return alias.getAliasValue();
        }
    };

    public AliasClassReference(@NotNull XmlAttributeValue element) {
        super(element, true);
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        XmlAttributeValue attributeValue = getElement();
        String value = attributeValue.getValue();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return AliasService.getInstance(attributeValue.getProject()).findPsiClass(attributeValue, value).orElse(null);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        AliasService aliasService = AliasService.getInstance(getElement().getProject());
        Collection<String> result = Collections2.transform(aliasService.getAliasCollection(getElement()), FUN);
        if (CollectionUtils.isEmpty(result)) {
            return new String[0];
        }
        return result.toArray(new String[0]);
    }
}
