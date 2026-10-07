package sk.solver.weatherapp.utils

import java.text.Normalizer

/**
 * Removes diacritics and converts the text to lowercase for comparison.
 */
fun String.normalized(): String {
    return Normalizer
        .normalize(this, Normalizer.Form.NFD)
        .replace("\\p{M}+".toRegex(), "")
        .lowercase()
}