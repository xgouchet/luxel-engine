package fr.xgouchet.luxels.core.render.exposure

import io.kotest.core.spec.style.DescribeSpec

class CleanFilmTest : DescribeSpec({
    include(abstractFilmTest { resolution -> CleanFilm(resolution) })
})
