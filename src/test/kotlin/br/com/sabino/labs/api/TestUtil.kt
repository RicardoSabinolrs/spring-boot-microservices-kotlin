@file:JvmName("TestUtil")

package br.com.sabino.labs.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher
import org.hamcrest.TypeSafeMatcher
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar
import org.springframework.format.support.DefaultFormattingConversionService
import org.springframework.format.support.FormattingConversionService
import java.io.IOException
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

private val mapper = createObjectMapper()

private fun createObjectMapper() =
    ObjectMapper().apply {
        configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        registerModule(JavaTimeModule())
    }

@Throws(IOException::class)
fun convertObjectToJsonBytes(`object`: Any): ByteArray = mapper.writeValueAsBytes(`object`)

fun createByteArray(size: Int, data: String) = ByteArray(size) { java.lang.Byte.parseByte(data, 2) }
fun sameInstant(date: ZonedDateTime) = ZonedDateTimeMatcher(date)
fun sameNumber(number: BigDecimal): NumberMatcher = NumberMatcher(number)

class ZonedDateTimeMatcher(private val date: ZonedDateTime) : TypeSafeDiagnosingMatcher<String>() {

    override fun matchesSafely(item: String, mismatchDescription: Description): Boolean {
        try {
            if (!date.isEqual(ZonedDateTime.parse(item))) {
                mismatchDescription.appendText("was ").appendValue(item)
                return false
            }
            return true
        } catch (e: DateTimeParseException) {
            mismatchDescription.appendText("was ").appendValue(item)
                .appendText(", which could not be parsed as a ZonedDateTime")
            return false
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("a String representing the same Instant as ").appendValue(date)
    }
}

class NumberMatcher(private val value: BigDecimal) : TypeSafeMatcher<Number>() {
    override fun describeTo(description: Description) {
        description.appendText("a numeric value is ").appendValue(value)
    }

    override fun matchesSafely(item: Number): Boolean {
        val bigDecimal = asDecimal(item)
        return value.compareTo(bigDecimal) == 0
    }

    fun asDecimal(item: Number?): BigDecimal? {
        if (item == null) {
            return null
        }

        return when (item) {
            is BigDecimal -> item
            is Long -> item.toBigDecimal()
            is Int -> item.toLong().toBigDecimal()
            is Float -> item.toBigDecimal()
            is Double -> item.toBigDecimal()
            else -> item.toDouble().toBigDecimal()
        }
    }
}

fun <T : Any> equalsVerifier(clazz: KClass<T>) {
    val domainObject1 = clazz.createInstance()
    assertThat(domainObject1.toString()).isNotNull()
    assertThat(domainObject1).isEqualTo(domainObject1)
    assertThat(domainObject1).hasSameHashCodeAs(domainObject1)

    // Test with an instance of another class
    val testOtherObject = Any()
    assertThat(domainObject1).isNotEqualTo(testOtherObject)
    assertThat(domainObject1).isNotEqualTo(null)

    // Test with an instance of the same class
    val domainObject2 = clazz.createInstance()
    assertThat(domainObject1).isNotEqualTo(domainObject2)

    // HashCodes are equals because the objects are not persisted yet
    assertThat(domainObject1).hasSameHashCodeAs(domainObject2)
}

fun createFormattingConversionService(): FormattingConversionService {
    val dfcs = DefaultFormattingConversionService()
    val registrar = DateTimeFormatterRegistrar()
    registrar.setUseIsoFormat(true)
    registrar.registerFormatters(dfcs)
    return dfcs
}

const val TEST_USER_LOGIN = "test"
