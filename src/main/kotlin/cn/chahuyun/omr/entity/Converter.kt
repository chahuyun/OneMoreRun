package cn.chahuyun.omr.entity

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class StringListConverter : AttributeConverter<List<String>, String> {

    private val objectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        return attribute?.let {
            try {
                objectMapper.writeValueAsString(it)
            } catch (e: JsonProcessingException) {
                throw RuntimeException("Error converting list to JSON", e)
            }
        } ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return dbData?.let {
            try {
                objectMapper.readValue(it, Array<String>::class.java).toList()
            } catch (e: JsonProcessingException) {
                throw RuntimeException("Error converting JSON to list", e)
            }
        } ?: emptyList()
    }
}