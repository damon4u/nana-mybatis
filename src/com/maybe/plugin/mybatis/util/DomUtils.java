package com.maybe.plugin.mybatis.util;

import com.google.common.collect.Collections2;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-18 18:36
 */
public final class DomUtils {
    
    private DomUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 判断是否是mybatis的mapper文件
     */
    public static boolean isMybatisFile(@Nullable PsiFile file) {
        if (null == file || isNotXmlFile(file)) {
            return false;
        }
        XmlTag rootTag = ((XmlFile) file).getRootTag();
        return null != rootTag && "mapper".equals(rootTag.getName());
    }

    @NotNull
    @NonNls
    static <T extends DomElement> Collection<T> findDomElements(@NotNull Project project, Class<T> clazz) {
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        List<DomFileElement<T>> elements = DomService.getInstance().getFileElements(clazz, project, scope);
        return Collections2.transform(elements, DomFileElement::getRootElement);
    }

    private static boolean isNotXmlFile(@NotNull PsiFile file) {
        return !(file instanceof XmlFile);
    }
}
