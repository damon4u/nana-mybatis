package com.maybe.plugin.mybatis.completion;

import com.maybe.plugin.mybatis.dom.model.IdDomElement;
import com.maybe.plugin.mybatis.util.DomUtils;
import com.maybe.plugin.mybatis.util.MapperUtils;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-24 20:24
 */
public class SqlParamCompletionContributor extends BaseParamCompletionContributor {


    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        if (parameters.getCompletionType() != CompletionType.BASIC) {
            return;
        }
        final PsiElement position = parameters.getPosition();
        PsiFile topLevelFile = InjectedLanguageManager.getInstance(parameters.getPosition().getProject()).getTopLevelFile(position);
        if (DomUtils.isMybatisFile(topLevelFile)) {
            if (shouldAddElement(position.getContainingFile(), parameters.getOffset())) {
                process(topLevelFile, result, position);
            }    
        }
        
    }

    private boolean shouldAddElement(PsiFile file, int offset) {
        String text = file.getText();
        for (int i = offset - 1; i > 0; i--) {
            char c = text.charAt(i);
            if (c == '{' && text.charAt(i - 1) == '#') {
                return true;
            }
        }
        return false;
    }

    private void process(PsiFile xmlFile, CompletionResultSet result, PsiElement position) {
        int offset = InjectedLanguageManager.getInstance(position.getProject()).injectedToHost(position, position.getTextOffset());
        Optional<IdDomElement> idDomElement = MapperUtils.findParentIdDomElement(xmlFile.findElementAt(offset));
        if (idDomElement.isPresent()) {
            addCompletionForPsiParameter(position.getProject(), result, idDomElement.get());
            result.stopHere();
        }
    }
}
