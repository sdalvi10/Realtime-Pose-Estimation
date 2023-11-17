import cv2
import mediapipe as mp
import numpy as np
mp_drawing = mp.solutions.drawing_utils
mp_drawing_styles = mp.solutions.drawing_styles
mp_holistic = mp.solutions.holistic
    
    
def get_post_image(input_image):
  with mp_holistic.Holistic(
    static_image_mode=True, min_detection_confidence=0.5, model_complexity=2) as holistic:
    #input_image = cv2.imread(image_file)
    #print(type(input_image))
    processed_input_image = holistic.process(cv2.cvtColor(input_image, cv2.COLOR_BGR2RGB))

    updated_image_with_pose = input_image.copy()
    mp_drawing.draw_landmarks(updated_image_with_pose, processed_input_image.left_hand_landmarks, mp_holistic.HAND_CONNECTIONS)
    mp_drawing.draw_landmarks(updated_image_with_pose, processed_input_image.right_hand_landmarks, mp_holistic.HAND_CONNECTIONS)
    mp_drawing.draw_landmarks(
        updated_image_with_pose,
        processed_input_image.face_landmarks,
        mp_holistic.FACEMESH_TESSELATION,
        landmark_drawing_spec=None,
        connection_drawing_spec=mp_drawing_styles.get_default_face_mesh_tesselation_style())
    mp_drawing.draw_landmarks(
        updated_image_with_pose,
        processed_input_image.pose_landmarks,
        mp_holistic.POSE_CONNECTIONS,
        landmark_drawing_spec=mp_drawing_styles.
        get_default_pose_landmarks_style())
    return updated_image_with_pose
    #cv2.imwrite('output' + '.png', updated_image_with_pose)
    
    
if __name__ == "__main__":
    get_post_image("yoga_input_sample1.jpeg")