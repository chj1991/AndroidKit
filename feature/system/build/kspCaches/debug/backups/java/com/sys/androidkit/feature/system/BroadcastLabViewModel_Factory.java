package com.sys.androidkit.feature.system;

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
public final class BroadcastLabViewModel_Factory implements Factory<BroadcastLabViewModel> {
  @Override
  public BroadcastLabViewModel get() {
    return newInstance();
  }

  public static BroadcastLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BroadcastLabViewModel newInstance() {
    return new BroadcastLabViewModel();
  }

  private static final class InstanceHolder {
    static final BroadcastLabViewModel_Factory INSTANCE = new BroadcastLabViewModel_Factory();
  }
}
