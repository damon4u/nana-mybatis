package com.maybe.plugin.mybatis.completion;

import com.maybe.plugin.mybatis.annotation.Annotation;
import com.maybe.plugin.mybatis.dom.model.IdDomElement;
import com.maybe.plugin.mybatis.util.JavaUtils;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrioritizedLookupElement;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-24 20:01
 */
abstract class BaseParamCompletionContributor extends CompletionContributor {

    private static final double PRIORITY = 400.0;

    static void addCompletionForPsiParameter(@NotNull final Project project,
                                             @NotNull final CompletionResultSet completionResultSet,
                                             @Nullable final IdDomElement element) {
        if (element == null) {
            return;
        }

        final PsiMethod method = JavaUtils.findMethod(project, element).orElse(null);

        if (method == null) {
            return;
        }

        final PsiParameter[] parameters = method.getParameterList().getParameters();

        if (parameters.length == 1) {
            final PsiParameter parameter = parameters[0];
            completionResultSet.addElement(buildLookupElementWithIcon(parameter.getName(), parameter.getType().getPresentableText()));
        } else {
            for (PsiParameter parameter : parameters) {
                final Optional<String> annotationValueText = JavaUtils.getAnnotationValueText(parameter, Annotation.PARAM);
                completionResultSet.addElement(buildLookupElementWithIcon(annotationValueText.orElseGet(parameter::getName), parameter.getType().getPresentableText()));
            }
        }
        
    }

    private static LookupElement buildLookupElementWithIcon(final String parameterName, 
                                                            final String parameterType) {
        return PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create(parameterName)
                        .withTypeText(parameterType)
                        .withIcon(PlatformIcons.PARAMETER_ICON),
                PRIORITY);
    }
    
}
