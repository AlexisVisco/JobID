package util

import java.net.{MalformedURLException, URISyntaxException, URL}
import java.util.Random

/**
  * Par Alexis le 10/12/2016.
  */
class CodeGenerator(nb: Int) {

  val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  val numbers = "0123456789"

  val list = List(letters, numbers)

  var random: Random = new Random()

  def randomInt(start: Int, end: Int): Int = start + random.nextInt(end - start + 1)

  def pickRandom(str: String): Char = str.charAt(randomInt(0, str.length() - 1))

  def rand() : String = list(randomInt(0, list.size-1))

  def generate() : String = {
    val strBuilder = new StringBuilder
    for(a <- 0 to nb)
      {
        strBuilder.append(pickRandom(rand()))
      }
    strBuilder.toString()
  }

}
