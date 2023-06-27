package org.archguard.spec.lang

import org.archguard.spec.base.Rule
import org.archguard.spec.base.RuleResult
import org.archguard.spec.base.verifier.LlmRuleVerifier
import org.archguard.spec.lang.base.BaseDeclaration
import org.archguard.spec.lang.base.Spec

class ArchitectureSpec : Spec<String> {
    override fun setVerifier(ruleVerifier: LlmRuleVerifier) {
        TODO("Not yet implemented")
    }

    override fun default(): Spec<String> {
        TODO("Not yet implemented")
    }

    override fun exec(element: String): List<RuleResult> {
        TODO("Not yet implemented")
    }

    fun system(name: String, function: SystemDeclaration.() -> Unit): SystemDeclaration {
        val system = SystemDeclaration(name)
        system.function()
        return system
    }
}

class SystemDeclaration(name: String) : BaseDeclaration<String> {
    override fun rules(element: String): List<Rule<String>> {
        TODO("Not yet implemented")
    }

    fun component(name: String, function: ComponentDeclaration.() -> Unit): ComponentDeclaration {
        val component = ComponentDeclaration(name)
        component.function()
        return component
    }

    fun connection(pair: Pair<String, String>, function: () -> Unit?) {

    }

    fun connection(pair: Pair<String, String>) {

    }

}

class ComponentDeclaration(name: String) : BaseDeclaration<String> {
    override fun rules(element: String): List<Rule<String>> {
        return listOf()
    }
}

fun architecture(function: ArchitectureSpec.() -> Unit): ArchitectureSpec {
    val spec = ArchitectureSpec()
    spec.function()
    return spec
}