import easyocr
import json
import numpy as np
import sys
import os
import socket
tcp_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_addr = ("localhost", 39621)
tcp_socket.connect(server_addr)
rootPath = sys.argv[1]
num=int(sys.argv[2])
count=int(sys.argv[3])
start=int(sys.argv[4])
start_name=sys.argv[5]
temp=int(sys.argv[6])
class NumpyEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, np.integer):
            return int(obj)
        elif isinstance(obj, np.floating):
            return float(obj)
        elif isinstance(obj, np.ndarray):
            return obj.tolist()
        return super(NumpyEncoder, self).default(obj)
def img_rec(rootPath,num,count,start,start_name,temp):
    reader = easyocr.Reader(['ch_sim', 'en'],gpu='cuda:0')
    now_num=0
    endResult = []
    if not os.path.exists(f'{rootPath}/result'): #判断所在目录下是否有该文件名的文件夹
        os.mkdir(f'{rootPath}/result') #创建多级目录用mkdirs，单击目录mkdi
    for i in range(start,start+count):
        tcp_socket.send((rootPath+"\\"+start_name+str(i)+".png\n").encode("utf-8"))
        result = reader.readtext(rootPath+"\\"+start_name+str(i)+".png")
        endResult.append(result)
        now_num+=1
        if now_num == num:
            now_num=0
            json_result = json.dumps(endResult,cls=NumpyEncoder, ensure_ascii=False)
            with open(f'{rootPath}/result/result{temp}.txt', 'w',encoding='utf-8') as f:
                f.write(json_result)
            endResult.clear()
            temp+=1
img_rec(rootPath,num,count,start,start_name,temp)
tcp_socket.send("exit\n".encode("utf-8"))
tcp_socket.close()