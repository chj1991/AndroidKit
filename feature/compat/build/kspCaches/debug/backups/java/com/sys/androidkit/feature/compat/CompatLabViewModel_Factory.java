package com.sys.androidkit.feature.compat;

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
public final class CompatLabViewModel_Factory implements Factory<CompatLabViewModel> {
  @Override
  public CompatLabViewModel get() {
    return newInstance();
  }

  public static CompatLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CompatLabViewModel newInstance() {
    return new CompatLabViewModel();
  }

  private static final class InstanceHolder {
    static final CompatLabViewModel_Factory INSTANCE = new CompatLabViewModel_Factory();
  }
}
