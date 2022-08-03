package com.hss01248.hss_atest_plungin;

import com.android.build.gradle.AppExtension;
import com.ss.android.ugc.bytex.common.BaseContext;

import org.gradle.api.Project;

public class SourceFileContext extends BaseContext<SourceFileExtension> {
    public SourceFileContext(Project project, AppExtension android, SourceFileExtension extension) {
        super(project, android, extension);
    }
}
