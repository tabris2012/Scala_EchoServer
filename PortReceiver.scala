package project.receive

import java.io._
import java.net.{ InetAddress, Socket, SocketException }
//ポートを開き、受信した内容を標準出力に書きだす
object PortReceiver {
  def main(args: Array[String]) {
    val port_num = args.length match {
      case 0 => 8001 //デフォルト受信ポート番号
      case 1 => args(0).toInt //指定ポート番号
      case _ => {
        println("How to use: (program) (receive port)")
        exit
      }
    }
    
    println("Receive port: " + port_num)
    var line = "" //書き出し用バッファ
    
    try {
      val ia = InetAddress.getByName("localhost")
      val socket = new Socket(ia, port_num) //受信先ソケットを展開
      val in = new DataInputStream(socket.getInputStream()) //入力に接続
      
      try {
        while (true) {
          line  = in.readUTF()
          println(line)
        }
      }
      catch {
        case e: EOFException => {
          in.close()
          socket.close()
        }
      }
    }
    catch {
      case e: IOException => e.printStackTrace
    }
  }
}
