package com.maybe.plugin.mybatis.dom.converter;

import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * Description:
 * ResolvingConverter的适配器，提供默认实现方法，具体减少实现类的无用代码
 *
 * @author damon4u
 * @version 2018-10-26 11:16
 */
public abstract class ConverterAdaptor<T> extends ResolvingConverter<T> {

    @NotNull
    @Override
    public Collection<? extends T> getVariants(ConvertContext context) {
        return Collections.emptyList();
    }

    @Nullable
    @Override
    public T fromString(@Nullable String s, ConvertContext context) {
        return null;
    }

    @Nullable
    @Override
    public String toString(@Nullable T t, ConvertContext context) {
        return null;
    }
}
