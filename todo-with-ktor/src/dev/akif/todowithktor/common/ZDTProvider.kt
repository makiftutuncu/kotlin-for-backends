package dev.akif.todowithktor.common

import java.time.ZonedDateTime

interface ZDTProvider {
    fun now(): ZonedDateTime
}
