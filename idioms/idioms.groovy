/**
 * Idioms Driver Integration by Kurt Sanders 2025
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include kurtsanders.SanderSoft-Library
#include kurtsanders.Idioms-Library

@Field static final String  PARENT_DEVICE_NAME  = "Idioms"
@Field static final String  IDIOMS_GITHIB		= "https://raw.githubusercontent.com/KurtSanders/Hubitat-Fun/refs/heads/main/idioms/data_v2/Idioms.json"
@Field static final String  VERSION 			= "0.1.0"
@Field static final Number IDIOMS_PER_FILE 		= 613

metadata {
    definition(name: PARENT_DEVICE_NAME, namespace: NAMESPACE, author: AUTHOR_NAME) {
        capability "Actuator"
        capability "Sensor"
        capability "Refresh"
        capability "Switch"
        
        attribute "id"			, "number"
        attribute "phrase"		, "string"
        attribute "definition"	, "string"
        attribute "idiom"		, "string"
        attribute "error"		, "string"
        
        command "resetKey"			, [[name: "Idiom Key ID Number*"	, type: "NUMBER", description: "Resets the idiom MAP key"]]
    }
}

preferences {
    section() {
        //	Logging Levels & Help
		input name: "logLevel", type: "enum", title: fmtTitle("Logging Level"),
    		description: fmtDesc("Logs selected level and above"), defaultValue: 0, options: LOG_LEVELS
		input name: "logLevelTime", type: "enum", title: fmtTitle("Logging Level Time"),
    		description: fmtDesc("Time to enable Debug/Trace logging"),defaultValue: 0, options: LOG_TIMES
    	//  Display Help Link
		input name: "helpInfo", type: "hidden", title: fmtHelpInfo("Community Link")
	}
}

def installed() {
    setLogLevel("Debug", "30 Minutes")
    checkLogLevel()  // Set Logging Objects    
	log.info "Setting Inital logging level to 'Debug' for 30 minutes"
	log.info "Initializing Idioms Database..  Please wait"
    refresh()
	log.info "Idioms Database Initialization Completed"
}

def updated() {
    logInfo "updated..."
    checkLogLevel()  // Set Logging Objects    
    logDebug "Debug logging is: ${logEnable == true}"
    
    // State cleanup from 0.0.1
    state.remove('idiomsFileNum')
    state.remove('idiomsGitHubFileNumber')
    state.remove('dataFileSuffix')
    state.remove('idiomNextKey')
}

def resetKey(num=0) {
	state.idiomsPerFile = state.idiomsPerFile?:IDIOMS_PER_FILE 
    if (num <= state.idiomsPerFile) {
        state.idNextKey = num
        logInfo "Idioms Key id reset to: ${state.idNextKey}"
        sendEvent(name: error, value: "")
    } else {
        def msg = "Invalid Idiom Key #, must be <= ${state.maxRecsFile}"
        logErr msg
        sendEvent(name: error, value: msg)
    }
}

def on() {
    refresh()
    runIn(500,'off')
}

def off() {   
	sendEvent(name: 'switch', value: 'off')
}

def refresh() {
    state.idNextKey = (state.idNextKey)?:0
    logDebug "==> state.idNextKey= ${state.idNextKey}"
    state.idiomsPerFile = state.idiomsPerFile?:IDIOMS_PER_FILE 
    if (state.idNextKey > state.idiomsPerFile) {
        logInfo "Idioms EOF reached: Reseting Idioms Key to 0"
        state.idNextKey = 0
    }
    def uri = "${IDIOMS_GITHIB}"
    logDebug "Sending on GET request to → ${uri}"
    logDebug "==> state.idNextKey= ${state.idNextKey}"
    
    try {
        httpGet(['uri': uri, contentType: "application/json"]) { response ->
            logTrace "Json Response = ${response.data}"
            response.headers.each {
                logTrace "${it.name} → ${it.value}"
            }
            logTrace "==> response.success= ${response.success}"
            if (response.success) {
                state.idiomsPerFile = response.data["dictionary"].size()
                logDebug "==> state.idiomsPerFile= ${state.idiomsPerFile}"
                
                Map mapIdiomData = response.data["dictionary"][state.idNextKey]
                Integer id = mapIdiomData["id"]
                logDebug "==> id= ${id}"
                sendEvent(name: "id", value: id)
                def phrase = mapIdiomData["phrase"]
                logDebug "==> phrase= ${phrase}"
                sendEvent(name: "phrase", value: phrase, isStateChange: true)
                def definition = mapIdiomData["definition"]
                logDebug "==> definition= ${definition}"
                sendEvent(name: "definition", value: definition, isStateChange: true)

                def idiom = "Your idiom for today is '${phrase}', which means '${definition}'."
                logDebug "==> idiom= ${idiom}"
                sendEvent(name: "idiom", value: idiom, isStateChange: true)

                // Increment the state.id
                state.idNextKey = id + 1
            }
        }
    } catch (Exception e) {
        def msg = "Http Call to ${uri} failed: ${e.message}"
        logWarn msg
		sendEvent(name: error, value: msg)        
    }
    logDebug "Refresh Completed"
}
