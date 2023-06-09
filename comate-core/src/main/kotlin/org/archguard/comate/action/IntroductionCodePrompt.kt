package org.archguard.comate.action

import org.archguard.architecture.layered.ChannelType
import org.archguard.comate.code.dependencyMapping
import org.archguard.comate.command.ComateContext
import org.archguard.comate.strategy.CodePromptStrategy
import org.archguard.comate.strategy.Strategy
import org.archguard.scanner.core.sca.CompositionDependency

class IntroductionCodePrompt(
    val context: ComateContext,
    override val strategy: Strategy,
) : CodePromptStrategy {
    override fun getRole(): String = "Architecture"
    override fun getInstruction(): String = "根据分析如下的 dependencies 等信息，分析并编写这个项目的介绍。"
    override fun getRequirements(): String = """1. 从 {all channel types} 中选择最可能的 {channel type}，不做解释。
2. 根据 dependencies 分析这个应用的核心场景。
3. 根据 dependencies 分析这个应用要考虑的功能需求和非功能需求。
4. 你返回的介绍类似于："""

    override fun getSample(): String {
        return """
```
{xxx} 项目是一个 {channel type} 应用程序，使用了 {xxx} 和一系列相关的库来构建 {xxx} 等功能（介绍核心的框架）。
该应用还使用了一些第三方库来构建 {xxx} (诸如于用户界面)，以及进行 {xxx} 等任务（诸如于任务调度等功能）。该应用需要考虑 {xxx} 等非功能需求。
```""".trimIndent()
    }

    override fun getExtendData(): String {
        val dep = context.fetchProjectDependencies()
        val depMap: Map<String, List<String>> = CompositionDependency.dependencyMapping(dep, context.workdir)

        val instr = context.fetchReadmeIntroduction()

        val items = depMap.map { "| ${it.key} | ${it.value.joinToString(", ")} |" }.joinToString("\n")
        val channels = ChannelType.allValues()

        return instr + """
all channel types: $channels
                
dependencies: 

| path | deps |
| --- | --- |
$items
"""
    }
}