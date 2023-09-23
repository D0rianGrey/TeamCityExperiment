package jetbrains.buildServer.configs.kotlin.v2018_1.triggers

import jetbrains.buildServer.configs.kotlin.v2018_1.*

/**
 * Perforce Shelve Trigger queues a build when a shelved changelist is created/updated in
 * the VCS Root of the build configuration.
 *
 * **Example.**
 * Configure a Perforce Shelve Trigger which runs a personal build when a new or modified shelved changelist is detected.
 * The shelved changelist must have #review keyword in its description.
 * The build will enforce clean checkout, and will have an extra `inShelve=true` configuration parameter.
 * ```
 * object Docker : BuildType({
 *     name = "Test build for shelves"
 *     ...
 *     triggers {
 *         perforceShelveTrigger {
 *             keyword = "#review"
 *
 *             enforceCleanCheckout = true
 *             buildParams {
 *                 param("inShelve", "true")
 *             }
 *         }
 *     }
 * })
 * ```
 *
 *
 * @see perforceShelveTrigger
 */
open class PerforceShelveTrigger() : Trigger() {

    init {
        type = "perforceShelveTrigger"
    }

    constructor(init: PerforceShelveTrigger.() -> Unit): this() {
        init()
    }

    /**
     * The keyword which should be present in Perforce shelved changelist description to trigger the build.
     * Default keyword is `#teamcity`
     */
    var keyword by stringParameter("clDescriptionKeyword")

    override fun validate(consumer: ErrorConsumer) {
        super.validate(consumer)
    }
}


/**
 * Adds [Perforce Build Trigger](https://www.jetbrains.com/help/teamcity/?Perforce+Shelve+Trigger) to build configuration or template
 *
 * **Example.**
 * Configure a Perforce Shelve Trigger which runs a personal build when a new or modified shelved changelist is detected.
 * The shelved changelist must have #review keyword in its description.
 * The build will enforce clean checkout, and will have an extra `inShelve=true` configuration parameter.
 * ```
 * object Docker : BuildType({
 *     name = "Test build for shelves"
 *     ...
 *     triggers {
 *         perforceShelveTrigger {
 *             keyword = "#review"
 *
 *             enforceCleanCheckout = true
 *             buildParams {
 *                 param("inShelve", "true")
 *             }
 *         }
 *     }
 * })
 * ```
 *
 *
 * @see PerforceShelveTrigger
 */
fun Triggers.perforceShelveTrigger(init: PerforceShelveTrigger.() -> Unit): PerforceShelveTrigger {
    val result = PerforceShelveTrigger(init)
    trigger(result)
    return result
}
