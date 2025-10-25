package cn.chahuyun.omr.entity

import cn.chahuyun.omr.game.Property
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val jsonFormat = Json {
    prettyPrint = true // 如果需要格式化输出
    ignoreUnknownKeys = true // 忽略 JSON 中多余的字段
}


@Converter
class StringListConverter : AttributeConverter<List<String>, String> {

    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        return attribute?.let {
            jsonFormat.encodeToString(it)
        } ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return dbData?.let {
            jsonFormat.decodeFromString<List<String>>(it)
        } ?: emptyList()
    }
}

@Converter
class PropertyListConverter : AttributeConverter<List<Property>, String> {
    override fun convertToDatabaseColumn(attribute: List<Property>?): String {
        return attribute?.let {
            jsonFormat.encodeToString(it)
        } ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): List<Property> {
        return dbData?.let {
            jsonFormat.decodeFromString<List<Property>>(it)
        } ?: emptyList()
    }
}