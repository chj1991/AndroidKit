package com.sys.androidkit.feature.storage;

import com.sys.androidkit.core.database.NoteDao;
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
public final class RoomLabViewModel_Factory implements Factory<RoomLabViewModel> {
  private final Provider<NoteDao> noteDaoProvider;

  private RoomLabViewModel_Factory(Provider<NoteDao> noteDaoProvider) {
    this.noteDaoProvider = noteDaoProvider;
  }

  @Override
  public RoomLabViewModel get() {
    return newInstance(noteDaoProvider.get());
  }

  public static RoomLabViewModel_Factory create(Provider<NoteDao> noteDaoProvider) {
    return new RoomLabViewModel_Factory(noteDaoProvider);
  }

  public static RoomLabViewModel newInstance(NoteDao noteDao) {
    return new RoomLabViewModel(noteDao);
  }
}
