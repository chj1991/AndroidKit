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
public final class PhotoPickerLabViewModel_Factory implements Factory<PhotoPickerLabViewModel> {
  @Override
  public PhotoPickerLabViewModel get() {
    return newInstance();
  }

  public static PhotoPickerLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PhotoPickerLabViewModel newInstance() {
    return new PhotoPickerLabViewModel();
  }

  private static final class InstanceHolder {
    static final PhotoPickerLabViewModel_Factory INSTANCE = new PhotoPickerLabViewModel_Factory();
  }
}
