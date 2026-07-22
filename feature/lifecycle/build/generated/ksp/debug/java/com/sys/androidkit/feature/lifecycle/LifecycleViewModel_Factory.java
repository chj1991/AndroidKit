package com.sys.androidkit.feature.lifecycle;

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
public final class LifecycleViewModel_Factory implements Factory<LifecycleViewModel> {
  @Override
  public LifecycleViewModel get() {
    return newInstance();
  }

  public static LifecycleViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static LifecycleViewModel newInstance() {
    return new LifecycleViewModel();
  }

  private static final class InstanceHolder {
    static final LifecycleViewModel_Factory INSTANCE = new LifecycleViewModel_Factory();
  }
}
