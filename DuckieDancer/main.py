import cv2
import time
import math as m
import mediapipe as mp
import numpy as np
import http.server
import socketserver
import threading
from urllib.parse import urlparse
from urllib.parse import parse_qs
from networktables import NetworkTables
import sys
import logging
import math

rotate_z_calibrated = 22
rotate_x_calibrated = 0.1
rotate_y_calibrated = 100

head_x = 0
head_y = 0
head_z = 0 
r_arm = 0
l_arm = 0

mp_pose = mp.solutions.pose
pose = mp_pose.Pose()


def server():
    global head_x
    global head_y
    global head_z
    global r_arm
    global l_arm
    logging.basicConfig(level=logging.DEBUG)
    ip = "10.81.22.2"

    NetworkTables.initialize(server=ip)

    sd = NetworkTables.getTable("SmartDashboard")

    while True:
        sd.putNumber("head_x", head_x)
        sd.putNumber("head_y", head_y)
        sd.putNumber("head_z", head_z)
        sd.putNumber("r_arm", 180)
        sd.putNumber("l_arm", l_arm)
        time.sleep(0.01)
            

def dance_reader():
    global head_x
    global head_y
    global head_z
    global r_arm
    global l_arm
    cap = cv2.VideoCapture("http:/10.81.22.218:4747/video")
    width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    frame_size = (width, height)
    while cap.isOpened():
        success, image = cap.read()
        if not success:
            print("Null.Frames")
            break
        h, w = image.shape[:2]
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        keypoints = pose.process(image)
        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
        lm = keypoints.pose_landmarks
        lmPose = mp_pose.PoseLandmark
        ##image = cv2.rectangle(image, (0,0), (w,h), (0,0,0), 600)
        try:
            right_eye = (int(lm.landmark[lmPose.RIGHT_EYE].x*w)), (int(lm.landmark[lmPose.RIGHT_EYE].y*h))
            left_eye =  (int(lm.landmark[lmPose.LEFT_EYE].x*w)), (int(lm.landmark[lmPose.LEFT_EYE].y*h))
            mouth_mid = int(((int(lm.landmark[lmPose.MOUTH_RIGHT].x*w))+(int(lm.landmark[lmPose.MOUTH_LEFT].x*w)))/2), int(((int(lm.landmark[lmPose.MOUTH_RIGHT].y*h))+(int(lm.landmark[lmPose.MOUTH_LEFT].y*h)))/2)
            mouth_x = int(((int(lm.landmark[lmPose.MOUTH_RIGHT].x*w))+(int(lm.landmark[lmPose.MOUTH_LEFT].x*w)))/2)
            right_ear = (int(lm.landmark[lmPose.RIGHT_EAR].x*w)), (int(lm.landmark[lmPose.RIGHT_EAR].y*h))
            left_ear = (int(lm.landmark[lmPose.LEFT_EAR].x*w)), (int(lm.landmark[lmPose.LEFT_EAR].y*h))
            image = cv2.line(image, right_eye, left_eye, (255, 0, 0), 2)
            image = cv2.line(image, left_eye, mouth_mid, (255, 0, 0), 2)
            image = cv2.line(image, mouth_mid, right_eye, (255, 0, 0), 2)
            image = cv2.line(image, right_ear, left_ear, (0, 255, 0), 2)
            image = cv2.line(image, left_ear, mouth_mid, (0, 255, 0), 2)
            image = cv2.line(image, mouth_mid, right_ear, (0, 255, 0), 2)
            image = cv2.line(image, left_eye, left_ear, (0, 0, 255), 2)
            image = cv2.line(image, left_eye, right_ear, (0, 0, 255), 2)
            image = cv2.line(image, right_eye, left_ear, (0, 0, 255), 2)
            image = cv2.line(image, right_eye, right_ear, (0, 0, 255), 2)
            eye_level_y = ((int(lm.landmark[lmPose.RIGHT_EYE].y*h)) + (int(lm.landmark[lmPose.LEFT_EYE].y*h))) / 2
            ear_level_y = ((int(lm.landmark[lmPose.RIGHT_EAR].y*h)) + (int(lm.landmark[lmPose.LEFT_EAR].y*h))) / 2
            rotate_y = round(((eye_level_y - ear_level_y + rotate_z_calibrated) * -1), 3)
            rotate_x_r = (int(lm.landmark[lmPose.RIGHT_EYE].x*w)) - (int(lm.landmark[lmPose.RIGHT_EAR].x*w)) 
            rotate_x_l = (int(lm.landmark[lmPose.LEFT_EYE].x*w)) - (int(lm.landmark[lmPose.LEFT_EAR].x*w)) 
            rotate_x_size = abs(rotate_x_l + rotate_x_r) * rotate_x_calibrated
            rotate_x = round((((rotate_x_r+rotate_x_l)/2) * rotate_x_size), 3)
            eye_slope = (int(lm.landmark[lmPose.RIGHT_EYE].y*h) - int(lm.landmark[lmPose.LEFT_EYE].y*h)) / (int(lm.landmark[lmPose.RIGHT_EYE].x*w) - int(lm.landmark[lmPose.LEFT_EYE].x*w))
            ear_slope = (int(lm.landmark[lmPose.RIGHT_EAR].y*h) - int(lm.landmark[lmPose.LEFT_EAR].y*h)) / (int(lm.landmark[lmPose.RIGHT_EAR].x*w) - int(lm.landmark[lmPose.LEFT_EAR].x*w))
            rotate_z = round(((eye_slope + ear_slope) * rotate_y_calibrated), 3)
            head_x = rotate_x
            head_y = rotate_y
            head_z = rotate_z
        except:
            print("WARNING: NO HEAD?")
        try:
            right_wrist_x = (int(lm.landmark[lmPose.RIGHT_WRIST].x*w))
            right_wrist_y = (int(lm.landmark[lmPose.RIGHT_WRIST].y*h))
            right_shoulder_x = (int(lm.landmark[lmPose.RIGHT_SHOULDER].x*w))
            right_shoulder_y = (int(lm.landmark[lmPose.RIGHT_SHOULDER].y*h))
            right_shoulder_wrist_distance_x = right_shoulder_x - right_wrist_x # negative towards the left
            right_shoulder_wrist_distance_y = right_shoulder_y - right_wrist_y # negative going down
            r_arm_speed = 3
            if (right_shoulder_wrist_distance_y > 0):
                if (abs(right_shoulder_wrist_distance_x) < 55):
                    # target 90
                    if (r_arm < 90):
                        r_arm = r_arm + r_arm_speed
                    elif (r_arm > 90):
                        r_arm = r_arm - r_arm_speed
                    else:
                        r_arm = 90
                elif (right_shoulder_wrist_distance_x < 0):
                    # target 45
                    if (r_arm < 45):
                        r_arm = r_arm + r_arm_speed
                    elif (r_arm > 45):
                        r_arm = r_arm - r_arm_speed
                    else:
                        r_arm = 45
                else:
                    # target 135
                    if (r_arm < 135):
                        r_arm = r_arm + r_arm_speed
                    elif (r_arm > 135):
                        r_arm = r_arm - r_arm_speed
                    else:
                        r_arm = 135
            else:
                if (r_arm < r_arm_speed or r_arm > (180 - r_arm_speed)):
                    r_arm = r_arm
                elif(r_arm < 90):
                    r_arm = r_arm - r_arm_speed
                else:
                    r_arm = r_arm + r_arm_speed
            
        except:
            print("WARNING: RIGHT ARM NOT VISIBLE")
        try:
            left_wrist_x = (int(lm.landmark[lmPose.LEFT_WRIST].x*w))
            left_wrist_y = (int(lm.landmark[lmPose.LEFT_WRIST].y*h))
            left_shoulder_x = (int(lm.landmark[lmPose.LEFT_SHOULDER].x*w))
            left_shoulder_y = (int(lm.landmark[lmPose.LEFT_SHOULDER].y*h))
            left_shoulder_wrist_distance_x = left_shoulder_x - left_wrist_x # negative towards the left
            left_shoulder_wrist_distance_y = left_shoulder_y - left_wrist_y # negative going down
            l_arm_speed = 3
            if (left_shoulder_wrist_distance_y > 0):
                if (abs(left_shoulder_wrist_distance_x) < 55):
                    # target 90
                    if (l_arm < 90):
                        l_arm = l_arm + l_arm_speed
                    elif (l_arm > 90):
                        l_arm = l_arm - l_arm_speed
                    else:
                        r_arm = 90
                elif (left_shoulder_wrist_distance_x < 0):
                    # target 45
                    if (l_arm < 45):
                       l_arm = l_arm + l_arm_speed
                    elif (l_arm > 45):
                        l_arm = l_arm - l_arm_speed
                    else:
                        l_arm = 45
                else:
                    # target 135
                    if (l_arm < 135):
                        l_arm = l_arm + l_arm_speed
                    elif (l_arm > 135):
                        l_arm = l_arm - l_arm_speed
                    else:
                        l_arm = 135
            else:
                if (l_arm < l_arm_speed or l_arm > (180 - l_arm_speed)):
                    l_arm = l_arm
                elif(l_arm < 90):
                    l_arm = l_arm - l_arm_speed
                else:
                    l_arm = l_arm + l_arm_speed
            
            print(l_arm)
        except:
            print("WARNING: LEFT ARM NOT VISIBLE")

        ## Bthis some fruity ahh code sorry mb
        try:
            image = cv2.line(image, (right_shoulder_x,right_shoulder_y), (left_shoulder_x, left_shoulder_y), (255, 0, 0), 4)
            spine_end_y = int((right_shoulder_y+left_shoulder_y)/2) + 100
            spine_end_x = int((right_shoulder_x+left_shoulder_x)/2)
            image = cv2.line(image, mouth_mid, (spine_end_x, spine_end_y), (255,0,0), 4) # doesnt work
        except:
            i=0 # :skull:

        # Display.
        cv2.imshow('DanceTracker', image)
        if cv2.waitKey(5) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()


if __name__ == "__main__":
    t2 = threading.Thread(target=dance_reader, args=())
    t1 = threading.Thread(target=server,args=())
    t1.start()
    t2.start()



