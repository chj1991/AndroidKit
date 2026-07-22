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
public final class ImageLabViewModel_Factory implements Factory<ImageLabViewModel> {
  @Override
  public ImageLabViewModel get() {
    return newInstance();
  }

  public static ImageLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ImageLabViewModel newInstance() {
    return new ImageLabViewModel();
  }

  private static final class InstanceHolder {
    static final ImageLabViewModel_Factory INSTANCE = new ImageLabViewModel_Factory();
  }
}
