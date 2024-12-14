package fr.xgouchet.luxels.components.test.kotest.property

import fr.xgouchet.luxels.components.color.atomic.PeriodicTable
import io.kotest.property.exhaustive.exhaustive

fun atomicElementArb() = exhaustive(PeriodicTable.allElements.asList())
