package com.kedokato.lession6.data.mapper

import androidx.core.net.toUri
import com.kedokato.lession6.data.remote.dto.SongDTO
import com.kedokato.lession6.domain.model.Song
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

