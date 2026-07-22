package com.sys.androidkit.feature.compat;

import android.content.Context;
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
public final class ScopedStorageLabViewModel_Factory implements Factory<ScopedStorageLabViewModel> {
  private final Provider<Context> contextProvider;

  private ScopedStorageLabViewModel_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public ScopedStorageLabViewModel get() {
    return newInstance(contextProvider.get());
  }

  public static ScopedStorageLabViewModel_Factory create(Provider<Context> contextProvider) {
    return new ScopedStorageLabViewModel_Factory(contextProvider);
  }

  public static ScopedStorageLabViewModel newInstance(Context context) {
    return new ScopedStorageLabViewModel(context);
  }
}
