package com.maybe.plugin.mybatis.reference;

import com.maybe.plugin.mybatis.util.JavaUtils;
import com.maybe.plugin.mybatis.util.MapperUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.ReferenceSetBase;
import com.intellij.psi.xml.XmlAttributeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * Description:
 * <p>
 * 将xml属性解析到实体类字段
 *
 * @author damon4u
 * @version 2018-10-19 11:46
 */
class PsiFiledReferenceSetResolver {

    /**
     * 嵌套引用分隔符
     */
    private static final Splitter SPLITTER = Splitter.on(String.valueOf(ReferenceSetBase.DOT_SEPARATOR));

    /**
     * 当前属性元素
     */
    private XmlAttributeValue element;

    /**
     * 属性全名可能包含引用，例如user.name，那么按照点号分割，保存所有字段名称
     */
    private List<String> fieldNameWithReferenceList;

    PsiFiledReferenceSetResolver(@NotNull XmlAttributeValue element) {
        this.element = element;
        // 属性全名，可能包含引用，如user.name
        String wholeFiledName = element.getValue() != null ? element.getValue() : "";
        this.fieldNameWithReferenceList = Lists.newArrayList(SPLITTER.split(wholeFiledName));
    }

    /**
     * 将xml属性解析到实体类字段
     *
     * @param index 按照点号分割后，属性位于哪一级，index为索引值
     *              例如user.name，那么idea会为user和name分别创建引用，都可以鼠标点击跳转
     *              user的index为0，name的index为1
     *              这个值决定解析层级深度
     */
    final Optional<? extends PsiElement> resolve(int index) {
        // 先获取一级属性
        // 对于简单属性"name"，那么就是这个属性
        // 对于包含引用的情况"user.name"，那么先找到一级属性user
        Optional<PsiField> firstLevelElement = getFirstLevelElement(Iterables.getFirst(fieldNameWithReferenceList, null));
        return firstLevelElement.isPresent() ?
                (fieldNameWithReferenceList.size() > 1 ? parseNextLevelElement(firstLevelElement, index) : firstLevelElement)
                : Optional.empty();
    }

    /**
     * 获取一级属性
     * 对于简单属性"name"，那么就是这个属性
     * 对于包含引用的情况"user.name"，那么先找到一级属性user
     *
     * @param firstLevelFieldName 第一层级的属性名，可能是一个引用，也可能是基本类型
     * @return
     */
    @NotNull
    private Optional<PsiField> getFirstLevelElement(@Nullable String firstLevelFieldName) {
        Optional<PsiClass> clazz = MapperUtils.getPropertyClazz(element);
        return clazz.flatMap(psiClass -> JavaUtils.findSettablePsiField(psiClass, firstLevelFieldName));
    }

    /**
     * 按照点号逐层解析，前面层级为引用，去引用中解析下一层字段
     *
     * @param maxLevelIndex 最大解析层级深度，比如"user.name"，那么如果是要为user建立引用，maxLevelIndex为1；
     *                      如果要为name建立引用，那么maxLevelIndex为2
     */
    private Optional<PsiField> parseNextLevelElement(Optional<PsiField> current, int maxLevelIndex) {
        int index = 1;
        while (current.isPresent() && index <= maxLevelIndex) {
            String nextLevelIndexFiledName = fieldNameWithReferenceList.get(index);
            if (nextLevelIndexFiledName.contains(" ")) {
                return Optional.empty();
            }
            current = resolveReferenceField(current.get(), nextLevelIndexFiledName);
            index++;
        }
        return current;
    }

    /**
     * 从引用类型中解析字段
     * 例如user.name
     * 那么current为前面解析出来的user引用，fieldName为name
     *
     * @param current   当前引用
     * @param fieldName 要从引用中解析的字段名称
     * @return 字段
     */
    @NotNull
    private Optional<PsiField> resolveReferenceField(@NotNull PsiField current, @NotNull String fieldName) {
        PsiType type = current.getType();
        // 引用类型，而且不包含可变参数
        if (type instanceof PsiClassReferenceType && !((PsiClassReferenceType) type).hasParameters()) {
            PsiClass clazz = ((PsiClassReferenceType) type).resolve();
            if (null != clazz) {
                return JavaUtils.findSettablePsiField(clazz, fieldName);
            }
        }
        return Optional.empty();
    }
}
