package com.sys.androidkit.feature.image;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class CustomImagePickerSelectViewModel_Factory implements Factory<CustomImagePickerSelectViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private CustomImagePickerSelectViewModel_Factory(Provider<Context> contextProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.contextProvider = contextProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public CustomImagePickerSelectViewModel get() {
    return newInstance(contextProvider.get(), savedStateHandleProvider.get());
  }

  public static CustomImagePickerSelectViewModel_Factory create(Provider<Context> contextProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new CustomImagePickerSelectViewModel_Factory(contextProvider, savedStateHandleProvider);
  }

  public static CustomImagePickerSelectViewModel newInstance(Context context,
      SavedStateHandle savedStateHandle) {
    return new CustomImagePickerSelectViewModel(context, savedStateHandle);
  }
}
