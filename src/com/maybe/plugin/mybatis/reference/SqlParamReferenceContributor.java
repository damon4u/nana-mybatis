package com.maybe.plugin.mybatis.reference;

import com.maybe.plugin.mybatis.util.MapperUtils;
import com.google.common.collect.Lists;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.xml.XmlToken;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-23 18:10
 */
public class SqlParamReferenceContributor extends PsiReferenceContributor {
    
    private static final Pattern PARAM_PATTERN = Pattern.compile("#\\{(.*?)}");

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(XmlToken.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        XmlToken token = (XmlToken) element;
                        if (MapperUtils.isElementWithinMybatisFile(token)) {
                            String text = token.getText();
                            Matcher matcher = PARAM_PATTERN.matcher(text);
                            ArrayList<PsiReference> referenceList = Lists.newArrayList();
                            while (matcher.find()) {
                                String param = matcher.group(1);
                                int start = matcher.start(1);
                                int end = matcher.end(1);
                                referenceList.add(new ParamReference(token, new TextRange(start, end), param));
                            }
                            return referenceList.toArray(new PsiReference[0]);
                        }
                        
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}
