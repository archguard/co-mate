package org.archguard.comate.dynamic.functions

import org.archguard.action.checkout.GitCommandManager
import org.archguard.action.checkout.GitSourceSettings
import org.archguard.action.checkout.doCheckout
import org.archguard.action.io.FileExt.mkdir
import org.archguard.comate.command.ComateContext
import java.io.File
import kotlin.io.path.Path

@ComateFunction
class InitializeSystemFunction(override val context: ComateContext) : DyFunction {
    override val hidden: Boolean get() = true

    override fun explain(): String {
        return "Initialize system will clone the repository and setup it."
    }

    override fun execute(): FunctionResult<Boolean> {
        val settings = GitSourceSettings(context.projectRepo)
        // mkdir temp
        mkdir("temp")
        val workingDirectory = File("temp", settings.repositoryPath)

        mkdir(workingDirectory.toString())
        val git = GitCommandManager(workingDirectory.toString())

        doCheckout(git, settings)
        context.workdir = Path(workingDirectory.absolutePath.toString())
        return FunctionResult.Success(true)
    }

    override fun parameters(): HashMap<String, String> {
        return hashMapOf()
    }
}