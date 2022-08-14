package routine.entity.common

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class CategoryConverter: AttributeConverter<CategoryEnum, String> {
    override fun convertToDatabaseColumn(Category: CategoryEnum): String =  Category.category

    override fun convertToEntityAttribute(value: String): CategoryEnum = CategoryEnum.valueOf(value.uppercase())
}

@Converter
class ResultConverter: AttributeConverter<ResultEnum, String>{
    override fun convertToDatabaseColumn(category: ResultEnum): String =  category.result

    override fun convertToEntityAttribute(value: String): ResultEnum = ResultEnum.valueOf(value.uppercase())
}

@Converter
class DayConverter: AttributeConverter<DayEnum, Int>{

    override fun convertToDatabaseColumn(day: DayEnum): Int = day.day

    override fun convertToEntityAttribute(value: Int): DayEnum = when(value){
            0 -> DayEnum.MON
            1 -> DayEnum.TUE
            2 -> DayEnum.WED
            3 -> DayEnum.THU
            4 -> DayEnum.FRI
            5 -> DayEnum.SAT
            6 -> DayEnum.SUN
        else -> {throw IllegalArgumentException("Invalid Argument")}
    }
}