package com.maybe.plugin.mybatis.alias;

import com.maybe.plugin.mybatis.util.JavaUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-11-01 18:13
 */
public abstract class AliasResolver {
    
    protected Project project;

    public AliasResolver(Project project) {
        this.project = project;
    }

    @NotNull
    protected Optional<Alias> addAlias(@NotNull Set<Alias> aliasSet, @Nullable PsiClass clazz, @Nullable String aliasValue) {
        if (null == aliasValue || !JavaUtils.isModelClazz(clazz)) {
            return Optional.empty();
        }
        Alias alias = new Alias(clazz, aliasValue);
        aliasSet.add(alias);
        return Optional.of(alias);
    }

    public abstract Set<Alias> getClassAlias(@Nullable PsiElement element);

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
