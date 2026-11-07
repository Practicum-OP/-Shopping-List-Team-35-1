package com.example.shoppinglist.core.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `item_history` (
                `normalizedName` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `lastUsedAt` INTEGER NOT NULL,
                PRIMARY KEY(`normalizedName`)
            )
            """.trimIndent(),
        )
        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS `index_item_history_lastUsedAt` 
            ON `item_history` (`lastUsedAt`)
            """.trimIndent(),
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE INDEX IF NOT EXISTS `index_shopping_items_listId_position` 
            ON `shopping_items` (`listId`, `position`)
            """.trimIndent(),
        )
    }
}

val ALL_MIGRATIONS = arrayOf(
    MIGRATION_1_2,
    MIGRATION_2_3,
)
