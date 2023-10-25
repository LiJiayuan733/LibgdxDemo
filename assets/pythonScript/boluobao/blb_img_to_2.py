import cv2
import os
import sys
import socket
tcp_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_addr = ("localhost", 39621)
rootPath = sys.argv[1]
def img_to_2(rootPath):
    res = []
    #60 100 980 2180
    for path in os.listdir(rootPath):
        # check if current path is a file
        if os.path.isfile(os.path.join(rootPath, path)):
            res.append(os.path.join(rootPath, path))
    endResult = []
    for img_path in res:
        tcp_socket.send(img_path+"\n".encode("utf-8"))
        img = cv2.imread(img_path)
        img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        ret,img =cv2.threshold(img, 50, 255, cv2.THRESH_BINARY)
        cv2.imwrite(img_path,img)
img_to_2(rootPath)
tcp_socket.send("exit\n".encode("utf-8"))
tcp_socket.close()