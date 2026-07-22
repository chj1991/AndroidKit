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
public final class PermissionLabViewModel_Factory implements Factory<PermissionLabViewModel> {
  @Override
  public PermissionLabViewModel get() {
    return newInstance();
  }

  public static PermissionLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PermissionLabViewModel newInstance() {
    return new PermissionLabViewModel();
  }

  private static final class InstanceHolder {
    static final PermissionLabViewModel_Factory INSTANCE = new PermissionLabViewModel_Factory();
  }
}
