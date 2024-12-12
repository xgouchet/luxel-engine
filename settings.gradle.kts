

include(":graphikio")
include(":core")
include(":engine")
include(":components")
include(":cli")
include(":detekt")

rootProject.children.forEach {
    it.buildFileName = "${it.name}.build.gradle.kts"
}
