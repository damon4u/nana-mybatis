package com.maybe.plugin.mybatis.reference;

import com.maybe.plugin.mybatis.annotation.Annotation;
import com.maybe.plugin.mybatis.dom.model.IdDomElement;
import com.maybe.plugin.mybatis.util.JavaUtils;
import com.maybe.plugin.mybatis.util.MapperUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-23 21:05
 */
public class ParamReference extends PsiReferenceBase<XmlElement> {
    
    // 参数名称
    private String paramName;

    public ParamReference(@NotNull XmlElement attributeValue, TextRange textRange, @NotNull String paramName) {
        // 调用父类构造函数
        super(attributeValue, textRange);
        this.paramName = paramName;
    }
    
    @Nullable
    @Override
    public PsiElement resolve() {
        XmlElement element = getElement();
        Project project = element.getProject();
        // 寻找当前元素的父级IdDomElement，这里应该拿到的是Select，Update等，包含Dao层方法名
        IdDomElement domElement = MapperUtils.findParentIdDomElement(element).orElse(null);
        if (domElement == null) {
            return null;
        }
        // 根据mapper的namespace拿到Dao类，然后根据IdDomElement的id拿到方法名称
        final PsiMethod method = JavaUtils.findMethod(project, domElement).orElse(null);
        if (method == null) {
            return null;
        }
        // 取出方法参数
        final PsiParameter[] parameters = method.getParameterList().getParameters();

        // dao层方法参数可能有两种情况
        // 1、只有一个参数，那么可能这个参数没有用@Param注解标注，那么直接使用参数名称与paramName比较；
        // 2、方法有多个参数，那么每个参数都应该使用@Param注解标注，用@Param的value值挨个与paramName比较。如果没有标注，那么就认为没找到，不创建引用。
        if (parameters.length == 1) {
            PsiParameter parameter = parameters[0];
            if (paramName.equals(parameter.getName())) {
                return parameter;
            } else {
                return null;
            }
        } else {
            for (final PsiParameter parameter : parameters) {
                final Optional<String> value = JavaUtils.getAnnotationValueText(parameter, Annotation.PARAM);
                if (value.isPresent() && paramName.equals(value.get())) {
                    return parameter;
                }
            }
        }
        return null;
    }

    /**
     * 这个方法是用来提供代码补全候选的，这里没有实现，后面会用SqlParamCompletionContributor实现补全
     */
    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
