package com.github.zubmike.service.demo.types

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import com.github.zubmike.service.types.BasicEntityDictItem

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "zones")
@Cache(region = "zone", usage = CacheConcurrencyStrategy.READ_WRITE)
class Zone : BasicEntityDictItem() {

    @Column(name = "max_size")
    var maxSize: Int = 0

}
