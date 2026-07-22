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
public final class AnimationLabViewModel_Factory implements Factory<AnimationLabViewModel> {
  @Override
  public AnimationLabViewModel get() {
    return newInstance();
  }

  public static AnimationLabViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AnimationLabViewModel newInstance() {
    return new AnimationLabViewModel();
  }

  private static final class InstanceHolder {
    static final AnimationLabViewModel_Factory INSTANCE = new AnimationLabViewModel_Factory();
  }
}
