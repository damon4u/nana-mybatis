package com.maybe.plugin.mybatis.util;

import com.maybe.plugin.mybatis.dom.model.Association;
import com.maybe.plugin.mybatis.dom.model.Collection;
import com.maybe.plugin.mybatis.dom.model.IdDomElement;
import com.maybe.plugin.mybatis.dom.model.Mapper;
import com.maybe.plugin.mybatis.dom.model.ResultMap;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-23 15:56
 */
public final class MapperUtils {

    private MapperUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取property从属的类型
     *
     * @param attributeValue property属性
     * @return
     */
    public static Optional<PsiClass> getPropertyClazz(XmlAttributeValue attributeValue) {
        DomElement domElement = DomUtil.getDomElement(attributeValue);
        if (null == domElement) {
            return Optional.empty();
        }
        // collection标签下的property，那么类型为ofType指定
        Collection collection = DomUtil.getParentOfType(domElement, Collection.class, true);
        if (null != collection && isNotWithinSameTag(collection, attributeValue)) {
            return Optional.ofNullable(collection.getOfType().getValue());
        }
        // association标签下的property，那么类型为javaType指定
        Association association = DomUtil.getParentOfType(domElement, Association.class, true);
        if (null != association && isNotWithinSameTag(association, attributeValue)) {
            return Optional.ofNullable(association.getJavaType().getValue());
        }
        // resultMap标签下的property，那么类型为type指定
        ResultMap resultMap = DomUtil.getParentOfType(domElement, ResultMap.class, true);
        if (null != resultMap && isNotWithinSameTag(resultMap, attributeValue)) {
            return Optional.ofNullable(resultMap.getType().getValue());
        }
        return Optional.empty();

    }

    private static boolean isNotWithinSameTag(@NotNull DomElement domElement, @NotNull XmlElement xmlElement) {
        XmlTag xmlTag = PsiTreeUtil.getParentOfType(xmlElement, XmlTag.class);
        return !domElement.getXmlTag().equals(xmlTag);
    }

    @NotNull
    public static Optional<IdDomElement> findParentIdDomElement(@Nullable PsiElement element) {
        DomElement domElement = DomUtil.getDomElement(element);
        if (null == domElement) {
            return Optional.empty();
        }
        if (domElement instanceof IdDomElement) {
            return Optional.of((IdDomElement) domElement);
        }
        return Optional.ofNullable(DomUtil.getParentOfType(domElement, IdDomElement.class, true));
    }

    public static boolean isElementWithinMybatisFile(@NotNull PsiElement element) {
        PsiFile psiFile = element.getContainingFile();
        return element instanceof XmlElement && DomUtils.isMybatisFile(psiFile);
    }
    
    @NotNull
    @NonNls
    public static String getNamespace(@NotNull DomElement element) {
        return getNamespace(getMapper(element));
    }
    
    @NotNull
    @NonNls
    public static String getNamespace(@NotNull Mapper mapper) {
        String ns = mapper.getNamespace().getStringValue();
        return null == ns ? "" : ns;
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @NonNls
    public static Mapper getMapper(@NotNull DomElement element) {
        Optional<Mapper> optional = Optional.ofNullable(DomUtil.getParentOfType(element, Mapper.class, true));
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new IllegalArgumentException("Unknown element");
        }
    }

    @Nullable
    @NonNls
    public static <T extends IdDomElement> String getId(@NotNull T domElement) {
        return domElement.getId().getRawText();
    }

    @NotNull
    @NonNls
    public static <T extends IdDomElement> String getIdSignature(@NotNull T domElement) {
        return getNamespace(domElement) + "." + getId(domElement);
    }

    @NotNull
    @NonNls
    public static java.util.Collection<Mapper> findMappers(@NotNull Project project) {
        return DomUtils.findDomElements(project, Mapper.class);
    }
}
