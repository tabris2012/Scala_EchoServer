package external.server

import scala.actors._
import scala.actors.Actor._
import java.io._
import java.net.{ ServerSocket, Socket, SocketException }

case class End //終了メッセージ用ケース
//サンプル用のエコーサーバー
object SampleServer {
  def main(args: Array[String]) {
    val port_num = args.length match {
      case 0 => Array(8000, 8001) //デフォルト送受信ポート番号
      case 2 => Array(args(0).toInt, args(1).toInt) //指定ポート番号
      case _ => {
        println("How to use: (program) (receive port) (send port)")
        exit
      }
    }
    
    println("Receive port: " + port_num(0))
    println("Send port: " + port_num(1))
    
    val send_server = actor {
      try {
        val server_soc = new ServerSocket(port_num(1)) //送信用ソケットを展開
        val socket = server_soc.accept() //接続待ち
        val out = new DataOutputStream(socket.getOutputStream()) //出力ストリームを開放

        loop {
          react { //メッセージを受け取って送信
            case line: String => out.writeUTF(line)
            case End => { //終了処理
              out.close()
              socket.close()
              exit
            }
          }
        }
      } catch {
        case e: IOException => e.printStackTrace
      }
    }
    
    send_server.start
    
    val rec_server = actor {
      try {
        val server_soc = new ServerSocket(port_num(0)) //受信用ソケットを展開
        val socket = server_soc.accept() //接続待ち
        val in = new DataInputStream(socket.getInputStream()) //入力ストリームを開放
        var line = "" //読み込み用バッファ
        
        loop {
          try {
            line = in.readUTF()
            send_server ! line //送信サーバにメッセージ送信
          } catch {
            case e: EOFException => { //readUTFがEOFで落ちたら終了
              send_server ! End //終了メッセージを送信
              in.close()
              socket.close()
              exit
            }
          }
        }
      } catch {
        case e: IOException => e.printStackTrace
      }
    }
    
    rec_server.start
  }
}
