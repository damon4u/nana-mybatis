package com.maybe.plugin.mybatis.dom.description;

import com.maybe.plugin.mybatis.dom.model.Mapper;
import com.maybe.plugin.mybatis.util.DomUtils;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Description:
 * 注册mapper文件Dom解析
 *
 * @author damon4u
 * @version 2018-10-18 18:34
 */
public class MapperDescription extends DomFileDescription<Mapper> {
    
    public MapperDescription() {
        super(Mapper.class, "mapper");
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
        return DomUtils.isMybatisFile(file);
    }
}
