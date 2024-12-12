@file:Suppress("CommentRegions")

package fr.xgouchet.luxels.engine.api.configuration

import fr.xgouchet.luxels.core.io.ImageFixer
import fr.xgouchet.luxels.core.io.NoOpFixer
import fr.xgouchet.luxels.core.math.Dimension
import fr.xgouchet.luxels.core.math.Vector
import fr.xgouchet.luxels.core.math.Volume
import fr.xgouchet.luxels.core.render.Resolution
import fr.xgouchet.luxels.engine.api.input.EnumInputSource
import fr.xgouchet.luxels.engine.api.input.FilesInputSource
import fr.xgouchet.luxels.engine.api.input.FixedSeedsInputSource
import fr.xgouchet.luxels.engine.api.input.InputSource
import fr.xgouchet.luxels.engine.api.input.RandomSeedsInputSource
import okio.Path
import kotlin.reflect.KType
import kotlin.time.Duration

/**
 * Creates a Luxel configuration with a single Unit Input.
 * @param D the dimension of the space luxels evolve in
 * @param dimension the dimension instance to build the space
 * @param configure the configuration block
 */
fun <D : Dimension> configuration(
    dimension: D,
    configure: ConfigurationBuilder<D, Unit>.() -> Unit,
): Configuration<D, Unit> {
    return configurationWithInputSource(dimension, InputSource.Default, configure)
}

/**
 * Creates a Luxel configuration with a set of random seeds as inputs.
 * @param D the dimension of the space luxels evolve in
 * @param dimension the dimension instance to build the space
 * @param count the number of random seeds to use (default:  8)
 * @param configure the configuration block
 */
fun <D : Dimension> configurationWithRandomSeeds(
    dimension: D,
    count: Int = 8,
    configure: ConfigurationBuilder<D, Long>.() -> Unit,
): Configuration<D, Long> {
    return configurationWithInputSource(dimension, RandomSeedsInputSource(count), configure)
}

/**
 * Creates a Luxel configuration with a set of Long seeds as inputs.
 * @param D the dimension of the space luxels evolve in
 * @param dimension the dimension instance to build the space
 * @param seeds the seeds to use (each seed will generate one full simulation)
 * @param configure the configuration block
 */
fun <D : Dimension> configurationWithFixedSeeds(
    dimension: D,
    vararg seeds: Long,
    configure: ConfigurationBuilder<D, Long>.() -> Unit,
): Configuration<D, Long> {
    return configurationWithInputSource(dimension, FixedSeedsInputSource(seeds.toList()), configure)
}

// TODO KMM
//    fun withImages(dir: String) {
//        inputConfig = inputConfig.copy(source = InputSource.Images(dir))
//    }

/**
 * Creates a Luxel configuration with all values from an enum as inputs.
 * @param D the dimension of the space luxels evolve in
 * @param E the type of enum to use
 * @param dimension the dimension instance to build the space
 * @param enumType the [KType] of enum to use (each value of the enum will generate one full simulation)
 * @param configure the configuration block
 */
fun <D : Dimension, E : Enum<E>> configurationWithEnum(
    dimension: D,
    enumType: KType,
    configure: ConfigurationBuilder<D, E>.() -> Unit,
): Configuration<D, E> {
    return configurationWithInputSource(dimension, EnumInputSource(enumType), configure)
}

/**
 * Creates a Luxel configuration with all files from a directory.
 * @param D the dimension of the space luxels evolve in
 * @param dimension the dimension instance to build the space
 * @param inputDir the input directory path
 * @param configure the configuration block
 */
fun <D : Dimension> configurationWithFilesFrom(
    dimension: D,
    inputDir: Path,
    configure: ConfigurationBuilder<D, Path>.() -> Unit,
): Configuration<D, Path> {
    return configurationWithInputSource(dimension, FilesInputSource(inputDir), configure)
}

/**
 * Creates a Luxel configuration with a custom input source.
 * @param D the dimension of the space luxels evolve in
 * @param I the type of data used as Input source
 * @param dimension the dimension instance to build the space
 * @param inputSource the source of inputs to use
 * @param configure the configuration block
 */
fun <D : Dimension, I : Any> configurationWithInputSource(
    dimension: D,
    inputSource: InputSource<I>,
    configure: ConfigurationBuilder<D, I>.() -> Unit,
): Configuration<D, I> {
    val builder = ConfigurationBuilder(dimension, inputSource)

    builder.configure()

    return builder.build()
}

/**
 *  A builder DSL for the [Configuration] type.
 */
class ConfigurationBuilder<D : Dimension, I : Any> internal constructor(
    private val dimension: D,
    inputSource: InputSource<I>,
) {
    private var input: Configuration.Input<I> = Configuration.Input(inputSource)
    private var simulation: Configuration.Simulation<D> = Configuration.Simulation(Volume.unit(dimension))
    private var render: Configuration.Render = Configuration.Render()
    private var animation: Configuration.Animation = Configuration.Animation()

    private var simulationVolumeDensity: Double? = null

    /**
     * Configure the simulation options.
     */
    fun simulation(configure: SimulationConfigBuilder<D>.() -> Unit) {
        val simulationConfigBuilder = SimulationConfigBuilder(dimension)

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

    /**
     * Sets the simulation space volume based on the resolution and a given density.
     * @param density the density (i.e.: the number of units per pixel)
     */
    fun simulationVolumeDensity(density: Double) {
        simulationVolumeDensity = density
    }

    internal fun build(): Configuration<D, I> {
        val densityVolume = simulationVolumeDensity?.let { density ->
            Volume(
                min = Vector.nul(dimension),
                max = Vector.from(dimension) {
                    if (it % 2 == 0) {
                        render.resolution.width * density
                    } else {
                        render.resolution.height * density
                    }
                },
            )
        }
        if (densityVolume != null) {
            simulation = simulation.copy(volume = densityVolume)
        }
        return Configuration(dimension, input, simulation, animation, render)
    }
}

/**
 *  A builder DSL for the [Configuration.Simulation] type.
 * @param D the dimension of the space luxels evolve in
 * @property dimension the dimension instance to build the space
 */
class SimulationConfigBuilder<D : Dimension> internal constructor(
    val dimension: D,
) {

    private var simulation = Configuration.Simulation(Volume.unit(dimension))

    /**
     * Sets the quality of the simulation.
     * @param quality the quality of the simulation (default: DEBUG)
     */
    fun quality(quality: Quality) {
        simulation = simulation.copy(quality = quality)
    }

    /**
     * Sets the range of the simulation.
     * @param volume the simulation range (default: unit volume)
     */
    fun space(volume: Volume<D>) {
        simulation = simulation.copy(volume = volume)
    }

    /**
     * Sets the number of threads to use in parallel for each computation.
     * @param count the number of threads to use (default: 4)
     */
    fun maximumThreadCount(count: Int) {
        simulation = simulation.copy(maxThreadCount = count)
    }

    /**
     * Sets the kind of rendering to perform based on the simulation.
     * @param type the type of pass (default: [SimulationType.RENDER])
     */
    fun passType(type: SimulationType) {
        simulation = simulation.copy(simulationType = type)
    }

    internal fun build(): Configuration.Simulation<D> {
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
     * @param fps the fps for the animation (default: 24)
     */
    fun fps(fps: Int) {
        animation = animation.copy(fps = fps)
    }

    internal fun build(): Configuration.Animation {
        return animation
    }
}
