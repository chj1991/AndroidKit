package com.sys.androidkit.feature.samplecounter;

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
public final class CounterViewModel_Factory implements Factory<CounterViewModel> {
  @Override
  public CounterViewModel get() {
    return newInstance();
  }

  public static CounterViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static CounterViewModel newInstance() {
    return new CounterViewModel();
  }

  private static final class InstanceHolder {
    static final CounterViewModel_Factory INSTANCE = new CounterViewModel_Factory();
  }
}
