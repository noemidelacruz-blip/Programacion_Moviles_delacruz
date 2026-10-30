package com.delacruz.huellitasvet.model

// Razas sugeridas en el formulario de mascota. La lista cambia segun la especie.
// Si la especie es OTRO, el formulario pide escribir la raza
object Razas {

    val PERRO = listOf(
        "Mestizo",
        "Labrador",
        "Golden Retriever",
        "Bulldog",
        "Beagle",
        "Poodle",
        "Pastor Alemán",
        "Schnauzer",
        "Chihuahua",
        "Husky",
        "Boxer",
        "Salchicha"
    )

    val GATO = listOf(
        "Mestizo",
        "Siamés",
        "Persa",
        "Angora",
        "Bengalí",
        "Maine Coon",
        "Ragdoll",
        "Sphynx",
        "Scottish Fold",
        "Azul Ruso"
    )

    fun para(especie: Especie): List<String> = when (especie) {
        Especie.PERRO -> PERRO
        Especie.GATO -> GATO
        Especie.OTRO -> emptyList()
    }
}
