package com.hss01248.hss_atest_plungin;

import com.android.build.api.transform.QualifiedContent;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.build.gradle.internal.scope.VariantScope;
import com.ss.android.ugc.bytex.common.CommonPlugin;
import com.ss.android.ugc.bytex.common.TransformConfiguration;
import com.ss.android.ugc.bytex.common.visitor.ClassVisitorChain;
import com.ss.android.ugc.bytex.pluginconfig.anno.PluginConfig;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Project;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @Despciption todo
 * @Author hss
 * @Date 03/08/2022 18:55
 * @Version 1.0
 */
//apply plugin: 'bytex.sourcefile2'
//@PluginConfig("bytex.sourcefile2")
public class SourceFileKillerPlugin2 extends CommonPlugin<SourceFileExtension, SourceFileContext> {

    SourceFileContext mContext;
    @Override
    protected SourceFileContext getContext(Project project, AppExtension android, SourceFileExtension extension) {
        mContext  =  new SourceFileContext(project, android, extension);
        return mContext;
    }

    @Override
    public void traverse(@Nonnull String relativePath, @Nonnull ClassVisitorChain chain) {
        super.traverse(relativePath, chain);
    }

    @Override
    public boolean transform(@Nonnull String relativePath, @Nonnull ClassVisitorChain chain) {
        //我们需要修改字节码,所以需要注册一个ClassVisitor
        //We need to modify the bytecode, so we need to register a ClassVisitor
        chain.connect(new SourceFileClassVisitor(extension,mContext));
        //mContext.getLogger().i("transform ----> "+ relativePath);
        return true;
    }

    @Override
    public boolean transform(@Nonnull String relativePath, @Nonnull ClassNode node) {
        if(node.interfaces != null && !node.interfaces.isEmpty()){
            for (String anInterface : node.interfaces) {
                //只包含声明在当前类上的,不包含其子类,子接口继承的
                if(anInterface.contains("Runnable")){
                    mContext.getLogger().i(node.name+": has impl--> Runnable : "+anInterface);
                }else {
                    //mContext.getLogger().i(node.name+": has impl: "+anInterface);
                }
            }
        }
        //获取父类
        //node.nestHostClass

        //node.permittedSubclasses




        //runtime注解
        if(node.visibleAnnotations != null && !node.visibleAnnotations.isEmpty()){
            for (AnnotationNode annotationNode : node.visibleAnnotations) {
                String desc = annotationNode.desc;
                //mContext.getLogger().i(node.name+": has anno-0: "+desc+","+annotationNode.values);
                if(desc != null && desc.contains("Keep")){
                    mContext.getLogger().i(node.name+": has anno: "+desc+","+annotationNode.values);
                }
            }
        }

        //class期注解
        if(node.invisibleAnnotations != null && !node.invisibleAnnotations.isEmpty()){
            for (AnnotationNode annotationNode : node.invisibleAnnotations) {
                String desc = annotationNode.desc;
               // mContext.getLogger().i(node.name+": invisible has anno-0: "+desc+","+annotationNode.values);
                if(desc != null && desc.contains("Keep")){
                    mContext.getLogger().i(node.name+": invisible has anno: "+desc+","+annotationNode.values);
                    //com/hss01248/aop_bytex_demo/Test: invisible has anno: Landroidx/annotation/Keep;,null
                }
            }
        }

        if(node.visibleTypeAnnotations != null && !node.visibleTypeAnnotations.isEmpty()){
            for (AnnotationNode annotationNode : node.visibleTypeAnnotations) {
                String desc = annotationNode.desc;
                mContext.getLogger().i(node.name+": visibleType has anno-0: "+desc+","+annotationNode.values);
                if(desc != null && desc.contains("Keep")){
                    mContext.getLogger().i(node.name+": visibleType has anno: "+desc+","+annotationNode.values);
                }
            }
        }

        if(node.invisibleTypeAnnotations != null && !node.invisibleTypeAnnotations.isEmpty()){
            for (AnnotationNode annotationNode : node.invisibleTypeAnnotations) {
                String desc = annotationNode.desc;
                mContext.getLogger().i(node.name+": invisibleType has anno-0: "+desc+","+annotationNode.values);
                if(desc != null && desc.contains("Keep")){
                    mContext.getLogger().i(node.name+": invisibleType has anno: "+desc+","+annotationNode.values);
                }
            }
        }

        return super.transform(relativePath, node);
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

            @Override
            public Set<QualifiedContent.ContentType> getInputTypes() {
                return TransformManager.CONTENT_JARS;
            }

            @Override
            public Set<? super QualifiedContent.Scope> getScopes(@Nullable VariantScope variantScope) {
                return TransformManager.SCOPE_FULL_PROJECT_WITH_LOCAL_JARS;
            }
        };
    }
}