package routine.entity.common

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class CategoryConverter: AttributeConverter<CategoryEnum, String> {
    override fun convertToDatabaseColumn(category: CategoryEnum?): String? =  category?.category

    override fun convertToEntityAttribute(value: String?): CategoryEnum? = value?.let{CategoryEnum.valueOf(it.uppercase())}
}

@Converter
class ResultConverter: AttributeConverter<ResultEnum, String>{
    override fun convertToDatabaseColumn(result: ResultEnum?): String? =  result?.result

    override fun convertToEntityAttribute(value: String?): ResultEnum? = value?.let{ResultEnum.valueOf(it.uppercase())}
}

@Converter
class DayConverter: AttributeConverter<DayEnum, Int>{

    override fun convertToDatabaseColumn(day: DayEnum?): Int? = day?.day

    override fun convertToEntityAttribute(value: Int?): DayEnum? = when(value){
            1 -> DayEnum.MON
            2 -> DayEnum.TUE
            3 -> DayEnum.WED
            4 -> DayEnum.THU
            5 -> DayEnum.FRI
            6 -> DayEnum.SAT
            7 -> DayEnum.SUN
        else -> {null}
    }
}