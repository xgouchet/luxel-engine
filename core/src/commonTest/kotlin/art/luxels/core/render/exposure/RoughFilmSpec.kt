package art.luxels.core.render.exposure

import art.luxels.core.render.RoughFilm
import io.kotest.core.spec.style.DescribeSpec

class RoughFilmSpec : DescribeSpec({
    include(abstractFilmSpec { resolution -> RoughFilm(resolution) })
})
