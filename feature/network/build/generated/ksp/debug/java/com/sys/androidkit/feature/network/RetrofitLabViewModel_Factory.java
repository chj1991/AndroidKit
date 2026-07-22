package com.sys.androidkit.feature.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
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
public final class RetrofitLabViewModel_Factory implements Factory<RetrofitLabViewModel> {
  private final Provider<PostApi> apiProvider;

  private RetrofitLabViewModel_Factory(Provider<PostApi> apiProvider) {
    this.apiProvider = apiProvider;
  }

  @Override
  public RetrofitLabViewModel get() {
    return newInstance(apiProvider.get());
  }

  public static RetrofitLabViewModel_Factory create(Provider<PostApi> apiProvider) {
    return new RetrofitLabViewModel_Factory(apiProvider);
  }

  public static RetrofitLabViewModel newInstance(PostApi api) {
    return new RetrofitLabViewModel(api);
  }
}
