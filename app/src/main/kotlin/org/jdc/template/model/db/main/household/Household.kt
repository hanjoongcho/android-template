package org.jdc.template.model.db.main.household

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "household")
class Household {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String = ""
}
