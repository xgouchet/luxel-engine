@file:Suppress("CommentRegions")

package fr.xgouchet.luxels.core.configuration

import fr.xgouchet.luxels.core.configuration.input.EnumInputSource
import fr.xgouchet.luxels.core.configuration.input.FilesInputSource
import fr.xgouchet.luxels.core.configuration.input.FixedSeedsInputSource
import fr.xgouchet.luxels.core.configuration.input.InputSource
import fr.xgouchet.luxels.core.configuration.input.RandomSeedsInputSource
import fr.xgouchet.luxels.core.io.BmpImageFixer
import fr.xgouchet.luxels.core.io.HdrImageFixer
import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.io.NoOpFixer
import fr.xgouchet.luxels.core.math.geometry.Space3
import okio.Path
import okio.Path.Companion.toPath
import kotlin.reflect.KType
import kotlin.time.Duration

/**
 * Creates a Luxel configuration with a single Unit Input.
 * @param configure the configuration block
 */
fun configuration(configure: ConfigurationBuilder<Unit>.() -> Unit): Configuration<Unit> {
    return configurationWithInputSource(InputSource.Default, configure)
}

/**
 * Creates a Luxel configuration with a set of random seeds as inputs.
 * @param count the number of random seeds to use (default:  10)
 * @param configure the configuration block
 */
fun configurationWithRandomSeeds(
    count: Int = 10,
    configure: ConfigurationBuilder<Long>.() -> Unit,
): Configuration<Long> {
    return configurationWithInputSource(RandomSeedsInputSource(count), configure)
}

/**
 * Creates a Luxel configuration with a set of Long seeds as inputs.
 * @param seeds the seeds to use (each seed will generate one full simulation)
 * @param configure the configuration block
 */
fun configurationWithFixedSeeds(
    vararg seeds: Long,
    configure: ConfigurationBuilder<Long>.() -> Unit,
): Configuration<Long> {
    return configurationWithInputSource(FixedSeedsInputSource(seeds.toList()), configure)
}

// TODO KMM
//    fun withImages(dir: String) {
//        inputConfig = inputConfig.copy(source = InputSource.Images(dir))
//    }

/**
 * Creates a Luxel configuration with all values from an enum as inputs.
 * @param E the type of enum to use
 * @param enumType the [KType] of enum to use (each value of the enum will generate one full simulation)
 * @param configure the configuration block
 */
fun <E : Enum<E>> configurationWithEnum(
    enumType: KType,
    configure: ConfigurationBuilder<E>.() -> Unit,
): Configuration<E> {
    return configurationWithInputSource(EnumInputSource(enumType), configure)
}

/**
 * Creates a Luxel configuration with all files from a directory.
 * @param configure the configuration block
 */
fun configurationWithFilesFrom(
    inputDir: Path,
    configure: ConfigurationBuilder<Path>.() -> Unit,
): Configuration<Path> {
    return configurationWithInputSource(FilesInputSource(inputDir), configure)
}

/**
 * Creates a Luxel configuration with a custom input source.
 * @param D the type of data used as Input source
 * @param inputSource the source of inputs to use
 * @param configure the configuration block
 */
fun <D : Any> configurationWithInputSource(
    inputSource: InputSource<D>,
    configure: ConfigurationBuilder<D>.() -> Unit,
): Configuration<D> {
    val builder = ConfigurationBuilder(inputSource)

    builder.configure()

    return builder.build()
}

/**
 *  A builder DSL for the [Configuration] type.
 */
class ConfigurationBuilder<D : Any> internal constructor(
    inputSource: InputSource<D>,
) {
    private var input: Configuration.Input<D> = Configuration.Input(inputSource)
    private var simulation: Configuration.Simulation = Configuration.Simulation()
    private var render: Configuration.Render = Configuration.Render()
    private var animation: Configuration.Animation = Configuration.Animation()

    /**
     * Configure the simulation options.
     */
    fun simulation(configure: SimulationConfigBuilder.() -> Unit) {
        val simulationConfigBuilder = SimulationConfigBuilder()

        simulationConfigBuilder.configure()

        simulation = simulationConfigBuilder.build()
    }

    /**
     * Configure the render options.
     */
    fun render(configure: RenderConfigBuilder.() -> Unit) {
        val renderConfigBuilder = RenderConfigBuilder()

        renderConfigBuilder.configure()

        render = renderConfigBuilder.build()
    }

    /**
     * Configure the animation options.
     */
    fun animation(configure: AnimationConfigBuilder.() -> Unit) {
        val animationConfigBuilder = AnimationConfigBuilder()

        animationConfigBuilder.configure()

        animation = animationConfigBuilder.build()
    }

    internal fun build(): Configuration<D> {
        return Configuration(input, simulation, render, animation)
    }
}

/**
 *  A builder DSL for the [Configuration.Simulation] type.
 */
class SimulationConfigBuilder internal constructor() {
    private var simulation = Configuration.Simulation()

    /**
     * Sets the quality of the simulation.
     * @param quality the quality of the simulation (default: DEBUG)
     */
    fun quality(quality: Quality) {
        simulation = simulation.copy(quality = quality)
    }

    /**
     * Sets the range of the simulation.
     * @param space3 the simulation range (default: [Space3.UNIT])
     */
    fun space(space3: Space3) {
        simulation = simulation.copy(space = space3)
    }

    /**
     * Sets the range of the simulation based on the resolution and a density.
     * @param resolution the film resolution
     * @param density the density (i.e.: the number of units per pixel)
     */
    fun space(resolution: Resolution, density: Double = 1.0) {
        simulation = simulation.copy(
            space = resolution.asSpace3() * density,
        )
    }

    /**
     * Sets the number of threads to use in parallel for each computation.
     * @param count the number of threads to use (default: 4)
     */
    fun threadCount(count: Int) {
        simulation = simulation.copy(threadCount = count)
    }

    // TODO KMM availableProcessors
//    fun threadCountMatchingCpuCores() {
//        simulationConfig = simulationConfig.copy(threadCount = Runtime.getRuntime().availableProcessors())
//    }

    /**
     * Sets the kind of rendering to perform based on the simulation.
     * @param type the type of pass (default: [PassType.RENDER])
     */
    fun passType(type: PassType) {
        simulation = simulation.copy(passType = type)
    }

    internal fun build(): Configuration.Simulation {
        return simulation
    }
}

/**
 *  A builder DSL for the [Configuration.Render] type.
 */
class RenderConfigBuilder internal constructor() {
    private var render = Configuration.Render()

    /**
     * Sets the type of film to use for rendering.
     * @param filmType the type of film to render on (default: [FilmType.ROUGH])
     */
    fun filmType(filmType: FilmType) {
        render = render.copy(filmType = filmType)
    }

    /**
     * Sets the resolution of the film to use for rendering.
     * @param resolution the resolution of film to render on (default: [Resolution.XGA])
     */
    fun resolution(resolution: Resolution) {
        render = render.copy(resolution = resolution)
    }

    /**
     * Uses an HDR image fixer.
     * @param path the output directory path (default is "./output")
     */
    fun hdrFixer(path: Path = "output".toPath()) {
        render = render.copy(fixer = HdrImageFixer(path))
    }

    /**
     * Uses a BMP image fixer.
     * @param iso the iso sensitivity of the fixer (the lower the darker the image, the higher the brighter, default is 400)
     * @param path the output directory path (default is "./output")
     */
    fun bmpFixer(iso: Double = 400.0, path: Path = "output".toPath()) {
        render = render.copy(fixer = BmpImageFixer(path, iso))
    }

    /**
     * Sets the [ImageFixer] that will save the content of the film to an Image file.
     * @param fixer the fixer to save the film to an Image file (default: [NoOpFixer])
     */
    fun fixer(fixer: ImageFixer) {
        render = render.copy(fixer = fixer)
    }

    internal fun build(): Configuration.Render {
        return render
    }
}

/**
 *  A builder DSL for the [Configuration.Animation] type.
 */
class AnimationConfigBuilder {
    private var animation = Configuration.Animation()

    /**
     * Sets the total duration of the animation.
     * @param duration the duration of the animation (default: 0)
     */
    fun duration(duration: Duration) {
        animation = animation.copy(duration = duration)
    }

    /**
     * Sets the number of frame per second for the animation.
     * @param fps the fps for the animation (default: 60)
     */
    fun fps(fps: Int) {
        animation = animation.copy(fps = fps)
    }

    internal fun build(): Configuration.Animation {
        return animation
    }
}
