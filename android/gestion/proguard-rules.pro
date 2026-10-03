# Le ViewModel est instancié par réflexion (AndroidViewModelFactory) :
# garder son constructeur (Application), au cas où les règles fournies par
# lifecycle ne suffiraient pas.
-keepclassmembers class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}
