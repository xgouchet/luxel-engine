package fr.xgouchet.luxels.core.render.exposure

import io.kotest.core.spec.style.DescribeSpec

class RoughFilmSpec : DescribeSpec({
    include(abstractFilmSpec { resolution -> RoughFilm(resolution) })
})
