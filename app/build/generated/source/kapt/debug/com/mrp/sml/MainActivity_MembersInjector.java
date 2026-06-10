package com.mrp.sml;

import com.mrp.sml.data.local.preferences.SettingsManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<SettingsManager> settingsManagerProvider;

  private MainActivity_MembersInjector(Provider<SettingsManager> settingsManagerProvider) {
    this.settingsManagerProvider = settingsManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<SettingsManager> settingsManagerProvider) {
    return new MainActivity_MembersInjector(settingsManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectSettingsManager(instance, settingsManagerProvider.get());
  }

  @InjectedFieldSignature("com.mrp.sml.MainActivity.settingsManager")
  public static void injectSettingsManager(MainActivity instance, SettingsManager settingsManager) {
    instance.settingsManager = settingsManager;
  }
}
