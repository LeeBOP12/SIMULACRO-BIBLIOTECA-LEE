package pe.edu.upeu.bibliomobil

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform