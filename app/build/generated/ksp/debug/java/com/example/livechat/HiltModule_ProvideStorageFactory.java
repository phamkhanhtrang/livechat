package com.example.livechat;

import com.google.firebase.storage.FirebaseStorage;
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
public final class HiltModule_ProvideStorageFactory implements Factory<FirebaseStorage> {
  private final HiltModule module;

  public HiltModule_ProvideStorageFactory(HiltModule module) {
    this.module = module;
  }

  @Override
  public FirebaseStorage get() {
    return provideStorage(module);
  }

  public static HiltModule_ProvideStorageFactory create(HiltModule module) {
    return new HiltModule_ProvideStorageFactory(module);
  }

  public static FirebaseStorage provideStorage(HiltModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideStorage());
  }
}
