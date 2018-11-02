package com.maybe.plugin.mybatis.alias;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-11-01 21:19
 */
public class AliasResolverFactory {

    public static AliasResolver createInnerAliasResolver(@NotNull Project project) {
        return new InnerAliasResolver(project);
    }

    public static AliasResolver createAnnotationResolver(@NotNull Project project) {
        return new AnnotationAliasResolver(project);
    }

    public static AliasResolver createBeanResolver(@NotNull Project project) {
        return new BeanAliasResolver(project);
    }
}
