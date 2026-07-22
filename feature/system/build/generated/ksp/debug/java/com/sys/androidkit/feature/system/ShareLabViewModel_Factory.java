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
public final class ShareLabViewModel_Factory implements Factory<ShareLabViewModel> {
  @Override
  public ShareLabViewModel get() {
    return newInstance();
  }

  public static ShareLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ShareLabViewModel newInstance() {
    return new ShareLabViewModel();
  }

  private static final class InstanceHolder {
    static final ShareLabViewModel_Factory INSTANCE = new ShareLabViewModel_Factory();
  }
}
