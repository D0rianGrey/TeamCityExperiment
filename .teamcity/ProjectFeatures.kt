import jetbrains.buildServer.configs.kotlin.Project
import jetbrains.buildServer.configs.kotlin.projectFeatures.buildReportTab

// Store all feature in 1 place
object ProjectFeatures : Project({
    features {
        feature {
            type = "allureReport"
            param("allureRunnerMode", "runner")
            param("allureReportGenerate", "true")
            param("allureResultsPattern", "allure-results/**")
        }

        buildReportTab {
            title = "Test Results"
            startPage = "allure-report/index.html"
        }
    }
})
