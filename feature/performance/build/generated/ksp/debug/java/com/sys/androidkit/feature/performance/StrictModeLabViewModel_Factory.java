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
public final class StrictModeLabViewModel_Factory implements Factory<StrictModeLabViewModel> {
  @Override
  public StrictModeLabViewModel get() {
    return newInstance();
  }

  public static StrictModeLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static StrictModeLabViewModel newInstance() {
    return new StrictModeLabViewModel();
  }

  private static final class InstanceHolder {
    static final StrictModeLabViewModel_Factory INSTANCE = new StrictModeLabViewModel_Factory();
  }
}
