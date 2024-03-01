package fr.xgouchet.luxels.core.render.exposure

import io.kotest.core.spec.style.DescribeSpec

class RoughFilmTest : DescribeSpec({
    include(AbstractFilmTest { resolution -> RoughFilm(resolution) })
})