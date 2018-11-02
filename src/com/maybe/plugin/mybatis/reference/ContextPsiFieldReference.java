package com.maybe.plugin.mybatis.reference;

import com.maybe.plugin.mybatis.service.JavaService;
import com.maybe.plugin.mybatis.util.JavaUtils;
import com.maybe.plugin.mybatis.util.MapperUtils;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Description:
 *
 * @author damon4u
 * @version 2018-10-19 15:02
 */
public class ContextPsiFieldReference extends PsiReferenceBase<XmlAttributeValue> {

    private PsiFiledReferenceSetResolver resolver;

    /**
     * 当前解析层级
     * 例如property为"user.name"
     * 那么如果鼠标点击的user，index为1
     * 如果鼠标点击的是name，index为2
     */
    private int index;

    /**
     * @param element 当前元素
     * @param range   元素边界
     * @param index   引用层级
     */
    ContextPsiFieldReference(XmlAttributeValue element, TextRange range, int index) {
        super(element, range, false);
        this.index = index;
        resolver = new PsiFiledReferenceSetResolver(element);
    }

    /**
     * 解析xml属性，返回对应的引用变量
     */
    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public PsiElement resolve() {
        Optional<PsiElement> resolved = (Optional<PsiElement>) resolver.resolve(index);
        return resolved.orElse(null);
    }

    /**
     * 代码自动提示类型中所有可赋值（非static和非final）的成员变量列表
     *
     * @return 类型中所有可赋值（非static和非final）的成员变量列表
     */
    @NotNull
    @Override
    public Object[] getVariants() {
        Optional<PsiClass> clazz = getTargetClazz();
        return clazz.isPresent() ? JavaUtils.findSettablePsiFields(clazz.get()) : PsiReference.EMPTY_ARRAY;
    }

    /**
     * 获取property参数的类型
     * 如果是简单的"name"，那么就使用外层标签指定的类型
     * 如果是带引用的"user.name"，那么需要深入解析出具体引用类型user
     *
     * @return property参数的类型
     */
    @SuppressWarnings("unchecked")
    private Optional<PsiClass> getTargetClazz() {
        String elementValue = getElement().getValue();
        if (elementValue == null) {
            return Optional.empty();
        }
        if (elementValue.contains(String.valueOf(ReferenceSetBase.DOT_SEPARATOR))) {
            // 包含点号，说明此参数的类型为引用类型
            // 例如一个类的成员变量中包含其他引用类型
            int ind = 0 == index ? 0 : index - 1;
            Optional<PsiElement> resolved = (Optional<PsiElement>) resolver.resolve(ind);
            if (resolved.isPresent()) {
                return JavaService.getInstance(myElement.getProject()).getReferenceClazzOfPsiField(resolved.get());
            }
        } else { // 没有点号，说明参数类型不是引用类型，直接就是外层标签定义的类型（如resultMap中type指定的类型）中包含的简单类型，如基本类型或者字符串类型变量
            return MapperUtils.getPropertyClazz(myElement);
        }
        return Optional.empty();
    }

}
