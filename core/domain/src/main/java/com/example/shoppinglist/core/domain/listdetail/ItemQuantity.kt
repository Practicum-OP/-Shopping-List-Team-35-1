package com.example.shoppinglist.core.domain.listdetail

import java.math.BigDecimal

/**
 * Проверенное положительное количество товара. Значение нормализовано,
 * поэтому 1.50 и 1.5 считаются одним и тем же количеством.
 */
@ConsistentCopyVisibility
data class ItemQuantity private constructor(
    val value: BigDecimal,
) {
    override fun toString(): String = value.toPlainString()

    companion object {
        private val DECIMAL_FORMAT = Regex("""\d+([.,]\d+)?""")

        /**
         * Возвращает `null`, если строка не является положительным десятичным числом.
         */
        fun parse(raw: String): ItemQuantity? {
            val text = raw.trim()
            if (!DECIMAL_FORMAT.matches(text)) return null

            return BigDecimal(text.replace(',', '.'))
                .takeIf { value -> value.signum() > 0 }
                ?.let { value -> ItemQuantity(value.stripTrailingZeros()) }
        }
    }
}
