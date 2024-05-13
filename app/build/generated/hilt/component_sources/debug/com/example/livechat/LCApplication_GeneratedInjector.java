package com.example.livechat;

import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedEntryPoint;

@OriginatingElement(
    topLevelClass = LCApplication.class
)
@GeneratedEntryPoint
@InstallIn(SingletonComponent.class)
public interface LCApplication_GeneratedInjector {
  void injectLCApplication(LCApplication lCApplication);
}
