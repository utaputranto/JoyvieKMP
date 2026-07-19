package com.utaputranto.joyviekmp.feature.onboarding.data.mapper

import com.utaputranto.joyviekmp.core.model.Movie
import com.utaputranto.joyviekmp.core.network.model.MovieDto

fun MovieDto.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
    )
}
