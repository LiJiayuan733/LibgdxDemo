import torch
import argparse
import flask
from flask import Flask, request, jsonify
import uuid
import json
from utils import Translator
import os
from traceback import print_exc
import socket
tcp_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_addr = ("localhost", 39621)
tcp_socket.connect(server_addr)
translator = None
app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False


# 失败的返回
def jsonFail(message):
    post_data = {
        "Code": -1,
        "Message": str(message),
        "RequestId": str(uuid.uuid4())
    }
    return jsonify(post_data)


# 成功的返回
def jsonSuccess(data):
    post_data = {
        "Code": 0,
        "Message": "Success",
        "RequestId": str(uuid.uuid4()),
        "Data": data
    }
    return jsonify(post_data)


def load_model_list(combo_box):
    for model_dir in os.listdir('models'):
        try:
            with open(f'models/{model_dir}/config.json', 'r', encoding='utf-8') as f:
                config = json.load(f)
                combo_box[config['name']] = model_dir
        except:
            continue


def load_model(Dir, device):
    try:
        global translator
        translator = Translator(f'models/{Dir}', device)
        if translator.tokenizer is None:
            print("错误" + "当前版本的翻译姬中不含有{}，请更新至最新版本")
            return
        if None in translator.input_cleaners or None in translator.output_cleaners:
            print("错误" + "当前版本的翻译姬中不含有所需的cleaner，请更新至最新版本")
            return
        print("Max text length:" + str(translator.config['max_len'][0]))
    except:
        print("错误" + "模型加载失败")


def _translate(original_text, beam_size, device, input_cleaner, output_cleaner):
    global translator
    translated_text = translator.translate(original_text, beam_size, device,
                                           input_cleaner, output_cleaner)
    return translated_text


combo_box = {}
load_model_list(combo_box)
device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
Dir = list(combo_box.values())[0]
self = {}
load_model(Dir, device)


def handle_request():
    # 客户端检测是否运行
    if request.method == "HEAD":
        return flask.Response(headers={
            "Dango-OCR": "OK",
        })

    try:
        post_data = request.get_data()
        post_data = json.loads(post_data.decode("utf-8"))

        res = _translate(post_data["Text"], 6, device, None, "t2s")
        return jsonSuccess(res)

    except Exception as err:
        print_exc()
        return jsonFail(err)


if __name__ == "__main__":
    host = "127.0.0.1"
    port = 7777
    path = "/api"
    parser = argparse.ArgumentParser(add_help=False)
    parser.add_argument("-h", "--host", type=str, default=host, help="监听的主机。默认：\"%s\"" % host)
    parser.add_argument("-p", "--port", type=int, default=port, help="监听的端口。默认：%d" % port)
    parser.add_argument("-P", "--path", type=str, default=path, help="监听的路径。默认：\"%s\"" % path)
    parser.add_argument('--help', action='help', help='打印帮助。')
    args = parser.parse_args()

    host = args.host
    port = args.port
    path = args.path
    tcp_socket.send(("Listen on http://"+host+":"+str(port)+path+"\n").encode("utf-8"))
    tcp_socket.send("exit\n".encode("utf-8"))
    tcp_socket.close()
    app.add_url_rule(path, view_func=handle_request, methods=["POST", "HEAD"])
    app.run(debug=False, host=host, port=port, threaded=False)

    # original_text = """2003年のリリース以来進化を続けるVOCALOID。
    # VOCALOID6ではAI技術を用いたVOCALOID:AIを搭載し、よりナチュラルで表現力豊かな歌声合成を実現しました。
    # 更に便利になった編集ツールと新機能が楽曲制作の自由度を高め、あなたのクリエイティビティを解放します。"""
    # print(_translate(original_text, 3, device, None, "t2s"))
    # tcp_socket.send(type(_translate(original_text, 3, device, None, "t2s")) + "\n")
    # translator.terminate()
    # tcp_socket.send("exit\n".encode("utf-8"))
    # tcp_socket.close()
