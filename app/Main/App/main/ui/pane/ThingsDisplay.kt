package ui.pane

//import service.things.web.response.Thing

data class ThingsDisplay(
    val name: String,
    val serialNumber: String,
    val model: String
) {
//    constructor(thing: Thing) : this(
//        name = thing.name,
//        serialNumber = thing.serialNumber,
//        model = thing.model,
//    )
}