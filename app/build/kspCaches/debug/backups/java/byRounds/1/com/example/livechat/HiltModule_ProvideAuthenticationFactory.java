package com.example.livechat;

import com.google.firebase.auth.FirebaseAuth;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class HiltModule_ProvideAuthenticationFactory implements Factory<FirebaseAuth> {
  private final HiltModule module;

  public HiltModule_ProvideAuthenticationFactory(HiltModule module) {
    this.module = module;
  }

  @Override
  public FirebaseAuth get() {
    return provideAuthentication(module);
  }

  public static HiltModule_ProvideAuthenticationFactory create(HiltModule module) {
    return new HiltModule_ProvideAuthenticationFactory(module);
  }

  public static FirebaseAuth provideAuthentication(HiltModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideAuthentication());
  }
}
