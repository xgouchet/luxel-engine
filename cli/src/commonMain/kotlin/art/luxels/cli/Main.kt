package art.luxels.cli

import art.luxels.cli.common.LuxelCommand
import art.luxels.cli.demo.cube.CubeCommand
import art.luxels.cli.demo.metaballs.MetaBallsCommand
import art.luxels.cli.demo.noise.NoiseCommand
import art.luxels.cli.fractals.buddhabrot.BuddhabrotCommand
import art.luxels.cli.fractals.mandelbrot.MandelbrotCommand
import art.luxels.cli.series.s01aether.AetherCommand
import art.luxels.cli.series.s03gravity.GravityCommand
import art.luxels.cli.wip.bookworm.BookwormCommand
import art.luxels.cli.wip.gravityreborn.GravityRebornCommand
import art.luxels.cli.wip.rain.RainCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    LuxelCommand()
        .subcommands(
            AetherCommand(),
            BookwormCommand(),
            BuddhabrotCommand(),
            CubeCommand(),
            GravityCommand(),
            GravityRebornCommand(),
            MandelbrotCommand(),
            MetaBallsCommand(),
            NoiseCommand(),
            RainCommand(),
        )
        .main(args)
}
