package com.sys.androidkit.feature.performance;

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
public final class StartupLabViewModel_Factory implements Factory<StartupLabViewModel> {
  @Override
  public StartupLabViewModel get() {
    return newInstance();
  }

  public static StartupLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static StartupLabViewModel newInstance() {
    return new StartupLabViewModel();
  }

  private static final class InstanceHolder {
    static final StartupLabViewModel_Factory INSTANCE = new StartupLabViewModel_Factory();
  }
}
