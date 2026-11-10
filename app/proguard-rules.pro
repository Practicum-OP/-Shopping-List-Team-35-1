# Правила R8 для release-сборки.
# Retrofit, OkHttp, kotlinx.serialization, Room, Koin и Navigation поставляют
# свои consumer-правила, поэтому здесь только специфичное для проекта.

# Сохраняем номера строк в стектрейсах (деобфускация через mapping.txt).
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Сетевые DTO сериализуются kotlinx.serialization — имена полей должны совпадать с JSON.
-keep @kotlinx.serialization.Serializable class com.example.shoppinglist.**.dto.** { *; }
