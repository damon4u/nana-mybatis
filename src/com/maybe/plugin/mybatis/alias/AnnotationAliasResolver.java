package com.maybe.plugin.mybatis.alias;

import com.maybe.plugin.mybatis.annotation.Annotation;
import com.maybe.plugin.mybatis.util.JavaUtils;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.AnnotatedElementsSearch;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-11-01 20:16
 */
public class AnnotationAliasResolver extends AliasResolver {
    
    private static final Function FUN = new Function<PsiClass, Alias>() {
        @Override
        public Alias apply(PsiClass psiClass) {
            Optional<String> annotationValueText = JavaUtils.getAnnotationValueText(psiClass, Annotation.ALIAS);
            return annotationValueText.map(annotationValue -> new Alias(psiClass, annotationValue)).orElse(null);
        }
    };

    public AnnotationAliasResolver(Project project) {
        super(project);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Set<Alias> getClassAlias(@Nullable PsiElement element) {
        Optional<PsiClass> clazz = Annotation.ALIAS.toPsiClass(project);
        if (clazz.isPresent()) {
            Collection<PsiClass> result = AnnotatedElementsSearch.searchPsiClasses(clazz.get(), GlobalSearchScope.allScope(project)).findAll();
            return Sets.newHashSet(Collections2.transform(result, FUN));
        }
        return Collections.emptySet();
    }
}
