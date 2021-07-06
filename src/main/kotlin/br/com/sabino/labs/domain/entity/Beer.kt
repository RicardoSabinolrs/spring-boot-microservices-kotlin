package br.com.sabino.labs.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

import java.io.Serializable

/**
 * A Beer.
 */
@Document(collection = "beer")
data class Beer(
    @Id
    var id: String? = null,
    @Field("name")
    var name: String? = null,

    @Field("ibu")
    var ibu: String? = null,

    @Field("style")
    var style: String? = null,

    @Field("description")
    var description: String? = null,

    @Field("alcohol_tenor")
    var alcoholTenor: String? = null,

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Beer) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Beer{" +
            "id=$id" +
            ", name='$name'" +
            ", ibu='$ibu'" +
            ", style='$style'" +
            ", description='$description'" +
            ", alcoholTenor='$alcoholTenor'" +
            "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
