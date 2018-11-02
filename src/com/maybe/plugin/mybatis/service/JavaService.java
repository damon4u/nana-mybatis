package com.maybe.plugin.mybatis.service;

import com.maybe.plugin.mybatis.dom.model.IdDomElement;
import com.maybe.plugin.mybatis.dom.model.Mapper;
import com.maybe.plugin.mybatis.util.JavaUtils;
import com.maybe.plugin.mybatis.util.MapperUtils;
import com.google.common.collect.Sets;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-19 15:21
 */
public class JavaService {
    
    private Project project;

    public static JavaService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, JavaService.class);
    }

    /**
     * 获取PsiField的引用类型
     * @param field PsiField，即成员变量
     * @return 成员变量的引用类型，如果变量不是引用类型，返回空
     */
    public Optional<PsiClass> getReferenceClazzOfPsiField(@NotNull PsiElement field) {
        if (!(field instanceof PsiField)) {
            return Optional.empty();
        }
        PsiType type = ((PsiField) field).getType();
        return type instanceof PsiClassReferenceType ? Optional.ofNullable(((PsiClassReferenceType) type).resolve()) : Optional.empty();
    }

    /**
     * 寻找method对应的xml语句
     * @param method
     * @return
     */
    public Optional<DomElement> findXmlStatement(@NotNull PsiMethod method) {
        CommonProcessors.FindFirstProcessor<DomElement> processor = new CommonProcessors.FindFirstProcessor<>();
        process(method, processor);
        return processor.isFound() ? Optional.ofNullable(processor.getFoundValue()) : Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public void process(@NotNull PsiElement target, @NotNull Processor processor) {
        if (target instanceof PsiMethod) {
            process((PsiMethod) target, processor);
        } else if (target instanceof PsiClass) {
            process((PsiClass) target, processor);
        }
    }

    public void process(@NotNull PsiMethod psiMethod, @NotNull Processor<IdDomElement> processor) {
        PsiClass psiClass = psiMethod.getContainingClass();
        if (null == psiClass) return;
        String id = psiClass.getQualifiedName() + "." + psiMethod.getName();
        for (Mapper mapper : MapperUtils.findMappers(psiMethod.getProject())) {
            for (IdDomElement idDomElement : mapper.getDaoElements()) {
                if (MapperUtils.getIdSignature(idDomElement).equals(id)) {
                    processor.process(idDomElement);
                }
            }
        }
    }

    public void process(@NotNull PsiClass clazz, @NotNull Processor<Mapper> processor) {
        String ns = clazz.getQualifiedName();
        for (Mapper mapper : MapperUtils.findMappers(clazz.getProject())) {
            if (MapperUtils.getNamespace(mapper).equals(ns)) {
                processor.process(mapper);
            }
        }
    }

    /**
     * 判断方法是否在mapper中
     * @param method 方法
     * @return
     */
    public boolean withinMapper(@Nullable PsiMethod method) {
        return null != method && isMapper(method.getContainingClass());
    }

    /**
     * 判断java类是否是mapper
     * @param javaClazz java类
     * @return
     */
    public boolean isMapper(@Nullable PsiClass javaClazz) {
        if (javaClazz == null || !JavaUtils.isElementWithinInterface(javaClazz)) {
            return false;
        }

        String packageName = ((PsiJavaFile) javaClazz.getContainingFile()).getPackageName();
        PsiPackage psiPackage = JavaPsiFacade.getInstance(javaClazz.getProject()).findPackage(packageName);
        for (PsiPackage pkg : getMapperPackageList(javaClazz.getProject())) {
            if (pkg.equals(psiPackage)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取所有mapper所在包
     * @param project
     * @return
     */
    private Set<PsiPackage> getMapperPackageList(@NotNull Project project) {
        HashSet<PsiPackage> result = Sets.newHashSet();
        Collection<Mapper> mappers = MapperUtils.findMappers(project);
        JavaPsiFacade javaPsiFacade = JavaPsiFacade.getInstance(project);
        for (Mapper mapper : mappers) {
            String namespace = MapperUtils.getNamespace(mapper);
            PsiClass clazz = javaPsiFacade.findClass(namespace, GlobalSearchScope.allScope(project));
            if (null != clazz) {
                PsiFile file = clazz.getContainingFile();
                if (file instanceof PsiJavaFile) {
                    String packageName = ((PsiJavaFile) file).getPackageName();
                    PsiPackage pkg = javaPsiFacade.findPackage(packageName);
                    if (null != pkg) {
                        result.add(pkg);
                    }
                }
            }
        }
        return result;
    }
    
}
