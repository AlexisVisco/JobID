package util

import java.util
import java.util.Random

/**
  * Par Alexis le 26/11/2016.
  */
class GeneratorPseudo{

  var vow = new util.ArrayList(util.Arrays.asList(
    "ai", "ey",
    "on", "en",
    "an", "au",
    "iu", "oe",
    "ay", "ei",
    "a", "e",
    "i", "o",
    "u", "y",
    "ou",
    "io", "hi",
    "oy",
    "eu"

  ))



  var con = new util.ArrayList(util.Arrays.asList(
    "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "r", "s", "t", "v", "w", "x", "z",
    "pp", "ll", "qu", "ss", "tt", "rr", "pr", "ch", "br", "dr", "rt", "sr", "th", "sc"
  ))

  var combVowCon = new util.ArrayList(util.Arrays.asList(
    "om", "is", "it", "er", "ek", "ak", "op", "ous", "es", "el", "eff", "abl", "er", "un", "ion"
  ))

  var random: Random = new Random()

  def randomInt(start: Int, end: Int): Int = start + random.nextInt(end - start + 1)

  def chanceOf(min: Int) : Boolean = randomInt(0, 100) <= min

  def pickRandom(table: util.ArrayList[String]): String = table.get(randomInt(0, table.size() - 1))

  def generate(syllable: Int): String = generate("", syllable)

  def generate(str: String, syllable: Int): String = {

    val str = new StringBuilder()

    def finalize() = {
      while (str.charAt(1).equals(str.charAt(0)) || con.contains(str.charAt(1))) {
        str.deleteCharAt(1)
      }
      while (str.charAt(str.size-2).equals(str.charAt(str.size-1))) {
        str.deleteCharAt(str.size-1)
      }
    }

    for (a <- 1 to syllable) {
      var c = ""
      if(a == 1 && chanceOf(10))
        c = pickRandom(vow)
      else
        c = pickRandom(con)
      if(a != 1 && chanceOf(3))
        c = pickRandom(combVowCon)
      val v = pickRandom(vow)
      str.append(c).append(v)
    }

    if(chanceOf(1))
      str.append(pickRandom(combVowCon))
    finalize()
    str.toString().capitalize
  }
}
