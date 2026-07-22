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
public final class CustomImagePickerLabViewModel_Factory implements Factory<CustomImagePickerLabViewModel> {
  @Override
  public CustomImagePickerLabViewModel get() {
    return newInstance();
  }

  public static CustomImagePickerLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CustomImagePickerLabViewModel newInstance() {
    return new CustomImagePickerLabViewModel();
  }

  private static final class InstanceHolder {
    static final CustomImagePickerLabViewModel_Factory INSTANCE = new CustomImagePickerLabViewModel_Factory();
  }
}
