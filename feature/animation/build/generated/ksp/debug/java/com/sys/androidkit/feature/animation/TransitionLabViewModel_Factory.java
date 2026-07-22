package com.sys.androidkit.feature.animation;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class TransitionLabViewModel_Factory implements Factory<TransitionLabViewModel> {
  @Override
  public TransitionLabViewModel get() {
    return newInstance();
  }

  public static TransitionLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TransitionLabViewModel newInstance() {
    return new TransitionLabViewModel();
  }

  private static final class InstanceHolder {
    static final TransitionLabViewModel_Factory INSTANCE = new TransitionLabViewModel_Factory();
  }
}
