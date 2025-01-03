package fr.xgouchet.luxels.components.color.atomic

/**
 * Utility grouping instances of [ASLColorSource] per atomic properties.
 */
object PeriodicTable {

    /**
     * Group the first 3 rows of the periodic table.
     */
    val lightElements = arrayOf(
        Hydrogen,
        Helium,
        Lithium,
        Beryllium,
        Boron,
        Carbon,
        Nitrogen,
        Oxygen,
        Fluorine,
        Neon,
        Sodium,
        Magnesium,
        Aluminium,
        Silicon,
        Phosphorus,
        Sulfur,
        Chlorine,
        Argon,
    )

    /**
     * Group rows 4 and 5 of the periodic table.
     */
    val mediumElements = arrayOf(
        Potassium,
        Calcium,
        Scandium,
        Titanium,
        Vanadium,
        Chromium,
        Manganese,
        Iron,
        Cobalt,
        Nickel,
        Copper,
        Zinc,
        Gallium,
        Germanium,
        Arsenic,
        // Selenium, // No Visible Spectral Lines
        Bromine,
        Krypton,
        Rubidium,
        Strontium,
        Yttrium,
        Zirconium,
        Niobium,
        Molybdenum,
        Technetium,
        Ruthenium,
        Rhodium,
        Palladium,
        Silver,
        Cadmium,
        Indium,
        Tin,
        Antimony,
        Tellurium,
        Iodine,
        Xenon,
    )

    /**
     * Group row 6 of the periodic table.
     */
    val heavyElements = arrayOf(
        Caesium,
        Barium,
        Lanthanum,
        Cerium,
        Praseodymium,
        Neodymium,
        Promethium,
        Samarium,
        Europium,
        Gadolinium,
        Terbium,
        Dysprosium,
        Holmium,
        Erbium,
        Thulium,
        Ytterbium,
        Lutetium,
        Hafnium,
        Tantalum,
        Tungsten,
        // Rhenium, // No Visible Spectral Lines
        // Osmium, // No Visible Spectral Lines
        Iridium,
        Platinum,
        Gold,
        Mercury,
        Thallium,
        Lead,
        Bismuth,
        Polonium,
        // Astatine, // No Visible Spectral Lines
        Radon,
    )

    /** Group Alkali Metals. */
    val alkaliMetals = arrayOf(Lithium, Sodium, Potassium, Rubidium, Caesium)

    /** Group Alkaline-Earth Metals. */
    val alkalineEarthMetals = arrayOf(Beryllium, Magnesium, Calcium, Strontium, Barium)

    /** Group Halogens ("salt former": when reacting with metals, they produce a wide range of salts). */
    // Missing: Astatine (No visible spectral lines)
    val halogens = arrayOf(Fluorine, Chlorine, Bromine, Iodine)

    /** Group Metalloids (non metal elements with some metallic properties). */
    val metalloids = arrayOf(Boron, Silicon, Germanium, Arsenic, Antimony, Tellurium, Polonium)

    /** Group Noble Gases (odorless, colorless, monatomic elements, with almost no reaction with other elements).*/
    val nobleGases = arrayOf(Helium, Neon, Argon, Krypton, Xenon, Radon)

    /** Group Non Metals (mostly lack distinctive metallic properties).*/
    // Missing: Selenium (No visible spectral lines)
    val nonMetals = arrayOf(Hydrogen, Carbon, Nitrogen, Oxygen, Phosphorus, Sulfur)

    /** Group Post-Transition Metals .*/
    val postTransitionMetals = arrayOf(Aluminium, Gallium, Indium, Tin, Thallium, Lead, Bismuth)

    /** Group Transition Metals. */
    val transitionMetals = arrayOf(
        Scandium,
        Titanium,
        Vanadium,
        Chromium,
        Manganese,
        Iron,
        Cobalt,
        Nickel,
        Copper,
        Zinc,
        Yttrium,
        Zirconium,
        Niobium,
        Molybdenum,
        Technetium,
        Ruthenium,
        Rhodium,
        Palladium,
        Silver,
        Cadmium,
        Hafnium,
        Tantalum,
        Tungsten,
        // Rhenium,
        // Osmium,
        Iridium,
        Platinum,
        Gold,
        Mercury,
    )

    /**
     * Group all known elements of the periodic table.
     */
    val allElements = lightElements + mediumElements + heavyElements
}
