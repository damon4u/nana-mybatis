package com.maybe.plugin.mybatis.alias;

import com.google.common.collect.Lists;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-11-01 18:10
 */
public class AliasService {
    
    private Project project;
    
    private JavaPsiFacade javaPsiFacade;
    
    private List<AliasResolver> resolvers;

    public static final AliasService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, AliasService.class);
    }

    public AliasService(Project project) {
        this.project = project;
        this.resolvers = Lists.newArrayList();
        this.javaPsiFacade = JavaPsiFacade.getInstance(project);
        initResolvers();        
    }
    
    private void initResolvers() {
        try {
            Class.forName("com.intellij.spring.model.utils.SpringModelUtils");
            this.registerResolver(AliasResolverFactory.createBeanResolver(project));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.registerResolver(AliasResolverFactory.createInnerAliasResolver(project));
        this.registerResolver(AliasResolverFactory.createAnnotationResolver(project));
    }

    private void registerResolver(@NotNull AliasResolver resolver) {
        this.resolvers.add(resolver);
    }

    public Optional<PsiClass> findPsiClass(@Nullable PsiElement element, @NotNull String shortName) {
        PsiClass clazz = javaPsiFacade.findClass(shortName, GlobalSearchScope.allScope(project));
        if (null != clazz) {
            return Optional.of(clazz);
        }
        for (AliasResolver resolver : resolvers) {
            for (Alias alias : resolver.getClassAlias(element)) {
                if (alias.getAliasValue().equalsIgnoreCase(shortName)) {
                    return Optional.of(alias.getClazz());
                }
            }
        }
        return Optional.empty();
    }

    public Collection<Alias> getAliasCollection(@Nullable PsiElement element) {
        ArrayList<Alias> result = Lists.newArrayList();
        for (AliasResolver resolver : resolvers) {
            result.addAll(resolver.getClassAlias(element));
        }
        return result;
    }
}
