package com.kedokato.music4u.data.mapper

import com.kedokato.music4u.data.remote.dto.SongDTO
import com.kedokato.music4u.domain.model.Song
import kotlin.random.Random

object SongMapper {

    fun toDomain(songDTO: SongDTO): Song{
        return Song(
            id = Random(songDTO.title.hashCode() + songDTO.artist.hashCode()).nextLong(Long.MAX_VALUE),
            name = songDTO.title,
            artist = songDTO.artist,
            duration = songDTO.duration.toString(),
            image = null,
            uri = songDTO.path,
        )
    }

}

