package com.cheatshqip.adapter.output

import kotlinx.serialization.Serializable

@Serializable
data class RESTFuzzyMatch(
    val word: String,
    val word2: String,
    val word5: String,
    val wordOrig: String,
    val wordDefinition: String,
    val wordDer1: String,
    val wordDer2: String,
    val wordDer3: String,
    val wordDer4: String,
    val wordDer5: String,
    val wordDer6: String,
    val wordDer7: String,
    val wordDer8: String,
    val wordDer9: String,
    val derivedWordMatch: Boolean,
    val exactMatch: Boolean,
    val canonicalWord: String,
    val verb: String?,
    val antonyms: List<String>,
    val synonyms: List<String>
)
