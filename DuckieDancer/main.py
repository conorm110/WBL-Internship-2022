import cv2
import time
import math as m
import mediapipe as mp

rotate_z_calibrated = 22
rotate_x_calibrated = 0.1
rotate_y_calibrated = 100

mp_pose = mp.solutions.pose
pose = mp_pose.Pose()

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


        # Display.
        cv2.imshow('DanceTracker', image)
        if cv2.waitKey(5) & 0xFF == ord('q'):
            break

cap.release()
cv2.destroyAllWindows()
