package com.example.processor

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*

@AutoService(SymbolProcessorProvider::class)
class AllPropertiesProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AllPropertiesProcessor(environment.logger)
    }
}

class AllPropertiesProcessor(private val logger: KSPLogger) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val classes = resolver
            .getSymbolsWithAnnotation("com.example.processor.InspectProperties")
            .filterIsInstance<KSClassDeclaration>()
        for (def in classes) {
            logger.warn("getAllProperties() for ${def.simpleName.asString()}")
            for (prop in def.getAllProperties()) {
                logger.warn(
                        "- ${prop.simpleName.asString()}: ${extract(prop.annotations)}",
                )
            }
        }
        return listOf()
    }

    private fun extract(annotations: Sequence<KSAnnotation>): String {
        val column = annotations
            .firstOrNull { it.shortName.asString() == "Column" }
            ?.arguments
            ?.firstOrNull { it.name?.asString() == "name" }
            ?.value as? String
        return column?.let { "Column($it)"} ?: "no annotation!"
    }
}
