# Add project specific ProGuard rules here.
# Keep Room entities
-keep class com.radhavallabh.naamsmaran.data.local.entity.** { *; }

# Keep Hilt generated code
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
