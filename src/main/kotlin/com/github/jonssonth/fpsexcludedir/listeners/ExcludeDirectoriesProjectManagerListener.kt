package com.github.jonssonth.fpsexcludedir.listeners


import com.intellij.ProjectTopics
import com.intellij.ide.projectView.actions.MarkRootActionBase
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.roots.ModuleRootEvent
import com.intellij.openapi.roots.ModuleRootListener
import com.intellij.openapi.roots.ModuleRootModificationUtil
import com.intellij.openapi.vfs.VirtualFile


internal class ExcludeDirectoriesProjectManagerListener : ProjectManagerListener {



    override fun projectOpened(project: Project) {
        project.messageBus.connect(project).subscribe(ProjectTopics.PROJECT_ROOTS, object : ModuleRootListener {
            override fun rootsChanged(event: ModuleRootEvent) {
                markAngularGeneratedAsExcluded(project);
            }
        })
    }

    private fun markAngularGeneratedAsExcluded(project: Project) {
        val module = ModuleManager.getInstance(project).findModuleByName("fps-web-angular");
        if (module != null) {
            ModuleRootModificationUtil.updateModel(module) { model ->
                val virtualFile = getAngularGenerated(module);
                if (virtualFile != null) {
                    runReadAction {
                        MarkRootActionBase.findContentEntry(model, virtualFile)?.addExcludeFolder(virtualFile)
                    }
                }
            }
        }
    }

    private fun getAngularGenerated(module: com.intellij.openapi.module.Module): VirtualFile? {
        return module.moduleFile!!.parent.findFileByRelativePath("/target/angular-generated");
    }

}
