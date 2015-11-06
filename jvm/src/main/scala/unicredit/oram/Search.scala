package unicredit.oram

import scala.io.StdIn
import java.util.UUID
import java.security.SecureRandom

import better.files._, Cmds._
import boopickle.Default._

import sync._
import search._
import transport._


object Search extends App {
  implicit val uuidPickler = transformPickler[UUID, String](_.toString, UUID.fromString)

  class IndexORAM(remote: Remote, passPhrase: String)
    extends LocalPathORAMProtocol[String, Set[UUID]] {
      val L = 8
      val Z = 4
      val rng = new SecureRandom
      val empty = Set.empty[UUID]
      val emptyID = ""
      val pickle = generatePickler[(String, Set[UUID])]
      val client = StandardClient[(String, Set[UUID])](remote, passPhrase)
    }

  class DocumentORAM(remote: Remote, passPhrase: String)
    extends LocalPathORAMProtocol[UUID, String] {
      val L = 8
      val Z = 4
      val rng = new SecureRandom
      val empty = ""
      val emptyID = UUID.fromString("16b01bbe-484b-49e8-85c5-f424a983205f")
      val pickle = generatePickler[(UUID, String)]
      val client = StandardClient[(UUID, String)](remote, passPhrase)
    }

  val remote1 = new ZMQRemote("tcp://localhost:8888")
  val remote2 = new ZMQRemote("tcp://localhost:8889")
  val index = new IndexORAM(remote1, "Hello my friend")
  val oram = new DocumentORAM(remote2, "Hello world")

  val store = new BasicStore(index, oram)

  println("Starting initialization...")
  index.init
  oram.init
  println("Done!")

  // for (document <- ls(file"examples")) {
  //   println(s"Adding document $document")
  //   store.addDocument(document.contentAsString)
  // }
  println("Adding documents...")
  val documents = ls(file"examples").map(_.contentAsString).toList
  store.addDocuments(documents)
  println("Done!")

  while (true) {
    println("Lookup a word:")
    val word = StdIn.readLine.trim
    val docs = store.search(word)
    for ((doc, i) <- docs.zipWithIndex) {
      println(s"====Document $i====")
      println(doc.take(200) + "...")
    }
  }
}