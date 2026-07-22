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
public final class StructuredConcurrencyLabViewModel_Factory implements Factory<StructuredConcurrencyLabViewModel> {
  @Override
  public StructuredConcurrencyLabViewModel get() {
    return newInstance();
  }

  public static StructuredConcurrencyLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static StructuredConcurrencyLabViewModel newInstance() {
    return new StructuredConcurrencyLabViewModel();
  }

  private static final class InstanceHolder {
    static final StructuredConcurrencyLabViewModel_Factory INSTANCE = new StructuredConcurrencyLabViewModel_Factory();
  }
}
