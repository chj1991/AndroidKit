package com.sys.androidkit.feature.animation;

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
public final class MotionLabViewModel_Factory implements Factory<MotionLabViewModel> {
  @Override
  public MotionLabViewModel get() {
    return newInstance();
  }

  public static MotionLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MotionLabViewModel newInstance() {
    return new MotionLabViewModel();
  }

  private static final class InstanceHolder {
    static final MotionLabViewModel_Factory INSTANCE = new MotionLabViewModel_Factory();
  }
}
