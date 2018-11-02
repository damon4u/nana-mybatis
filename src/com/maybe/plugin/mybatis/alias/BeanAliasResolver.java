package com.maybe.plugin.mybatis.alias;

import com.maybe.plugin.mybatis.util.JavaUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.spring.CommonSpringModel;
import com.intellij.spring.SpringManager;
import com.intellij.spring.SpringModelVisitorUtils;
import com.intellij.spring.model.SpringBeanPointer;
import com.intellij.spring.model.utils.SpringPropertyUtils;
import com.intellij.spring.model.xml.beans.SpringPropertyDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-11-01 20:49
 */
public class BeanAliasResolver extends PackageAliasResolver {

    private static final String MAPPER_ALIAS_PACKAGE_CLASS = "org.mybatis.spring.SqlSessionFactoryBean";
    private static final String MAPPER_ALIAS_PROPERTY = "typeAliasesPackage";

    private static final Splitter SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

    private ModuleManager moduleManager;
    private SpringManager springManager;

    public BeanAliasResolver(Project project) {
        super(project);
        this.moduleManager = ModuleManager.getInstance(project);
        this.springManager = SpringManager.getInstance(project);
    }
    
    @NotNull
    @Override
    public Collection<String> getPackages(@Nullable PsiElement element) {
        Set<String> result = Sets.newHashSet();
        for (Module module : moduleManager.getModules()) {
            for (CommonSpringModel springModel : springManager.getCombinedModel(module).getRelatedModels()) {
                addPackages(result, springModel);
            }
        }
        return result;
    }

    private void addPackages(Set<String> result, CommonSpringModel springModel) {
        Optional<PsiClass> sqlSessionFactoryClazzOpt = JavaUtils.findClazz(project, MAPPER_ALIAS_PACKAGE_CLASS);
        if (sqlSessionFactoryClazzOpt.isPresent()) {
            Collection<SpringBeanPointer> allDomBeans = SpringModelVisitorUtils.getAllDomBeans(springModel);
            PsiClass sqlSessionFactoryClazz = sqlSessionFactoryClazzOpt.get();

            for (Object domBean : allDomBeans) {
                SpringBeanPointer pointer = (SpringBeanPointer) domBean;
                PsiClass beanClass = pointer.getBeanClass();
                if (beanClass != null && beanClass.equals(sqlSessionFactoryClazz)) {
                    SpringPropertyDefinition basePackages = SpringPropertyUtils.findPropertyByName(pointer.getSpringBean(), MAPPER_ALIAS_PROPERTY);
                    if (basePackages != null) {
                        String stringValue = basePackages.getValueAsString();
                        if (stringValue != null) {
                            ArrayList<String> stringArrayList = Lists.newArrayList(SPLITTER.split(stringValue));
                            result.addAll(stringArrayList);
                        }
                    }
                }
            }
        }
    }
}
