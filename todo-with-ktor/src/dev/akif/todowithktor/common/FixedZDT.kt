package dev.akif.todowithktor.common

import java.time.ZonedDateTime

class FixedZDT(var value: ZonedDateTime) : ZDTProvider {
    override fun now(): ZonedDateTime = value
}
