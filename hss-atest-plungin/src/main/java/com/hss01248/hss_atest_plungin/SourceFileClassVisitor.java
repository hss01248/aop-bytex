package com.hss01248.hss_atest_plungin;

import com.ss.android.ugc.bytex.common.Constants;
import com.ss.android.ugc.bytex.common.visitor.BaseClassVisitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class SourceFileClassVisitor extends BaseClassVisitor {
    private SourceFileExtension extension;
    SourceFileContext mContext;

    public SourceFileClassVisitor(SourceFileExtension extension, SourceFileContext mContext) {
        this.extension = extension;
        this.mContext = mContext;
    }

    @Override
    public void visitSource(String source, String debug) {
        if (extension.isDeleteSourceFile()) {
            // delete
            return;
        }
        super.visitSource(source, debug);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        mContext.getLogger().i("xxxmethod ----> "+ name+" , "+ signature);
        if (extension.isDeleteLineNumber()) {
            return new MethodVisitor(Constants.ASM_API, mv) {
                @Override
                public void visitLineNumber(int line, Label start) {
                    //delete
                }

                @Override
                public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                    return super.visitAnnotation(descriptor, visible);
                }
            };
        }
        return mv;
    }
}
