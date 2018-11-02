package com.maybe.plugin.mybatis.linemarker;

import com.maybe.plugin.mybatis.dom.model.DMLAndDQLDynamicQueryableDomElement;
import com.maybe.plugin.mybatis.dom.model.Delete;
import com.maybe.plugin.mybatis.dom.model.IdDomElement;
import com.maybe.plugin.mybatis.dom.model.Insert;
import com.maybe.plugin.mybatis.dom.model.Select;
import com.maybe.plugin.mybatis.dom.model.Update;
import com.maybe.plugin.mybatis.util.Icons;
import com.maybe.plugin.mybatis.util.JavaUtils;
import com.maybe.plugin.mybatis.util.MapperUtils;
import com.google.common.collect.ImmutableList;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.Function;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-25 21:16
 */
public class XmlToDaoLineMarkerProvider implements LineMarkerProvider {

    private static final ImmutableList<Class<? extends DMLAndDQLDynamicQueryableDomElement>> TARGET_TYPES = ImmutableList.of(
            Select.class,
            Update.class,
            Insert.class,
            Delete.class
    );
    
    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element) {
        if (!isTargetElement(element)) {
            return null;
        }
        DomElement domElement = DomUtil.getDomElement(element);
        if (domElement == null) {
            return null;
        }
        Optional<PsiMethod> method = JavaUtils.findMethod(element.getProject(), (IdDomElement) domElement);
        return method.map(psiMethod -> new LineMarkerInfo<>((XmlTag) element,
                element.getTextRange(),
                Icons.XML_TO_DAO_LINE_MARKER_ICON,
                Pass.UPDATE_ALL,
                getTooltipProvider(psiMethod),
                getNavigationHandler(psiMethod),
                GutterIconRenderer.Alignment.CENTER)).orElse(null);
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> elements, @NotNull Collection<LineMarkerInfo> result) {

    }

    private boolean isTargetElement(@NotNull PsiElement element) {
        return element instanceof XmlTag
                && MapperUtils.isElementWithinMybatisFile(element)
                && isTargetType(element);
    }

    private boolean isTargetType(PsiElement element) {
        DomElement domElement = DomUtil.getDomElement(element);
        for (Class<?> clazz : TARGET_TYPES) {
            if (clazz.isInstance(domElement))
                return true;
        }
        return false;
    }

    private Function<XmlTag, String> getTooltipProvider(final PsiMethod target) {
        return from -> getTooltip(target);
    }

    private GutterIconNavigationHandler<XmlTag> getNavigationHandler(final PsiMethod target) {
        return (e, from) -> ((Navigatable) target.getNavigationElement()).navigate(true);
    }

    private String getTooltip(@NotNull PsiMethod target) {
        PsiClass containingClass = target.getContainingClass();
        if (containingClass == null) {
            return "Data access object not found";
        }
        return "Data access object found - " + containingClass.getQualifiedName();
    }
    
    
}
