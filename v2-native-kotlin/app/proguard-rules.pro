# Add project specific ProGuard rules here.
# Keep Room entities
-keep class com.radhavallabh.naamsmaran.data.local.entity.** { *; }

# Keep Gson DTOs for assets/sant_smaran.json (parsed reflectively; release has R8 on).
# Field names must survive shrinking/obfuscation and generics need the Signature attribute.
-keepattributes Signature,*Annotation*
-keep class com.radhavallabh.naamsmaran.data.santsmaran.*Dto { *; }

# Keep Hilt generated code
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
