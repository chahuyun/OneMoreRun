@file:Suppress("JpaDataSourceORMInspection")

package cn.chahuyun.omr.entity.data

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * 称号
 */
@Entity
@Table(name = "omr_title")
class TitleData(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column(nullable = false) val name: String,
)
