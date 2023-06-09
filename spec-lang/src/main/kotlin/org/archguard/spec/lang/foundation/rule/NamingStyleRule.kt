package org.archguard.spec.lang.foundation.rule

import org.archguard.architecture.style.NamingStyle
import org.archguard.spec.base.Rule
import org.archguard.spec.base.RuleResult
import org.archguard.spec.lang.foundation.declaration.NamingTarget
import org.archguard.spec.lang.foundation.expression.NamingExpression
import org.archguard.spec.element.FoundationElement
import org.jetbrains.annotations.TestOnly

class NamingStyleRule(val target: NamingTarget) : Rule<FoundationElement> {
    override val actionName: String get() = "Naming for " + target.name
    private var filterPattern: Regex = Regex(".*")
    private var namingRule: NamingRule? = null
    private var namingStyle = NamingStyle.CamelCase

    fun style(style: String) {
        if (!NamingStyle.contains(style)) {
            throw IllegalArgumentException("Unknown naming style: $style. Supported styles: ${NamingStyle.valuesString()}")
        }

        namingStyle = NamingStyle.valueOf(style)
    }

    /**
     * for filter element by regex
     */
    fun pattern(pattern: String, block: NamingExpression) {
        this.filterPattern = Regex(pattern)
        val namingRule = NamingRule()
        namingRule.delayBlock(block)
        this.namingRule = namingRule
    }

    override fun exec(input: FoundationElement): List<RuleResult> {
        val results = mutableListOf<RuleResult>()

        results += verifyNodeName(input)

        if (namingRule != null) {
            input.ds
                .filter {
                    filterPattern.matches(it.NodeName)
                }
                .map {
                    namingRule!!.exec(it.NodeName)
                }
                .flatten().forEach {
                    results.add(it)
                }
        }

        return results
    }

    private fun verifyNodeName(input: FoundationElement): List<RuleResult> {
        return input.ds.map {
            val rule = "Level: $target, NamingStyle: $namingStyle"

            when (target) {
                NamingTarget.Package -> {
                    listOf(RuleResult(actionName, rule, namingStyle.isValid(it.Package), it.Package))
                }

                NamingTarget.Class -> {
                    listOf(RuleResult(actionName, rule, namingStyle.isValid(it.NodeName), it.NodeName))
                }

                NamingTarget.Function -> {
                    it.Functions.map {
                        RuleResult(actionName, rule, namingStyle.isValid(it.Name), it.Name)
                    }
                }
            }
        }.flatten()
    }

    override fun toString(): String {

        return """
                ${target.name.lowercase()}_level {
                    style("${namingStyle.name}")
                    pattern("${filterPattern.pattern}") { ${namingRule} }
                }
            """.trimIndent()
    }

}

@TestOnly
fun naming_style_t(block: NamingStyleRule.() -> Unit, namingTarget: NamingTarget): NamingStyleRule {
    val namingStyleRule = NamingStyleRule(namingTarget)
    namingStyleRule.block()
    return namingStyleRule
}