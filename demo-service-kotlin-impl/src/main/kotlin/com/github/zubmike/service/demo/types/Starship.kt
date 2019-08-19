package com.github.zubmike.service.demo.types

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "starships")
@Cache(region = "zone", usage = CacheConcurrencyStrategy.READ_WRITE)
class Starship {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "number")
    var number: String? = null

    @Column(name = "planetary_system_id")
    var planetarySystemId: Int = 0

    @Column(name = "create_date")
    var createDate: LocalDateTime? = null

    @Column(name = "time_count")
    var timeCount: Int = 0
}
