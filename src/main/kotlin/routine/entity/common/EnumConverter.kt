package routine.entity.common

import com.fasterxml.jackson.core.JsonProcessingException
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class CategoryConverter: AttributeConverter<CategoryEnum, String> {
    override fun convertToDatabaseColumn(Category: CategoryEnum): String =  Category.category

    override fun convertToEntityAttribute(value: String): CategoryEnum = CategoryEnum.valueOf(value.uppercase())
}

@Converter
class ResultConverter: AttributeConverter<ResultEnum, String>{
    override fun convertToDatabaseColumn(Category: ResultEnum): String =  Category.result

    override fun convertToEntityAttribute(value: String): ResultEnum = ResultEnum.valueOf(value.uppercase())
}

@Converter
class DayConverter: AttributeConverter<List<DayEnum>, List<Int>>{

    override fun convertToDatabaseColumn(day_enum_list: List<DayEnum>): List<Int> =
        try{
            var converted_day: MutableList<Int> = mutableListOf()
            day_enum_list.forEach{ converted_day.add(it.day) }
            converted_day.toList()
        }catch (e: JsonProcessingException) {
            throw java.lang.IllegalArgumentException()
        }

    override fun convertToEntityAttribute(day_int_list: List<Int>?): List<DayEnum> =
        try{
            var converted_day: MutableList<DayEnum> = mutableListOf()
            day_int_list?.forEach{ converted_day.add(DayEnum.values()[it])}
            converted_day.toList()
        }catch (e: JsonProcessingException) {
            throw java.lang.IllegalArgumentException()
        }

}