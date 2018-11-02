package com.maybe.plugin.mybatis.inspection;

import com.maybe.plugin.mybatis.annotation.Annotation;
import com.maybe.plugin.mybatis.service.JavaService;
import com.maybe.plugin.mybatis.util.JavaUtils;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.daemon.HighlightDisplayKey;
import com.intellij.codeInspection.AbstractBaseJavaLocalInspectionTool;
import com.intellij.codeInspection.CustomSuppressableInspectionTool;
import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.SuppressIntentionAction;
import com.intellij.codeInspection.SuppressManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-30 21:04
 */
public class MapperMethodInspection extends AbstractBaseJavaLocalInspectionTool implements CustomSuppressableInspectionTool {


    @Nullable
    @Override
    public ProblemDescriptor[] checkMethod(@NotNull PsiMethod method, @NotNull InspectionManager manager, boolean isOnTheFly) {
        if (!JavaService.getInstance(method.getProject()).withinMapper(method) || JavaUtils.isAnyAnnotationPresent(method, Annotation.STATEMENT_SYMMETRIES)) {
            return new ProblemDescriptor[0];
        }

        final List<ProblemDescriptor> problemDescriptorList = Lists.newArrayListWithCapacity(2);
        Optional<ProblemDescriptor> problemDescriptor = checkStatementExists(method, manager, isOnTheFly);
        problemDescriptor.ifPresent(problemDescriptorList::add);
        return problemDescriptorList.toArray(new ProblemDescriptor[0]);
    }
    
    private Optional<ProblemDescriptor> checkStatementExists(@NotNull final PsiMethod method,
                                                             final InspectionManager manager,
                                                             final boolean isOnTheFly) {
        final PsiIdentifier methodName = method.getNameIdentifier();
        if (null != methodName && !JavaService.getInstance(method.getProject()).findXmlStatement(method).isPresent()) {
            return Optional.of(manager.createProblemDescriptor(methodName, 
                    "Statement with id=\"#ref\" not defined in mapper XML",
                    (LocalQuickFix) null,
                    ProblemHighlightType.GENERIC_ERROR,
                    isOnTheFly));
        }
        return Optional.empty();
    }


    @Nullable
    @Override
    public SuppressIntentionAction[] getSuppressActions(@Nullable PsiElement element) {
        String shortName = getShortName();
        HighlightDisplayKey key = HighlightDisplayKey.find(shortName);
        if (key == null) {
            throw new AssertionError("HighlightDisplayKey.find(" + shortName + ") is null. Inspection: " + getClass());
        }
        return SuppressManager.getInstance().createSuppressActions(key);
    }
}
