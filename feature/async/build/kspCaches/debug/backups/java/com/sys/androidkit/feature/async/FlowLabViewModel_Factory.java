package com.sys.androidkit.feature.async;

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
public final class FlowLabViewModel_Factory implements Factory<FlowLabViewModel> {
  @Override
  public FlowLabViewModel get() {
    return newInstance();
  }

  public static FlowLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FlowLabViewModel newInstance() {
    return new FlowLabViewModel();
  }

  private static final class InstanceHolder {
    static final FlowLabViewModel_Factory INSTANCE = new FlowLabViewModel_Factory();
  }
}
