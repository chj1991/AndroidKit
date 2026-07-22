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
public final class LottieLabViewModel_Factory implements Factory<LottieLabViewModel> {
  @Override
  public LottieLabViewModel get() {
    return newInstance();
  }

  public static LottieLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static LottieLabViewModel newInstance() {
    return new LottieLabViewModel();
  }

  private static final class InstanceHolder {
    static final LottieLabViewModel_Factory INSTANCE = new LottieLabViewModel_Factory();
  }
}
