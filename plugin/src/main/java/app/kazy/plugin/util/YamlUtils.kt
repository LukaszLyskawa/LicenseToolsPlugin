package app.kazy.plugin.util

import app.kazy.plugin.data.ArtifactId
import app.kazy.plugin.data.LibraryInfo
import app.kazy.plugin.extension.loadYaml
import java.io.File

object YamlUtils {
    fun loadToLibraryInfo(file: File): Set<LibraryInfo> {
        if (!file.exists()) {
            return emptySet()
        }
        return file.loadYaml()
            .map {
                LibraryInfo(
                    artifactId = ArtifactId.parse(it["artifact"] as String?),
                    name = it["name"] as String? ?: "",
                    libraryName = it["name"] as String? ?: "",
                    fileName = it["name"] as String? ?: "",
                    license = it["license"] as String? ?: "",
                    copyrightHolder = makeCopyRightHolder(it),
                    notice = it["notice"] as String?,
                    url = it.getOrDefault("url", "") as String,
                    licenseUrl = it["licenseUrl"] as String?,
                    skip = it.getOrDefault("skip", "false").toString().toBoolean(),
                    forceGenerate = it.getOrDefault(
                        "forceGenerate",
                        "false"
                    ).toString().toBoolean()
                )
            }.toSet()
    }

    private fun makeCopyRightHolder(map: Map<String, Any>): String? {
        return when {
            map["copyrightHolder"] != null -> {
                map["copyrightHolder"] as String
            }
            map["copyrightHolders"] != null -> {
                @Suppress("UNCHECKED_CAST")
                joinWords(map["copyrightHolders"] as List<String>)
            }
            map["authors"] != null -> {
                @Suppress("UNCHECKED_CAST")
                joinWords(map["authors"] as List<String>)
            }
            map["author"] != null -> {
                map["author"] as String
            }
            else -> null
        }
    }

    private fun joinWords(words: List<String>): String {
        return when (words.size) {
            0 -> ""
            1 -> words.first()
            2 -> "${words.first()} and ${words.last()}"
            else -> {
                val last: String = words.last()
                "${words.subList(0, words.size - 1).joinToString(",")}, and $last"
            }
        }
    }
}