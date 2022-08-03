package com.hss01248.hss_atest_plungin;

import com.ss.android.ugc.bytex.common.CommonPlugin;
import com.ss.android.ugc.bytex.common.TransformConfiguration;
import com.ss.android.ugc.bytex.common.visitor.ClassVisitorChain;
import com.ss.android.ugc.bytex.pluginconfig.anno.PluginConfig;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Project;

import javax.annotation.Nonnull;

/**
 * @Despciption todo
 * @Author hss
 * @Date 03/08/2022 18:55
 * @Version 1.0
 */
//apply plugin: 'bytex.sourcefile2'
//@PluginConfig("bytex.sourcefile2")
public class SourceFileKillerPlugin2 extends CommonPlugin<SourceFileExtension, SourceFileContext> {
    @Override
    protected SourceFileContext getContext(Project project, AppExtension android, SourceFileExtension extension) {
        return new SourceFileContext(project, android, extension);
    }

    @Override
    public boolean transform(@Nonnull String relativePath, @Nonnull ClassVisitorChain chain) {
        //我们需要修改字节码,所以需要注册一个ClassVisitor
        //We need to modify the bytecode, so we need to register a ClassVisitor
        chain.connect(new SourceFileClassVisitor(extension));
        return super.transform(relativePath, chain);
    }

    @Nonnull
    @Override
    public TransformConfiguration transformConfiguration() {
        return new TransformConfiguration() {
            @Override
            public boolean isIncremental() {
                //插件默认是增量的,如果插件不支持增量,需要返回false
                //The plugin is incremental by default.It should return false if incremental is not supported by the plugin
                return true;
            }
        };
    }
}