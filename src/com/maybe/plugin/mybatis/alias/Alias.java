package com.maybe.plugin.mybatis.alias;

import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Description:
 * 别名
 *
 * @author damon4u
 * @version 2018-11-01 18:17
 */
public class Alias {

    private PsiClass clazz;

    private String aliasValue;

    public Alias() {
    }

    public Alias(PsiClass clazz, String aliasValue) {
        this.clazz = clazz;
        this.aliasValue = aliasValue;
    }

    public static Alias getInstance(@NotNull PsiClass psiClass, @NotNull String aliasValue) {
        return new Alias(psiClass, aliasValue);
    }

    public PsiClass getClazz() {
        return clazz;
    }

    public void setClazz(PsiClass clazz) {
        this.clazz = clazz;
    }

    public String getAliasValue() {
        return aliasValue;
    }

    public void setAliasValue(String aliasValue) {
        this.aliasValue = aliasValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Alias aliasTmp = (Alias) o;

        if (this.aliasValue != null ? !aliasValue.equalsIgnoreCase(aliasTmp.aliasValue) : aliasTmp.aliasValue != null) {
            return false;
        }
        if (clazz != null ? !clazz.equals(aliasTmp.clazz) : aliasTmp.clazz != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, aliasValue);
    }
}
