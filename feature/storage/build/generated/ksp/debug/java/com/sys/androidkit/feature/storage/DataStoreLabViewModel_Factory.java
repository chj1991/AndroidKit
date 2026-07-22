package com.sys.androidkit.feature.storage;

import com.sys.androidkit.core.datastore.AppPreferences;
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
public final class DataStoreLabViewModel_Factory implements Factory<DataStoreLabViewModel> {
  private final Provider<AppPreferences> prefsProvider;

  private DataStoreLabViewModel_Factory(Provider<AppPreferences> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public DataStoreLabViewModel get() {
    return newInstance(prefsProvider.get());
  }

  public static DataStoreLabViewModel_Factory create(Provider<AppPreferences> prefsProvider) {
    return new DataStoreLabViewModel_Factory(prefsProvider);
  }

  public static DataStoreLabViewModel newInstance(AppPreferences prefs) {
    return new DataStoreLabViewModel(prefs);
  }
}
