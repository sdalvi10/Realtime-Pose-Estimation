""" MEDIAPIPE POSE ESTIMATION USING HOLISTIC """

import av
import cv2
import streamlit as st
from streamlit_webrtc import WebRtcMode,webrtc_streamer
from pose_estimation.pose_estimation import get_post_image



def callback(frame: av.VideoFrame) -> av.VideoFrame:
    img = frame.to_ndarray(format="bgr24")

    output = get_post_image(img)

    return av.VideoFrame.from_ndarray(output, format="bgr24")


webrtc_streamer(
    key="mediapipe",
    mode=WebRtcMode.SENDRECV,
    rtc_configuration={"iceServers": [{"urls": ["stun:stun.l.google.com:19302"]}]},
    video_frame_callback=callback,
    media_stream_constraints={"video": True, "audio": False},
    async_processing=True,
)

st.markdown(
    "GROUP 12 : REAL TIME POSE ESTIMATION"
)