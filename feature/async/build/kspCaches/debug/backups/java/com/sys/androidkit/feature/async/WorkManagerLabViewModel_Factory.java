package com.sys.androidkit.feature.async;

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
public final class WorkManagerLabViewModel_Factory implements Factory<WorkManagerLabViewModel> {
  private final Provider<Context> contextProvider;

  private WorkManagerLabViewModel_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public WorkManagerLabViewModel get() {
    return newInstance(contextProvider.get());
  }

  public static WorkManagerLabViewModel_Factory create(Provider<Context> contextProvider) {
    return new WorkManagerLabViewModel_Factory(contextProvider);
  }

  public static WorkManagerLabViewModel newInstance(Context context) {
    return new WorkManagerLabViewModel(context);
  }
}
