/**
 * Precompiled [realworld.settings.settings.gradle.kts][Realworld_settings_settings_gradle] script plugin.
 *
 * @see Realworld_settings_settings_gradle
 */
public
class Realworld_settingsPlugin : org.gradle.api.Plugin<org.gradle.api.initialization.Settings> {
    override fun apply(target: org.gradle.api.initialization.Settings) {
        try {
            Class
                .forName("Realworld_settings_settings_gradle")
                .getDeclaredConstructor(org.gradle.api.initialization.Settings::class.java, org.gradle.api.initialization.Settings::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
