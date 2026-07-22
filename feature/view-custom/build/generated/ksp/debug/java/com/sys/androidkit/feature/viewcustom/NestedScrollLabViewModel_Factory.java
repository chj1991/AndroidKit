package com.sys.androidkit.feature.viewcustom;

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
public final class NestedScrollLabViewModel_Factory implements Factory<NestedScrollLabViewModel> {
  @Override
  public NestedScrollLabViewModel get() {
    return newInstance();
  }

  public static NestedScrollLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NestedScrollLabViewModel newInstance() {
    return new NestedScrollLabViewModel();
  }

  private static final class InstanceHolder {
    static final NestedScrollLabViewModel_Factory INSTANCE = new NestedScrollLabViewModel_Factory();
  }
}
