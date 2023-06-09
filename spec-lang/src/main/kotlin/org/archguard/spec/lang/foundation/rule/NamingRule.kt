package org.archguard.spec.lang.foundation.rule

import org.archguard.spec.base.Rule
import org.archguard.spec.base.RuleResult
import org.archguard.spec.lang.foundation.expression.NamingExpression
import org.archguard.spec.lang.matcher.CompareType
import org.archguard.spec.lang.matcher.DelayCompare
import org.jetbrains.annotations.TestOnly


class NamingRule : Rule<String> {
    override val actionName: String = "Naming"
    val name: Any = "<placeholder>"
    var string: String = ""

    var delayCompare: DelayCompare? = null

    fun endsWith(vararg suffixes: String): DelayCompare {
        val compare = DelayCompare(string, CompareType.ENDS_WITH, suffixes.toList())
        this.delayCompare = compare
        return compare
    }

    fun startsWith(vararg symbols: String): DelayCompare {
        val compare = DelayCompare(string, CompareType.STARTS_WITH, symbols.toList())
        this.delayCompare = compare
        return compare
    }

    fun contains(vararg symbols: String): DelayCompare {
        val compare = DelayCompare(string, CompareType.CONTAINS, symbols.toList())
        this.delayCompare = compare
        return compare
    }

    fun delayBlock(block: NamingExpression) {
        block(this)
    }

    override fun exec(input: String): List<RuleResult> {
        this.delayCompare!!.left = input
        val compare = delayCompare!!.compare()
        return listOf(RuleResult(this.actionName, "Naming exec: $input, compareType: $delayCompare", compare, input))
    }

    override fun toString(): String {
        return """name${this.delayCompare}"""
    }
}

@TestOnly
fun naming(block: NamingRule.() -> Unit): NamingRule {
    val namingRule = NamingRule()
    namingRule.block()
    return namingRule
}
