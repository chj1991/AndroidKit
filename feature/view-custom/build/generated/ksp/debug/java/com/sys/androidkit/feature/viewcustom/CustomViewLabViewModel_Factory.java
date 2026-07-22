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
public final class CustomViewLabViewModel_Factory implements Factory<CustomViewLabViewModel> {
  @Override
  public CustomViewLabViewModel get() {
    return newInstance();
  }

  public static CustomViewLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CustomViewLabViewModel newInstance() {
    return new CustomViewLabViewModel();
  }

  private static final class InstanceHolder {
    static final CustomViewLabViewModel_Factory INSTANCE = new CustomViewLabViewModel_Factory();
  }
}
