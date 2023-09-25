import jetbrains.buildServer.configs.kotlin.Project


//
object MainProject : Project({

    // set 'Build' type for Main Project
    buildType(Build)

    // set 'ProjectFeatures' type for Main Project
    features {
        feature(ProjectFeatures.features.items[0])
        feature(ProjectFeatures.features.items[1])
    }
})
