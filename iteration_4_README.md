# CCN-Project-Group-12 Iteration 4 Information

## Project Title : Real Time Pose Estimation

### Task 1: Converting the url to https mode for web application on localhost.

- We were working on creating the backend server with the https url so that other computers can access
  that server using the camera as well. The https url is being generated by changing the configuration file of the readme and adding some certificates to it.

### Task 2: Creating client server between two devices.

- After the https url was generated for backend server, we tested on two devices as client and server where on computer will hit the url of the server running on another computer and run the machine learning prediction on it for pose estimation in real time using camera.

### Task 3: Testing web application and android end to end .

- In this iteration, we tested the web application completely and made sure that everything is working fine for web application in localhost and client server architecture way, for android we made sure that everything is working fine locally.

### Packages required to run the project

mediapipe==0.9.1.0 \
streamlit==1.21.0 \
streamlit-webrtc==0.45.0 \
numpy==1.24.2 \
opencv-python-headless==4.7.0.72 

### For https communication

By default the streamlit server will run on http and if we want to run to https then we should add the ssl certificates to config.toml file in ~/.streamlit/ folder.\
add following configuration in ~/.streamlit/config.toml for https communication.\
[server]\
sslCertFile = path-to-pem-file \
sslKeyFile = path-to-key-file

### Run the project

1. Clone this repository
2. Run the command `streamlit run main.py`
