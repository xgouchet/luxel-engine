package fr.xgouchet.luxels.cli

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import fr.xgouchet.luxels.cli.common.LuxelCommand
import fr.xgouchet.luxels.cli.demo.cube.CubeCommand
import fr.xgouchet.luxels.cli.demo.metaballs.MetaBallsCommand
import fr.xgouchet.luxels.cli.demo.noise.NoiseCommand
import fr.xgouchet.luxels.cli.fractals.buddhabrot.BuddhabrotCommand
import fr.xgouchet.luxels.cli.fractals.mandelbrot.MandelbrotCommand
import fr.xgouchet.luxels.cli.series.s01aether.AetherCommand
import fr.xgouchet.luxels.cli.series.s03gravity.GravityCommand
import fr.xgouchet.luxels.cli.wip.gravityreborn.GravityRebornCommand

fun main(args: Array<String>) {
    LuxelCommand()
        .subcommands(
            CubeCommand(),
            MetaBallsCommand(),
            NoiseCommand(),
            BuddhabrotCommand(),
            MandelbrotCommand(),
            AetherCommand(),
            GravityCommand(),
            GravityRebornCommand(),
        )
        .main(args)
}
