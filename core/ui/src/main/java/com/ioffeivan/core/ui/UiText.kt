package com.ioffeivan.core.ui

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

/**
 * A sealed class representing text that can be displayed on the UI,
 * abstracting the source of the string (either a literal string or a resource ID).
 */
sealed class UiText {
    /**
     * Represents a hardcoded, literal string value.
     * @param value Literal string value
     */
    data class StaticString(val value: String) : UiText()

    /**
     * Represents a string resource ID, potentially with format arguments.
     * @param id The Android string resource ID.
     * @param args Arguments to be used for string formatting.
     */
    class StringResource(
        @param:StringRes val id: Int,
        vararg val args: Any,
    ) : UiText() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StringResource

            if (id != other.id) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + args.contentHashCode()

            return result
        }
    }

    /**
     * Represents a plural string resource ID, enabling quantity-based selection (one, few, many, etc.)
     * with optional format arguments.
     * @param id The Android string resource ID.
     * @param quantity The numeric value used to determine which plural form to select.
     * @param args Arguments to be used for string formatting within the selected plural form.
     */
    class PluralResource(
        @param:PluralsRes val id: Int,
        val quantity: Int,
        vararg val args: Any,
    ) : UiText() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as PluralResource

            if (id != other.id) return false
            if (quantity != other.quantity) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id
            result = 31 * result + quantity
            result = 31 * result + args.contentHashCode()

            return result
        }
    }

    /**
     * Resolves the UiText into a displayable string within a [Composable] context.
     * @return The final string resolved from its source.
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is StaticString -> value
            is StringResource -> stringResource(id, *args)
            is PluralResource -> pluralStringResource(id, quantity, *args)
        }
    }

    /**
     * Resolves the [UiText] into a displayable string using an explicit [Context].
     * Used outside of Composition.
     * @param context The Android Context used to access resources.
     * @return The final string resolved from its source.
     */
    fun asString(context: Context): String {
        return when (this) {
            is StaticString -> value
            is StringResource -> context.getString(id, *args)
            is PluralResource -> context.resources.getQuantityString(id, quantity, *args)
        }
    }
}
