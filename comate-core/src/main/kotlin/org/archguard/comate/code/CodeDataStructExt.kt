package org.archguard.comate.code

import chapi.domain.core.CodeDataStruct

fun CodeDataStruct.Companion.generatePackageDependencies(codeDataStructs: List<CodeDataStruct>): Map<String, List<String>> {
    val packageList = codeDataStructs.map { it.Package }.distinct()
    val packageInOut = mutableMapOf<String, List<String>>()

    codeDataStructs.forEach {
        val packageIn = it.Package
        if (packageIn.isEmpty()) return@forEach

        packageInOut[packageIn] = packageInOut[packageIn].orEmpty().toMutableList().apply {
            val elements = it.Imports.map { import -> import.Source }
                .map { source -> source.substringBeforeLast(".") }
                .filter { source -> packageList.contains(source) }

            elements.forEach { element ->
                if (!this.contains(element)) {
                    this.add(element)
                }
            }
        }
    }

    return packageInOut.filter { it.value.isNotEmpty() }
}

fun CodeDataStruct.Companion.packageInString(codeDataStructs: List<CodeDataStruct>): String {
    val packageInOut = this.generatePackageDependencies(codeDataStructs)
    return """package fan in: $packageInOut"""
}