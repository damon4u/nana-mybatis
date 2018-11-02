package com.maybe.plugin.mybatis.util;

import com.maybe.plugin.mybatis.annotation.Annotation;
import com.maybe.plugin.mybatis.dom.model.IdDomElement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiModifierListOwner;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-19 14:44
 */
public final class JavaUtils {

    private JavaUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 从类中查找成员变量
     *
     * @param clazz        类
     * @param propertyName 属性名称
     * @return
     */
    @NotNull
    public static Optional<PsiField> findSettablePsiField(@NotNull final PsiClass clazz,
                                                          @Nullable final String propertyName) {
        final PsiField field = PropertyUtil.findPropertyField(clazz, propertyName, false);
        return Optional.ofNullable(field);
    }

    /**
     * 获取类中所有成员变量
     *
     * @param clazz 类
     * @return
     */
    @NotNull
    public static PsiField[] findSettablePsiFields(final @NotNull PsiClass clazz) {
        final PsiField[] allFields = clazz.getAllFields();
        final List<PsiField> settableFields = new ArrayList<>(allFields.length);
        for (final PsiField field : allFields) {
            final PsiModifierList modifierList = field.getModifierList();
            if (modifierList != null &&
                    (modifierList.hasModifierProperty(PsiModifier.STATIC)
                            || modifierList.hasModifierProperty(PsiModifier.FINAL))) {
                continue;
            }
            settableFields.add(field);
        }
        return settableFields.toArray(new PsiField[0]);
    }

    @NotNull
    public static Optional<PsiMethod> findMethod(@NotNull Project project, @NotNull IdDomElement element) {
        return findMethod(project, MapperUtils.getNamespace(element), MapperUtils.getId(element));
    }

    @NotNull
    public static Optional<PsiMethod> findMethod(@NotNull Project project, @Nullable String clazzName, @Nullable String methodName) {
        if (StringUtils.isBlank(clazzName) || StringUtils.isBlank(methodName)) {
            return Optional.empty();
        }
        Optional<PsiClass> clazz = findClazz(project, clazzName);
        if (clazz.isPresent()) {
            PsiMethod[] methods = clazz.get().findMethodsByName(methodName, true);
            return ArrayUtils.isEmpty(methods) ? Optional.empty() : Optional.of(methods[0]);
        }
        return Optional.empty();
    }

    @NotNull
    public static Optional<PsiClass> findClazz(@NotNull Project project, @NotNull String clazzName) {
        return Optional.ofNullable(JavaPsiFacade.getInstance(project).findClass(clazzName, GlobalSearchScope.allScope(project)));
    }

    public static Optional<String> getAnnotationValueText(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        Optional<PsiAnnotationMemberValue> annotationValue = getAnnotationValue(target, annotation);
        return annotationValue.map(psiAnnotationMemberValue -> psiAnnotationMemberValue.getText().replaceAll("\"", ""));
    }

    @NotNull
    public static Optional<PsiAnnotationMemberValue> getAnnotationValue(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        return getAnnotationAttributeValue(target, annotation, "value");
    }

    @NotNull
    public static Optional<PsiAnnotationMemberValue> getAnnotationAttributeValue(@NotNull PsiModifierListOwner target,
                                                                                 @NotNull Annotation annotation,
                                                                                 @NotNull String attrName) {
        if (!isAnnotationPresent(target, annotation)) {
            return Optional.empty();
        }
        Optional<PsiAnnotation> psiAnnotation = getPsiAnnotation(target, annotation);
        return psiAnnotation.isPresent() ? Optional.ofNullable(psiAnnotation.get().findAttributeValue(attrName)) : Optional.empty();
    }

    public static boolean isAnnotationPresent(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        PsiModifierList modifierList = target.getModifierList();
        return null != modifierList && null != modifierList.findAnnotation(annotation.getQualifiedName());
    }

    public static boolean isAnyAnnotationPresent(@NotNull PsiModifierListOwner target, @NotNull Set<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (isAnnotationPresent(target, annotation)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    public static Optional<PsiAnnotation> getPsiAnnotation(@NotNull PsiModifierListOwner target, @NotNull Annotation annotation) {
        PsiModifierList modifierList = target.getModifierList();
        return null == modifierList ? Optional.empty() : Optional.ofNullable(modifierList.findAnnotation(annotation.getQualifiedName()));
    }

    /**
     * 判断Psi元素是否是在接口中
     * 有两种情况，可能本身就是接口，返回true
     * 可能本身是个方法，在接口中，也返回true
     * @param element
     * @return
     */
    public static boolean isElementWithinInterface(@Nullable PsiElement element) {
        if (element instanceof PsiClass && ((PsiClass) element).isInterface()) {
            return true;
        }
        PsiClass type = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        return Optional.ofNullable(type).isPresent() && type.isInterface();
    }

    /**
     * 判断clazz是否是实体类
     * @param clazz 类
     */
    public static boolean isModelClazz(@Nullable PsiClass clazz) {
        return null != clazz && !clazz.isAnnotationType() && !clazz.isInterface() && !clazz.isEnum() && clazz.isValid();
    }
}
