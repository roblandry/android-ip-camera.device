This is a custom device type. This works with the Android IP Camera app. It allows you to take photos, record video, turn on/off the led, focus, overlay, and night vision. It displays various sensors including battery level, humidity, temperature, and light (lux). The sensor data is all dependent on what your phone supports.

1. First you must create the device type:
  https://graph.api.smartthings.com/ide/devices.
  Copy this code there. 
2. Then you must create the device:
  https://graph.api.smartthings.com/device/list.
3. Install the app on your phone. 
  https://play.google.com/store/apps/details?id=com.pas.webcam.pro.
  I recommend the paid version, but the free works as well. 
4. You have to adjust your router settings to allow the port to be forwarded to your phone.
  I set up static ip addresses for my phones with different ports forwarded. (ie 8091, 8092, ...). 
5. I also installed the paid version of dropsync. 
  https://play.google.com/store/apps/details?id=com.ttxapps.dropsync.pro.
  This will allow you to upload the videos/photos to a custom dropbox folder. 
  My setup is /IP Camera/{the camera name}.
6. Dont forget to setup the device in the smartthings mobile app. Use your external ip address.
  http://www.ipchicken.com will give this to you.
7. Also if you use this with the Action Dashboard: 
  http://community.smartthings.com/t/action-dashboard-4-6-3-is-here/8149.
  You will need to set the url to "http://username:password@{hostname or ip}:port/video"

Hope you like it. Feel free to provide feedback.

