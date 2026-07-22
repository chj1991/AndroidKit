package com.sys.androidkit.feature.system;

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
public final class ForegroundServiceLabViewModel_Factory implements Factory<ForegroundServiceLabViewModel> {
  @Override
  public ForegroundServiceLabViewModel get() {
    return newInstance();
  }

  public static ForegroundServiceLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ForegroundServiceLabViewModel newInstance() {
    return new ForegroundServiceLabViewModel();
  }

  private static final class InstanceHolder {
    static final ForegroundServiceLabViewModel_Factory INSTANCE = new ForegroundServiceLabViewModel_Factory();
  }
}
