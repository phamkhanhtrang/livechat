package com.example.livechat;

import com.google.firebase.firestore.FirebaseFirestore;
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
public final class HiltModule_ProvideFirestoreFactory implements Factory<FirebaseFirestore> {
  private final HiltModule module;

  public HiltModule_ProvideFirestoreFactory(HiltModule module) {
    this.module = module;
  }

  @Override
  public FirebaseFirestore get() {
    return provideFirestore(module);
  }

  public static HiltModule_ProvideFirestoreFactory create(HiltModule module) {
    return new HiltModule_ProvideFirestoreFactory(module);
  }

  public static FirebaseFirestore provideFirestore(HiltModule instance) {
    return Preconditions.checkNotNullFromProvides(instance.provideFirestore());
  }
}
