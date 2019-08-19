package com.github.zubmike.service.demo.types

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "zone_spaces")
class ZoneSpace {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "zone_id")
    var zoneId: Int = 0

    @Column(name = "starship_id")
    var starshipId: Long = 0

    @Column(name = "create_date")
    var createDate: LocalDateTime? = null

}
