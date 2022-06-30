import cv2
import time
import math as m
import mediapipe as mp
import numpy as np

rotate_z_calibrated = 22
rotate_x_calibrated = 0.1
rotate_y_calibrated = 100

mp_pose = mp.solutions.pose
pose = mp_pose.Pose()

def move_right_arm_duckie(angle):

    return

def move_head_duckie(rx, ry, rz):
    ## TODO: rotate prostetic head orsomething, maybe coorilate to other dance move
    # RX: Looking left or right, positive to the left and negative to the right
    # RY: Looking up or down, positive looking up and negative looking down
    # RZ: Tilting left or right, positive tilting head left and negative tilting head right

    return


if __name__ == "__main__":
    cap = cv2.VideoCapture(0)

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

        
        right_eye = (int(lm.landmark[lmPose.RIGHT_EYE].x*w)), (int(lm.landmark[lmPose.RIGHT_EYE].y*h))
        left_eye =  (int(lm.landmark[lmPose.LEFT_EYE].x*w)), (int(lm.landmark[lmPose.LEFT_EYE].y*h))
        mouth_mid = int(((int(lm.landmark[lmPose.MOUTH_RIGHT].x*w))+(int(lm.landmark[lmPose.MOUTH_LEFT].x*w)))/2), int(((int(lm.landmark[lmPose.MOUTH_RIGHT].y*h))+(int(lm.landmark[lmPose.MOUTH_LEFT].y*h)))/2)
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

        try:
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
            move_head_duckie(rotate_x,rotate_y,rotate_z)
        except:
            print("WARNING: NO HEAD?")
        
        ## print("RX,RY,RZ: " + str(rotate_x), str(rotate_y), str(rotate_z)) 

        try:
            right_wrist_x = (int(lm.landmark[lmPose.RIGHT_WRIST].x*w))
            right_wrist_y = (int(lm.landmark[lmPose.RIGHT_WRIST].y*h))
            right_shoulder_x = (int(lm.landmark[lmPose.RIGHT_SHOULDER].x*w))
            right_shoulder_y = (int(lm.landmark[lmPose.RIGHT_SHOULDER].y*h))
            right_hip_x = (int(lm.landmark[lmPose.RIGHT_HIP].x*w))
            right_hip_y = (int(lm.landmark[lmPose.RIGHT_HIP].y*h))
            right_torso_length = m.sqrt(m.pow(right_shoulder_x-right_hip_x, 2) + m.pow(right_shoulder_y-right_hip_y,2))
            right_arm_length = m.sqrt(m.pow(right_shoulder_x-right_wrist_x, 2) + m.pow(right_shoulder_y-right_wrist_y,2))

            right_0_x = int(right_shoulder_x - (right_torso_length/3))
            right_0_y = int(right_shoulder_y + (right_torso_length/3))
            right_1_x = int(right_shoulder_x + (right_torso_length/3))
            right_1_y = int(right_shoulder_y + (right_torso_length/3))
            right_2_x = int(right_shoulder_x + (right_torso_length/2))
            right_2_y = int(right_shoulder_y - (right_torso_length/10))
            right_3_x = right_shoulder_x
            right_3_y = int(right_shoulder_y - (right_torso_length/3))
            right_4_x = int(right_shoulder_x - (right_torso_length/2))
            right_4_y = int(right_shoulder_y - (right_torso_length/10))

            dt_r_0 = m.sqrt(m.pow(right_wrist_x - right_0_x, 2)+ m.pow(right_wrist_y - right_0_y, 2))
            dt_r_1 = m.sqrt(m.pow(right_wrist_x - right_1_x, 2)+ m.pow(right_wrist_y - right_1_y, 2))
            dt_r_2 = m.sqrt(m.pow(right_wrist_x - right_2_x, 2)+ m.pow(right_wrist_y - right_2_y, 2))
            dt_r_3 = m.sqrt(m.pow(right_wrist_x - right_3_x, 2)+ m.pow(right_wrist_y - right_3_y, 2))
            dt_r_4 = m.sqrt(m.pow(right_wrist_x - right_4_x, 2)+ m.pow(right_wrist_y - right_4_y, 2))
            
            if (dt_r_0 <= dt_r_0 and dt_r_0 <= dt_r_1 and dt_r_0 <= dt_r_2 and dt_r_0 <= dt_r_3 and dt_r_0 <= dt_r_4):
                image = cv2.line(image, (right_0_x,right_0_y), (right_shoulder_x, right_shoulder_y), (255, 0, 0), 4)
            elif (dt_r_1 <= dt_r_0 and dt_r_1 <= dt_r_1 and dt_r_1 <= dt_r_2 and dt_r_1 <= dt_r_3 and dt_r_1 <= dt_r_4):
                image = cv2.line(image, (right_1_x,right_1_y), (right_shoulder_x, right_shoulder_y), (255, 0, 0), 4)
            elif (dt_r_2 <= dt_r_0 and dt_r_2 <= dt_r_1 and dt_r_2 <= dt_r_2 and dt_r_2 <= dt_r_3 and dt_r_2 <= dt_r_4):
                image = cv2.line(image, (right_2_x,right_2_y), (right_shoulder_x, right_shoulder_y), (255, 0, 0), 4)
            elif (dt_r_3 <= dt_r_0 and dt_r_3 <= dt_r_1 and dt_r_3 <= dt_r_2 and dt_r_3 <= dt_r_3 and dt_r_3 <= dt_r_4):
                image = cv2.line(image, (right_3_x,right_3_y), (right_shoulder_x, right_shoulder_y), (255, 0, 0), 4)
            else:
                image = cv2.line(image, (right_4_x,right_4_y), (right_shoulder_x, right_shoulder_y), (255, 0, 0), 4)
        except:
            print("WARNING: RIGHT ARM NOT VISIBLE")
        
        try:
            left_wrist_x = (int(lm.landmark[lmPose.LEFT_WRIST].x*w))
            left_wrist_y = (int(lm.landmark[lmPose.LEFT_WRIST].y*h))
            left_shoulder_x = (int(lm.landmark[lmPose.LEFT_SHOULDER].x*w))
            left_shoulder_y = (int(lm.landmark[lmPose.LEFT_SHOULDER].y*h))
            left_hip_x = (int(lm.landmark[lmPose.LEFT_HIP].x*w))
            left_hip_y = (int(lm.landmark[lmPose.LEFT_HIP].y*h))
            left_torso_length = m.sqrt(m.pow(left_shoulder_x-left_hip_x, 2) + m.pow(left_shoulder_y-left_hip_y,2))
            left_arm_length = m.sqrt(m.pow(left_shoulder_x-left_wrist_x, 2) + m.pow(left_shoulder_y-left_wrist_y,2))

            left_0_x = int(left_shoulder_x - (left_torso_length/3))
            left_0_y = int(left_shoulder_y + (left_torso_length/3))
            left_1_x = int(left_shoulder_x + (left_torso_length/3))
            left_1_y = int(left_shoulder_y + (left_torso_length/3))
            left_2_x = int(left_shoulder_x + (left_torso_length/2))
            left_2_y = int(left_shoulder_y - (left_torso_length/10))
            left_3_x = left_shoulder_x
            left_3_y = int(left_shoulder_y - (left_torso_length/3))
            left_4_x = int(left_shoulder_x - (left_torso_length/2))
            left_4_y = int(left_shoulder_y - (left_torso_length/10))

            dt_l_0 = m.sqrt(m.pow(left_wrist_x - left_0_x, 2)+ m.pow(left_wrist_y - left_0_y, 2))
            dt_l_1 = m.sqrt(m.pow(left_wrist_x - left_1_x, 2)+ m.pow(left_wrist_y - left_1_y, 2))
            dt_l_2 = m.sqrt(m.pow(left_wrist_x - left_2_x, 2)+ m.pow(left_wrist_y - left_2_y, 2))
            dt_l_3 = m.sqrt(m.pow(left_wrist_x - left_3_x, 2)+ m.pow(left_wrist_y - left_3_y, 2))
            dt_l_4 = m.sqrt(m.pow(left_wrist_x - left_4_x, 2)+ m.pow(left_wrist_y - left_4_y, 2))
            
            if (dt_l_0 <= dt_l_0 and dt_l_0 <= dt_l_1 and dt_l_0 <= dt_l_2 and dt_l_0 <= dt_l_3 and dt_l_0 <= dt_l_4):
                image = cv2.line(image, (left_0_x,left_0_y), (left_shoulder_x, left_shoulder_y), (255, 0, 0), 4)
            elif (dt_l_1 <= dt_l_0 and dt_l_1 <= dt_l_1 and dt_l_1 <= dt_l_2 and dt_l_1 <= dt_l_3 and dt_l_1 <= dt_l_4):
                image = cv2.line(image, (left_1_x,left_1_y), (left_shoulder_x, left_shoulder_y), (255, 0, 0), 4)
            elif (dt_l_2 <= dt_l_0 and dt_l_2 <= dt_l_1 and dt_l_2 <= dt_l_2 and dt_l_2 <= dt_l_3 and dt_l_2 <= dt_l_4):
                image = cv2.line(image, (left_2_x,left_2_y), (left_shoulder_x, left_shoulder_y), (255, 0, 0), 4)
            elif (dt_l_3 <= dt_l_0 and dt_l_3 <= dt_l_1 and dt_l_3 <= dt_l_2 and dt_l_3 <= dt_l_3 and dt_l_3 <= dt_l_4):
                image = cv2.line(image, (left_3_x,left_3_y), (left_shoulder_x, left_shoulder_y), (255, 0, 0), 4)
            else:
                image = cv2.line(image, (left_4_x,left_4_y), (left_shoulder_x, left_shoulder_y), (255, 0, 0), 4)
        except:
            print("WARNING: LEFT ARM NOT VISIBLE")

        

        # Display.
        cv2.imshow('DanceTracker', image)
        if cv2.waitKey(5) & 0xFF == ord('q'):
            break

cap.release()
cv2.destroyAllWindows()
