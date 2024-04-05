

include(":graphikio")
include(":core")
include(":components")
include(":cli")
include(":detekt")

rootProject.children.forEach {
    it.buildFileName = "${it.name}.build.gradle.kts"
}
