/**
 *  Sure PetCare Pet Door Connect
 *
 *  Copyright 2019 Alex Lee Yuk Cheung
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  VERSION HISTORY
 *  11.12.2019 - v1.2.2 - Thanks for @solarfusion, new battery calculations.
 *  08.10.2019 - v1.2.1b - Change lock behaviour to 'Pet In' rather than lock both ways
 *  13.09.2019 - v1.2.1 - Add curfew status tile
 *  10.09.2019 - v1.2 - Add button controls to change lock status
 *  10.09.2019 - v1.1b - Improve API call efficiency
 *  09.09.2019 - v1.1 - Added Keep Pet In option for Dual Scan devices
 *  06.09.2019 - v1.0 - Initial Version
 */
 

metadata {
	definition (name: "Sure PetCare Pet Door Connect", namespace: "alyc100", author: "Alex Lee Yuk Cheung") {
		capability "Polling"
		capability "Refresh"
        capability "Actuator"
        capability "Health Check"
        capability "Battery"
		capability "Lock"
		capability "Sensor"
        
        attribute "network","string"
        
        command "toggleLockMode"
        command "setLockMode", ["string"]
        command "setLockModeToBoth"
        command "setLockModeToIn"
        command "setLockModeToOut"
        command "setLockModeToNone"
	}

	tiles(scale: 2) {
    	multiAttributeTile(name: "flap", width: 6, height: 4, type:"generic") {
			tileAttribute("device.lockMode", key:"PRIMARY_CONTROL", canChangeBackground: true){
				attributeState("both", label: 'LOCKED', action: "toggleLockMode", icon: "https://raw.githubusercontent.com/alyc100/SmartThingsPublic/master/devicetypes/alyc100/surepetcare-flap-lock.png", backgroundColor: "#ef6d6a", nextState:"waiting")
				attributeState("out", label: 'PETS OUT', action: "toggleLockMode", icon: "https://raw.githubusercontent.com/alyc100/SmartThingsPublic/master/devicetypes/alyc100/surepetcare-flap-out.png", backgroundColor: "#f88e4c", nextState:"waiting")
                attributeState("in", label: 'PETS IN', action: "toggleLockMode", icon: "https://raw.githubusercontent.com/alyc100/SmartThingsPublic/master/devicetypes/alyc100/surepetcare-flap-in.png", backgroundColor: "#81cb65", nextState:"waiting")
				attributeState("none", label: 'UNLOCKED', action: "toggleLockMode", icon: "https://raw.githubusercontent.com/alyc100/SmartThingsPublic/master/devicetypes/alyc100/surepetcare-flap-unlock.png", backgroundColor: "#33a1ff", nextState:"waiting")
            	attributeState("waiting", label:'Please Wait...', backgroundColor:"#ffffff")
            }
		}
        
        standardTile("both", "device.switch", width: 1, height: 1, inactiveLabel: false, canChangeIcon: false) {
			state("default", action:"setLockModeToBoth", icon:"https://raw.githubusercontent.com/alyc100/SmartThingsPublic/master/devicetypes/alyc100/surepetcare-flap-lock.png")
		}
        standardTile("out", "device.switch", width: 1, height: 1, inactiveLabel: false, canChangeIcon: false) {
			state("default", action:"setLockModeToOut", icon:"https://raw.githubusercontent.com/alyc100/SmartThingsPublic/master/devicetypes/alyc100/surepetcare-flap-out.png")
		}
        standardTile("in", "device.switch", width: 1, height: 1, inactiveLabel: false, canChangeIcon: false) {
			state("default", action:"setLockModeToIn", icon:"https://raw.githubusercontent.com/alyc100/SmartThingsPublic/master/devicetypes/alyc100/surepetcare-flap-in.png")
		}
        standardTile("none", "device.switch", width: 1, height: 1, inactiveLabel: false, canChangeIcon: false) {
			state("default", action:"setLockModeToNone", icon:"https://raw.githubusercontent.com/alyc100/SmartThingsPublic/master/devicetypes/alyc100/surepetcare-flap-unlock.png")
		}
        valueTile("serial_number", "device.serial_number", decoration: "flat", width: 3, height: 1) {
			state "default", label: 'Serial Number:\n${currentValue}'
		}
        valueTile("mac_address", "device.mac_address", decoration: "flat", width: 3, height: 1) {
			state "default", label: 'MAC Address:\n${currentValue}'
		}
        valueTile("created_at", "device.created_at", decoration: "flat", width: 3, height: 1) {
			state "default", label: 'Created at:\n${currentValue}'
		}
        valueTile("updated_at", "device.updated_at", decoration: "flat", width: 3, height: 1) {
			state "default", label: 'Updated at:\n${currentValue}'
		}
        valueTile("curfewStatus", "device.curfewStatus", decoration: "flat", width: 5, height: 1) {
			state "default", label: '${currentValue}'
		}
        standardTile("empty", "device.empty", decoration: "flat", inactiveLabel: false, width: 1, height: 1) {
			state "default", label: ''
		}
        valueTile("battery", "device.battery", decoration: "flat", inactiveLabel: false, width: 2, height: 2) {
			state "battery", label:'${currentValue}%\nbattery', unit:""
		}
        valueTile("device_rssi", "device.device_rssi", canChangeBackground: true, width: 2, height: 2){
			state "default", label: 'RSSI ${currentValue}',
            backgroundColors:[
				[value: -50, color: "#33cc33"],
                [value: -60, color: "#ffff99"],
                [value: -70, color: "#ff9933"],
                [value: -100, color: "#ff0000"]
			]
		}
        standardTile("network", "device.network", width: 2, height: 2, inactiveLabel: false, canChangeIcon: false) {
			state ("default", label:'unknown', icon: "st.unknown.unknown.unknown")
			state ("Connected", label:'Online', icon: "st.Health & Wellness.health9", backgroundColor: "#79b821")
			state ("Pending", label:'Pending', icon: "st.Health & Wellness.health9", backgroundColor: "#ffa500")
			state ("Not Connected", label:'Offline', icon: "st.Health & Wellness.health9", backgroundColor: "#bc2323")
		}
		standardTile("refresh", "device.refresh", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
		}
        
		main (["flap"])
		details(["flap", "curfewStatus", "empty", "both", "out", "battery", "device_rssi", "in", "none",  "serial_number", "mac_address", "created_at", "updated_at", "network", "refresh"])
	}
}

// handle commands
def installed() {
    sendEvent(name: "checkInterval", value: 1 * 60 * 60, data: [protocol: "cloud"], displayed: false)
}

def updated() {
    sendEvent(name: "checkInterval", value: 1 * 60 * 60, data: [protocol: "cloud"], displayed: false)
}

def poll() {
	log.debug "Executing 'poll'"
	
    if (!state.statusRespCode || state.statusRespCode != 200) {
		log.error("Unexpected result in poll(): [${state.statusRespCode}] ${state.statusResponse}")
		return []
	}
    def response = state.statusResponse.data.devices
    def flap = response.find{device.deviceNetworkId.toInteger() == it.id}
    sendEvent(name: 'product_id', value: flap.product_id)
    sendEvent(name: "serial_number", value: flap.serial_number)
    sendEvent(name: "mac_address", value: flap.mac_address)
    sendEvent(name: "created_at", value: flap.created_at)
    sendEvent(name: "updated_at", value: flap.updated_at)
    def curfewStatus
    if (flap.control.curfew && !flap.control.curfew.isEmpty()) {
    	curfewStatus = "A curfew is activated on this device between ${flap.control.curfew[0].lock_time} and ${flap.control.curfew[0].unlock_time}."
    } else {
    	curfewStatus = "A curfew is not enabled on this device."
    }
    sendEvent(name: "curfewStatus", value: curfewStatus)
    def lockMode
    switch (flap.status.locking.mode) {
    	case 0:
            lockMode = "none"
       		break;
        case 1:
        	lockMode = "in"
            break;
        case 2:
        	lockMode = "out"
            break;
        default:
        	lockMode = "both"
			break;
    }
    if (lockMode == "none" || lockMode == "out") {
    	 sendEvent(name: "lock", value: "unlocked")
    } else {
    	 sendEvent(name: "lock", value: "locked")
    }
    sendEvent(name: "lockMode", value: lockMode)
    if (flap.status.online) {
    	sendEvent(name: 'network', value: "Connected" as String)
    } else {
    	sendEvent(name: 'network', value: "Pending" as String)
    }
    def batteryPercent = getBatteryPercent(flap.status.battery)
    sendEvent(name: 'battery', value: batteryPercent)
    sendEvent(name: 'device_rssi', value: flap.status.signal.device_rssi)
    sendEvent(name: 'hub_rssi', value: flap.status.signal.hub_rssi)
}

def toggleLockMode() {
	log.debug "Executing 'toggleLockMode'"
    def lockMode
	if (device.currentState("lockMode").getValue() == "both") { 
    	lockMode = "in"
    } else if (device.currentState("lockMode").getValue() == "in")  {
    	lockMode = "out"
    } else if (device.currentState("lockMode").getValue() == "out")  {
    	lockMode = "none"
    } else {
    	lockMode = "both"
    }
    setLockMode(lockMode)
}

def setLockMode(mode) {
	log.debug "Executing 'setLockMode' with mode ${mode}"
	def modeValue
	switch (mode) {
    	case "none":
            modeValue = 0
       		break;
        case "in":
        	modeValue = 1
            break;
        case "out":
        	modeValue = 2
            break;
        case "both":
        	modeValue = 3
			break;
        default:
        	log.error("Unsupported lock mode: [${mode}]")
        	return []
    }
	def body = [
    	locking: modeValue
    ]
	def resp = parent.apiPUT("/api/device/" + device.deviceNetworkId + "/control", body)
    sendEvent(name: "lockMode", value: mode)
    runIn(2, "updateStatusAndRefresh")
}

def lock() {
	log.debug "Executing 'lock'"
	setLockMode("in")
}

def unlock() {
	log.debug "Executing 'unlock'"
	setLockMode("none")
}

def getBatteryPercent(voltage) {
	log.debug "Executing 'getBatteryPercent'"
	log.debug "Battery voltage " + voltage + " volts"
	/* Battery voltages
	6.27v = new batteries
	5.2v = flap starts misbehaving i.e. batteries dead
	0.27v increments for each 25% = 6.27, 6, 5.8, 5.5, 5.2
	*/
	def percentage = 100
	if (voltage < 5.2) {
    	percentage = 0
	} else if (voltage < 5.5) {
    	percentage = 25
	} else if (voltage < 5.8) {
    	percentage = 50
	} else if (voltage < 6) {
    	percentage = 75
	}
	return percentage
}

def refresh() {
	log.debug "Executing 'refresh'"
	poll()    
}

def updateStatusAndRefresh() {
	log.debug "Executing 'updateStatusAndRefresh'"
    def resp = parent.apiGET("/api/me/start")
    setStatusRespCode(resp.status)
    setStatusResponse(resp.data)
    refresh()
}

def setStatusRespCode(respCode) {
	state.statusRespCode = respCode
}

def setStatusResponse(respBody) {
	state.statusResponse = respBody
}

def setLockModeToBoth() {
	setLockMode("both")
}

def setLockModeToIn() {
	setLockMode("in")
}

def setLockModeToOut() {
	setLockMode("out")
}

def setLockModeToNone() {
	setLockMode("none")
}