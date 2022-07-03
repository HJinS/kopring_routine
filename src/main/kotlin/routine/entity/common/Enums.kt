package routine.entity.common

enum class CategoryEnum(val category: String){
    MIRACLE("MIRACLE"),
    HOMEWORK("HOMEWORK"),
}

enum class ResultEnum(val result: String){
    NOT("NOT"),
    TRY("TRY"),
    DONE("DONE"),
}

enum class DayEnum(var day: Int){
    MON(0),
    TUE(1),
    WED(2),
    THU(3),
    FRI(4),
    SAT(5),
    SUN(6);

    val myDay = day
}