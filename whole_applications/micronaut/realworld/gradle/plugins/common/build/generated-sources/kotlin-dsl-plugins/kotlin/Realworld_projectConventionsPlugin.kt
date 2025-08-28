/**
 * Precompiled [realworld.project-conventions.gradle.kts][Realworld_project_conventions_gradle] script plugin.
 *
 * @see Realworld_project_conventions_gradle
 */
public
class Realworld_projectConventionsPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Realworld_project_conventions_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
