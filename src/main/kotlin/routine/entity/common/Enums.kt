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
    MON(1),
    TUE(2),
    WED(3),
    THU(4),
    FRI(5),
    SAT(6),
    SUN(7);
}