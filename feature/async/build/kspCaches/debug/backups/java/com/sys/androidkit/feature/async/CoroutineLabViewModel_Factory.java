package com.sys.androidkit.feature.async;

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
public final class CoroutineLabViewModel_Factory implements Factory<CoroutineLabViewModel> {
  @Override
  public CoroutineLabViewModel get() {
    return newInstance();
  }

  public static CoroutineLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CoroutineLabViewModel newInstance() {
    return new CoroutineLabViewModel();
  }

  private static final class InstanceHolder {
    static final CoroutineLabViewModel_Factory INSTANCE = new CoroutineLabViewModel_Factory();
  }
}
