package com.sys.androidkit.feature.recycler;

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
public final class RecyclerLabViewModel_Factory implements Factory<RecyclerLabViewModel> {
  @Override
  public RecyclerLabViewModel get() {
    return newInstance();
  }

  public static RecyclerLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RecyclerLabViewModel newInstance() {
    return new RecyclerLabViewModel();
  }

  private static final class InstanceHolder {
    static final RecyclerLabViewModel_Factory INSTANCE = new RecyclerLabViewModel_Factory();
  }
}
