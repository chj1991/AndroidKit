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
public final class BaselineProfileLabViewModel_Factory implements Factory<BaselineProfileLabViewModel> {
  @Override
  public BaselineProfileLabViewModel get() {
    return newInstance();
  }

  public static BaselineProfileLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BaselineProfileLabViewModel newInstance() {
    return new BaselineProfileLabViewModel();
  }

  private static final class InstanceHolder {
    static final BaselineProfileLabViewModel_Factory INSTANCE = new BaselineProfileLabViewModel_Factory();
  }
}
