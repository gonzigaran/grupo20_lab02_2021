package edu.paradigmas.nermodel

/* Abstraction of a NERModel (Named Entity Recognition Pipeline)
 * Receives a sequence of text.
 * Calculates a map with the Named Entities recognized
 * and the number of times each appears in the input text
 */
class NERNormalizedModel() {

  val punctuationSymbols = ".,()!?;:'`´\n"
  val punctuationRegex = "\\" + punctuationSymbols.split("").mkString("|\\")

  // Extract Named Entities from a single text
  def getNEsSingle(text: String): Seq[String] =
    text.replaceAll(punctuationRegex, "").split(" ")
      .filter { word:String => word.length > 1 &&
                Character.isUpperCase(word.charAt(0)) &&
                !STOPWORDS.contains(word.toLowerCase) }.toSeq

  // Return the named entities detected from a list of text sorted by frequency
  def getSortedNEs(textList: Seq[String]): Seq[NERCount] = 
    sortNEs(textList.map(getNEsSingle).map(countNEs).flatten)

  // Count Named Entities and sort by frequency
  def countNEs(neResult: Seq[String]): Seq[NERCount] = {
    val nes = neResult.foldLeft(Map.empty[String, Int]) {
                    (count, word) => count + (word -> (count.getOrElse(word, 0) + 1)) }
    val neTotal = nes.values.sum
    nes.toList.map { case (word, count) => NERCount(word, count, count.toFloat/neTotal) }
  }

  // Sort Named Entities by normalized frequency
  def sortNEs(neResult: Seq[NERCount]): Seq[NERCount] = {
    neResult.foldLeft(Map.empty[String, NERCount]) {
        (map, normNE) => map + (
          normNE.ner -> (NERCount(
            normNE.ner,
            map.getOrElse(normNE.ner, NERCount(normNE.ner,0,0)).count + normNE.count,
            map.getOrElse(normNE.ner, NERCount(normNE.ner,0,0)).normalizedCount + normNE.normalizedCount))
          ) }
      .toList.map { case (word, normNE) => normNE }
      .sortBy(_.normalizedCount)(Ordering[Float].reverse)
  }

}
