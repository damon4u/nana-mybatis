package com.maybe.plugin.mybatis.linemarker;

import com.maybe.plugin.mybatis.dom.model.IdDomElement;
import com.maybe.plugin.mybatis.service.JavaService;
import com.maybe.plugin.mybatis.util.Icons;
import com.maybe.plugin.mybatis.util.JavaUtils;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.CommonProcessors;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-25 20:57
 */
public class DaoToXmlLineMarkerProvider extends RelatedItemLineMarkerProvider {

    private static final Function<DomElement, XmlTag> FUNCTION = DomElement::getXmlTag;

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, @NotNull Collection<? super RelatedItemLineMarkerInfo> result) {
        if (element instanceof PsiNameIdentifierOwner && JavaUtils.isElementWithinInterface(element)) {
            CommonProcessors.CollectProcessor<IdDomElement> processor = new CommonProcessors.CollectProcessor<>();
            JavaService.getInstance(element.getProject()).process(element, processor);
            Collection<IdDomElement> results = processor.getResults();
            if (!results.isEmpty()) {
                NavigationGutterIconBuilder<PsiElement> builder = NavigationGutterIconBuilder.create(Icons.DAO_TO_XML_LINE_MARKER_ICON)
                        .setAlignment(GutterIconRenderer.Alignment.CENTER)
                        .setTargets(Collections2.transform(results, FUNCTION))
                        .setTooltipTitle("Navigation to target in mapper xml");
                PsiElement nameIdentifier = ((PsiNameIdentifierOwner) element).getNameIdentifier();
                if (nameIdentifier != null) {
                    result.add(builder.createLineMarkerInfo(nameIdentifier));
                }
            }
        }
    }
}
