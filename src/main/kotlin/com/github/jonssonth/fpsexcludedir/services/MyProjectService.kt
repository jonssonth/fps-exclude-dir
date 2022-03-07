package com.github.jonssonth.fpsexcludedir.services

import com.intellij.openapi.project.Project
import com.github.jonssonth.fpsexcludedir.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
