package br.com.sabino.labs.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import java.time.Instant


abstract class AbstractAuditingEntity(

    @CreatedBy
    @Field("created_by")
    @JsonIgnore
    var createdBy: String? = null,

    @CreatedDate
    @Field("created_date")
    @JsonIgnore
    var createdDate: Instant? = Instant.now(),

    @LastModifiedBy
    @Field("last_modified_by")
    @JsonIgnore
    var lastModifiedBy: String? = null,

    @LastModifiedDate
    @Field("last_modified_date")
    @JsonIgnore
    var lastModifiedDate: Instant? = Instant.now()

) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
