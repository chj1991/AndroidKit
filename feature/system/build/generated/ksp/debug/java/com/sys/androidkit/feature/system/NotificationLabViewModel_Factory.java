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
public final class NotificationLabViewModel_Factory implements Factory<NotificationLabViewModel> {
  @Override
  public NotificationLabViewModel get() {
    return newInstance();
  }

  public static NotificationLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NotificationLabViewModel newInstance() {
    return new NotificationLabViewModel();
  }

  private static final class InstanceHolder {
    static final NotificationLabViewModel_Factory INSTANCE = new NotificationLabViewModel_Factory();
  }
}
