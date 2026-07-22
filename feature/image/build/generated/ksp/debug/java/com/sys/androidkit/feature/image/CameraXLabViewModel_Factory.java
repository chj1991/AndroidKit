package com.sys.androidkit.feature.image;

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
public final class CameraXLabViewModel_Factory implements Factory<CameraXLabViewModel> {
  @Override
  public CameraXLabViewModel get() {
    return newInstance();
  }

  public static CameraXLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CameraXLabViewModel newInstance() {
    return new CameraXLabViewModel();
  }

  private static final class InstanceHolder {
    static final CameraXLabViewModel_Factory INSTANCE = new CameraXLabViewModel_Factory();
  }
}
