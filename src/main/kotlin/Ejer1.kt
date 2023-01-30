import redis.clients.jedis.Jedis

fun main(args: Array<String>) {
    val con = Jedis("89.36.214.106")
    con.auth("ieselcaminas.ad")
    con.connect()

    val lista = con.keys("*").toTypedArray()
    printHeader(lista, con)
    do {
        println("Introduce un indice para ver su valor (0 para terminar):")
        val userIndex = readln().toInt() - 1
        if (userIndex in 0..lista.size) {
            val clave = con.keys("*").elementAt(userIndex)
            when (con.type(lista.elementAt(userIndex))) {

                "string" -> println("$clave: " + con.get(lista[userIndex]))

                "hash" -> {
                    println(clave)
                    con.hgetAll(clave).forEach { println("\t"+it.key + " --> " + it.value) }
                }

                "list" ->{
                    println(clave)
                    con.lrange(clave, 0, -1).forEach { println("\t"+it) }
                }

                "set" -> con.get(lista[userIndex]).forEach { println(it) }
            }
        }
    } while (userIndex != -1)

    con.close()
}

private fun printHeader(
    lista: Array<String>,
    con: Jedis
) {
    println("0 - Terminar programa")
    for ((indice, key) in lista.withIndex()) {
        println("${indice + 1}. - $key (${con.type(key)})")

    }
}