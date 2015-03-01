/** Android IP Camera
 *
 * Author: rob.a.landry@gmail.com
 * 
 * Author: http://github.com/roblandry
 * Date: 2/28/15
 */

preferences
{
	input("username",	"text",		title: "Camera username",	description: "Username for web login")
	input("password",	"password",	title: "Camera password",	description: "Password for web login")
	input("url",		"text",		title: "IP or URL of camera",	description: "Do not include http://")
	input("port",		"text",		title: "Port",			description: "Port")
}

metadata {
	definition (name: "Android IP Camera", author: "Rob Landry", namespace: "roblandry") {
		capability "Image Capture"
		capability "Switch"
		capability "Actuator"

		command "ledOn"
		command "ledOff"
		command "focusOn"
		command "focusOff"
		command "overlayOn"
		command "overlayOff"
		command "nightVisionOn"
		command "nightVisionOff"

	}

	tiles {
		carouselTile("cameraDetails", "device.image", width: 3, height: 2) { }

		standardTile("camera", "device.image", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: true) {
			state "default", label: '', action: "Image Capture.take", icon: "st.camera.camera", backgroundColor: "#FFFFFF"
		}

		standardTile("take", "device.image", width: 1, height: 1, canChangeIcon: false, inactiveLabel: true, canChangeBackground: false, decoration: "flat") {
			state "take", label: 'Take Photo', action: "Image Capture.take", icon: "st.camera.take-photo", nextState:"taking"
			state "taking", label: 'Taking...', action: "Image Capture.take", icon: "st.camera.take-photo", backgroundColor: "#79b821"
		}

		standardTile("record", "device.switch", width: 1, height: 1) {
			state "off", label: 'Record Off', action:"switch.on", icon:"st.switches.light.off", backgroundColor: "#ffffff"
			state "on", label: 'Record On', action:"switch.off", icon:"st.switches.light.on", backgroundColor: "#79b821"
		}

		standardTile("led", "device.led", width: 1, height: 1) {
			state "off", label: 'Led Off', action:"ledOn", icon:"st.switches.light.off", backgroundColor: "#ffffff"
			state "on", label: 'Led On', action:"ledOff", icon:"st.switches.light.on", backgroundColor: "#79b821"
		}

		standardTile("focus", "device.focus", width: 1, height: 1) {
			state "off", label: 'Focus Off', action:"focusOn", icon:"st.switches.light.off", backgroundColor: "#ffffff"
			state "on", label: 'Focus On', action:"focusOff", icon:"st.switches.light.on", backgroundColor: "#79b821"
		}

		standardTile("overlay", "device.overlay", width: 1, height: 1) {
			state "off", label: 'Overlay Off', action:"overlayOn", icon:"st.switches.light.off", backgroundColor: "#ffffff"
			state "on", label: 'Overlay On', action:"overlayOff", icon:"st.switches.light.on", backgroundColor: "#79b821"
		}

		standardTile("nightVision", "device.nightVision", width: 1, height: 1) {
			state "off", label: 'Night Vision Off', action:"nightVisionOn", icon:"st.switches.light.off", backgroundColor: "#ffffff"
			state "on", label: 'Night Vision On', action:"nightVisionOff", icon:"st.switches.light.on", backgroundColor: "#79b821"
		}

		main "camera"
		details(["cameraDetails","take","record","led","focus","overlay", "nightVision"])
	}
}


def parseCameraResponse(def response) {
	if(response.headers.'Content-Type'.contains("image/jpeg")) {
		def imageBytes = response.data

		if(imageBytes) {
			storeImage(getPictureName(), imageBytes)
		}
	} else {
		log.error("${device.label} could not capture an image.")
	}
}

private getPictureName() {
	def pictureUuid = java.util.UUID.randomUUID().toString().replaceAll('-', '')
	"image" + "_$pictureUuid" + ".jpg"
}

private take() {
	log.info("${device.label} taking photo")

	httpGet("http://${username}:${password}@${url}:${port}/photo_save_only.jpg"){
		//httpGet("http://${username}:${password}@${url}:${port}/photo_save_only.jpg")
		//httpGet("http://${username}:${password}@${url}:${port}/photo_save_only.jpg")
		httpGet("http://${username}:${password}@${url}:${port}/photo.jpg"){
			response -> log.info("${device.label} image captured")
			parseCameraResponse(response)
		}
	}
}

def on(theSwitch="record") {
	def sUrl
	switch ( theSwitch ) {
		case "led":
			sUrl = "enabletorch"
			break

		case "focus":
			sUrl = "focus"
			break

		case "overlay":
			sUrl = "settings/overlay?set=on"
			break

		case "nightVision":
			sUrl = "settings/night_vision?set=on"
			break

		default:
			sUrl = "/startvideo?force=1"
	}

	httpGet("http://${username}:${password}@${url}:${port}/${sUrl}"){
		response -> log.info("${device.label} ${theSwitch} On")
		sendEvent(name: "${theSwitch}", value: "on")
	}

}

def off(theSwitch="record") {
	def sUrl
	switch ( theSwitch ) {
		case "led":
			sUrl = "disabletorch"
			break

		case "focus":
			sUrl = "nofocus"
			break

		case "overlay":
			sUrl = "settings/overlay?set=off"
			break

		case "nightVision":
			sUrl = "settings/night_vision?set=off"
			break

		default:
			sUrl = "stopvideo?force=1"
	}

	httpGet("http://${username}:${password}@${url}:${port}/${sUrl}"){
		response -> log.info("${device.label} ${theSwitch} Off")
		sendEvent(name: "${theSwitch}", value: "off")
	}

}

def ledOn() { on("led") }

def ledOff() { off("led") }

def focusOn() { on("focus") }

def focusOff() { off("focus") }

def overlayOn() { on("overlay") }

def overlayOff() { off("overlay") }

def nightVisionOn() { on("nightVision") }

def nightVisionOff() { off("nightVision") }