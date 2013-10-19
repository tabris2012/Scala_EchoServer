package project.send

import java.io._
import java.net.{ InetAddress, Socket, SocketException }

object PortSender {
  def main(args: Array[String]) {
    val port_num = args.length match {
      case 0 => 8000 //デフォルト送信ポート番号
      case 1 => args(0).toInt //指定ポート番号
      case _ => {
        println("How to use: (program) (send port)")
        exit
      }
    }
    
    println("Send port: " + port_num)
    var line = "" //読み込み用バッファ
    var std_cond = true //読み込み状態検知
    
    try {
      val ia = InetAddress.getByName("localhost")
      val socket = new Socket(ia, port_num) //送信先ソケットを展開
      val out = new DataOutputStream(socket.getOutputStream()) //出力に接続
      
      while (std_cond) {
        line = readLine("> ")

        if (line == null) {
          std_cond = false //EOFで終了
          println
        } else {
          out.writeUTF(line)
          println("Sent line: " + line)
        }
      }
      
      out.close()
      socket.close()
    }
    catch {
      case e: IOException => e.printStackTrace
    }
  }
}
