package scripts

import jetbrains.buildServer.configs.kotlin.BuildStep
import jetbrains.buildServer.configs.kotlin.BuildSteps
import jetbrains.buildServer.configs.kotlin.buildSteps.script

object AllureReportScripts {
    // Extension function for BuildSteps
    fun BuildSteps.applyAllureReportScripts() {
        script {
            name = "Get Test Metrics from Allure Report"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            scriptContent = """
        PASSED_COUNT=${'$'}(awk '/launch_status_passed/ {print $2}' allure-report/export/prometheusData.txt)
        FAILED_COUNT=${'$'}(awk '/launch_status_failed/ {print $2}' allure-report/export/prometheusData.txt)
        SKIPPED_COUNT=${'$'}(awk '/launch_status_skipped/ {print $2}' allure-report/export/prometheusData.txt)
        DURATION=${'$'}(awk '/launch_time_duration/ {print $2}' allure-report/export/prometheusData.txt)
        echo "##teamcity[setParameter name='env.PASSED_TESTS' value='${'$'}PASSED_COUNT']"
        echo "##teamcity[setParameter name='env.FAILED_TESTS' value='${'$'}FAILED_COUNT']"
        echo "##teamcity[setParameter name='env.SKIPPED_TESTS' value='${'$'}SKIPPED_COUNT']"
        echo "##teamcity[setParameter name='env.DURATION' value='${'$'}DURATION']"
    """.trimIndent()
        }

        script {
            name = "Send Adaptive Card to Microsoft Teams as Allure report"
            executionMode = BuildStep.ExecutionMode.ALWAYS
            val payload = """
                {
  "@context": "http://schema.org/extensions",
  "@type": "MessageCard",
  "themeColor": "0076D7",
  "title": "Allure Report",
  "text": "This is a simplified report after the job is completed",
  "sections": [
    {
      "startGroup": true,
      "title": "**Information about build:**",
      "facts": [
        {
          "name": "Branch name",
          "value": "%teamcity.build.branch%"
        },
        {
          "name": "Build Number",
          "value": "%build.number%"
        },
        {
          "name": "Agent name",
          "value": "%teamcity.agent.name%"
        },
        {
        "name": "Triggered by user name",
        "value": "%teamcity.build.triggeredBy.username%"
        }
      ]
    },
    {
      "startGroup": true,
      "title": "**Information about tests:**",
      "facts": [
        {
        "name": "Passed",
        "value": "%env.PASSED_TESTS%"
        },
        {
        "name": "Failed",
        "value": "%env.FAILED_TESTS%"
        },
        {
        "name": "Skipped",
        "value": "%env.SKIPPED_TESTS%"
        },
        {
        "name": "Duration",
        "value": "%env.DURATION%"
        }
      ]
    }
  ],
  "potentialAction": [
    {
      "@type": "OpenUri",
      "name": "Open detailed report",
      "targets": [
        {
          "os": "default",
          "uri": "%allure_report_url%"
        }
      ]
    }
  ]
}
            """.trimIndent()
            scriptContent = """
        curl -H 'Content-Type: application/json' -d '$payload' %webhook_url%
    """.trimIndent()
        }
    }
}
