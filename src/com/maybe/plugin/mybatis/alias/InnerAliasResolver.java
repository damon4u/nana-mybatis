package com.maybe.plugin.mybatis.alias;

import com.maybe.plugin.mybatis.util.JavaUtils;
import com.google.common.collect.ImmutableSet;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Description:
 * 内置别名
 *
 * @author damon4u
 * @version 2018-11-01 20:10
 */
public class InnerAliasResolver extends AliasResolver {

    private final Set<Alias> innerAliasSet = ImmutableSet.of(
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.String").get(), "string"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.Byte").get(), "byte"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.Long").get(), "long"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.Short").get(), "short"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.Integer").get(), "int"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.Integer").get(), "integer"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.Double").get(), "double"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.Float").get(), "float"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.Boolean").get(), "boolean"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.util.Date").get(), "date"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.math.BigDecimal").get(), "decimal"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.lang.Object").get(), "object"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.util.Map").get(), "map"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.util.HashMap").get(), "hashmap"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.util.List").get(), "list"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.util.ArrayList").get(), "arraylist"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.util.Collection").get(), "collection"),
            Alias.getInstance(JavaUtils.findClazz(project, "java.util.Iterator").get(), "iterator")
    );

    public InnerAliasResolver(Project project) {
        super(project);
    }

    @Override
    public Set<Alias> getClassAlias(@Nullable PsiElement element) {
        return innerAliasSet;
    }
}
