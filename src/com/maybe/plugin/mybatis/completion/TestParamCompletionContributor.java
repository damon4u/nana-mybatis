package com.maybe.plugin.mybatis.completion;

import com.maybe.plugin.mybatis.util.MapperUtils;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-24 20:01
 */
public class TestParamCompletionContributor extends BaseParamCompletionContributor {

    public TestParamCompletionContributor() {
        extend(CompletionType.BASIC,
                XmlPatterns.psiElement().inside(XmlPatterns.xmlAttribute().withName("test")),
                new CompletionProvider<CompletionParameters>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                  ProcessingContext context,
                                                  @NotNull CompletionResultSet result) {
                        final PsiElement position = parameters.getPosition();
                        addCompletionForPsiParameter(position.getProject(), result, MapperUtils.findParentIdDomElement(position).orElse(null));
                    }
                });
    }

}
