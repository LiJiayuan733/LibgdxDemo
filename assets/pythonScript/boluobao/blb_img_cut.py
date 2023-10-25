import cv2
import os
import sys
import socket
tcp_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_addr = ("localhost", 39621)
tcp_socket.connect(server_addr)
rootPath = sys.argv[1]
x1=int(sys.argv[2])
y1=int(sys.argv[3])
x2=int(sys.argv[4])
y2=int(sys.argv[5])
def img_cut(rootPath,x1,y1,x2,y2):
    res = []
    #60 100 980 2180
    for path in os.listdir(rootPath):
        # check if current path is a file
        if os.path.isfile(os.path.join(rootPath, path)):
            res.append(os.path.join(rootPath, path))
    for img_path in res:
        tcp_socket.send((img_path+"\n").encode("utf"))
        img = cv2.imread(img_path)
        img=img[y1:y2,x1:x2]
        cv2.imwrite(img_path,img)
img_cut(rootPath,x1,y1,x2,y2)
tcp_socket.send("exit\n".encode("utf-8"))
tcp_socket.close()