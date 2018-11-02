package com.maybe.plugin.mybatis.reference;

import com.maybe.plugin.mybatis.util.MapperUtils;
import com.google.common.collect.Lists;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-23 18:10
 */
public class TestParamReferenceContributor extends PsiReferenceContributor {

    private static final Pattern PARAM_PATTERN = Pattern.compile("([A-Za-z]+)");

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        // 注意，这里第一个参数说明是要给XmlAttributeValue创建引用，那么下面返回TestParamReference的泛型参数也是XmlAttributeValue
        // 开始试过一次XmlAttribute来作为pattern，然后TestParamReference的泛型参数是XmlAttributeValue，这样其实需要点击外层属性name才能触发，不对应
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(XmlAttributeValue.class),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        XmlAttributeValue xmlAttributeValue = (XmlAttributeValue) element;
                        if (MapperUtils.isElementWithinMybatisFile(xmlAttributeValue)) {
                            // XmlAttributeValue其实是XmlAttribute的子标签，获取父类即可拿到XmlAttribute
                            PsiElement xmlAttribute = xmlAttributeValue.getParent();
                            if (xmlAttribute instanceof XmlAttribute && ((XmlAttribute) xmlAttribute).getName().equals("test")) {
                                String value = xmlAttributeValue.getValue();
                                if (StringUtils.isNotBlank(value)) {
                                    ArrayList<PsiReference> referenceList = Lists.newArrayList();
                                    Matcher matcher = PARAM_PATTERN.matcher(value);
                                    while (matcher.find()) {
                                        String param = matcher.group(1);
                                        if (param.equalsIgnoreCase("and")
                                                || param.equalsIgnoreCase("or")
                                                || param.equalsIgnoreCase("null")) {
                                            continue;
                                        }
                                        // 这里加1是因为attribute value用双引号包着
                                        int start = matcher.start(1) + 1;
                                        int end = matcher.end(1) + 1;
                                        referenceList.add(new ParamReference(xmlAttributeValue, new TextRange(start, end), param));
                                    }
                                    return referenceList.toArray(new PsiReference[0]);
                                }
                            }
                        }

                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }

}
