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
public final class LeakLabViewModel_Factory implements Factory<LeakLabViewModel> {
  @Override
  public LeakLabViewModel get() {
    return newInstance();
  }

  public static LeakLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static LeakLabViewModel newInstance() {
    return new LeakLabViewModel();
  }

  private static final class InstanceHolder {
    static final LeakLabViewModel_Factory INSTANCE = new LeakLabViewModel_Factory();
  }
}
