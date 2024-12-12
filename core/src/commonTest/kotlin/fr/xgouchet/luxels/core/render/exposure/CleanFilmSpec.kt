package fr.xgouchet.luxels.core.render.exposure

import fr.xgouchet.luxels.core.render.CleanFilm
import io.kotest.core.spec.style.DescribeSpec

class CleanFilmSpec : DescribeSpec({
    include(abstractFilmSpec { resolution -> CleanFilm(resolution) })
})
