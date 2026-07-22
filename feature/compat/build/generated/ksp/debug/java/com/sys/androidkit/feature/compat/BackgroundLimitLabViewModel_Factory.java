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
public final class BackgroundLimitLabViewModel_Factory implements Factory<BackgroundLimitLabViewModel> {
  private final Provider<Context> contextProvider;

  private BackgroundLimitLabViewModel_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public BackgroundLimitLabViewModel get() {
    return newInstance(contextProvider.get());
  }

  public static BackgroundLimitLabViewModel_Factory create(Provider<Context> contextProvider) {
    return new BackgroundLimitLabViewModel_Factory(contextProvider);
  }

  public static BackgroundLimitLabViewModel newInstance(Context context) {
    return new BackgroundLimitLabViewModel(context);
  }
}
