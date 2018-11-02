package com.maybe.plugin.mybatis.alias;

import com.google.common.collect.Sets;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-11-01 20:52
 */
public abstract class PackageAliasResolver extends AliasResolver {
    
    private JavaPsiFacade javaPsiFacade;

    public PackageAliasResolver(Project project) {
        super(project);
        this.javaPsiFacade = JavaPsiFacade.getInstance(project);
    }
    
    @Override
    public Set<Alias> getClassAlias(@Nullable PsiElement element) {
        HashSet<Alias> result = Sets.newHashSet();
        for (String pkgName : getPackages(element)) {
            if (StringUtils.isBlank(pkgName)) {
                continue;
            }
            PsiPackage pkg = javaPsiFacade.findPackage(pkgName);
            if (null != pkg) {
                addAlias(result, pkg);
                for (PsiPackage tmp : pkg.getSubPackages()) {
                    addAlias(result, tmp);
                }
            }
        }
        return result;
    }

    private void addAlias(Set<Alias> result, PsiPackage pkg) {
        for (PsiClass clazz : pkg.getClasses()) {
            addAlias(result, clazz, clazz.getName());
        }
    }

    @NotNull
    public abstract Collection<String> getPackages(@Nullable PsiElement element);
}
