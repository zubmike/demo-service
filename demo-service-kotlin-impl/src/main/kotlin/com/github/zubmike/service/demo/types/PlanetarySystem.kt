package com.github.zubmike.service.demo.types

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.Immutable
import com.github.zubmike.service.types.BasicEntityDictItem

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "planetary_systems")
@Immutable
@Cache(region = "planetarySystem", usage = CacheConcurrencyStrategy.READ_ONLY)
class PlanetarySystem : BasicEntityDictItem {

    @Column(name = "code")
    var code: String = ""

    constructor() {}

    constructor(id: Int, name: String?, code: String) : super(id, name) {
        this.code = code
    }

}
