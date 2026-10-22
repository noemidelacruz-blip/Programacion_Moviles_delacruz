package com.delacruz.huellitasvet.model

// Estados posibles de la vacuna segun la fecha de proxima dosis
enum class EstadoVacuna(val etiqueta: String) {
    VIGENTE("Vigente"),
    PROXIMA("Próxima"),
    VENCIDA("Vencida");

    companion object {
        fun desdeTexto(texto: String?): EstadoVacuna =
            entries.firstOrNull { it.name == texto } ?: VIGENTE
    }
}
