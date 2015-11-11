// package unicredit.oram
// package sync
//
// import java.util.Random
//
// import boopickle.Default._
//
// import serialization.BooSerializer
// import transport.Remote
// import client._
//
//
// trait AbstractRecursivePathORAM[K, Id <: K, Doc, Bin <: K] extends PathORAM[K, Either[Map[Id, Path], Doc], Id, Doc] { self =>
//   def bin(id: Id): Bin
//   val emptyBin = bin(emptyID)
//   implicit def pickleId: Pickler[Id]
//   implicit def pickleBin: Pickler[Bin]
//   import Path.pathPickler
//   implicit val pickle = implicitly[Pickler[(Bin, Map[Id, Path])]]
//
//   def index: ORAM[Bin, Map[Id, Path]]
//
//   // val index = new LocalPathORAM[Bin, Map[Id, Path]](
//   //   client = client.withSerializer(new BooSerializer[(Bin, Map[Id, Path])]),
//   //   rng = rng,
//   //   emptyID = bin(emptyID),
//   //   empty = Map(),
//   //   L = L,
//   //   Z = Z
//   // )
//
//   override def getPosition(id: Id) =
//     index.read(bin(id)).getOrElse(id, Path.random(L))
//
//   override def putPosition(id: Id, path: Path) = {
//     val map  = index.read(bin(id)) + (id -> path)
//     index.write(bin(id), map)
//   }
// }
//
// class RecursivePathORAM[K, Id <: K, Doc, Bin <: K](
//   val client: StandardClient[(Id, Doc)],
//   val rng: Random,
//   val emptyID: Id,
//   val empty: Doc,
//   val L: Int,
//   val Z: Int,
//   val binf: Id => Bin
// )(implicit val pickleId: Pickler[Id], val pickleBin: Pickler[Bin]) extends AbstractRecursivePathORAM[Id, Doc, Bin] {
//   def bin(id: Id) = binf(id)
// }
//
// object RecursivePathORAM {
//   import boopickle.Default._
//   import java.security.SecureRandom
//
//   def apply[Id, Doc, Bin](remote: Remote, passPhrase: String, emptyID: Id,
//     empty: Doc, L: Int, Z: Int, bin: Id => Bin)
//     (implicit p1: Pickler[Id], p2: Pickler[Doc], p3: Pickler[Bin]) =
//       new RecursivePathORAM[Id, Doc, Bin](
//         StandardClient[(Id, Doc)](remote, passPhrase), new SecureRandom,
//         emptyID, empty, L, Z, bin
//       )
//
//   def default(remote: Remote, passPhrase: String, L: Int = 8, Z: Int = 4) =
//     apply[Int, String, Int](remote, passPhrase, -1, "", L, Z, _ % 1024)
// }