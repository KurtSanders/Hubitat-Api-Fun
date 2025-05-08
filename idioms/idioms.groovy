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
@Field static final String  IDIOMS_FNAME 		= "Idioms_phrases_map"
@Field static final String  IDIOMS_GITHIB		= "https://raw.githubusercontent.com/KurtSanders/Hubitat-Fun/refs/heads/main/idioms/data/"
@Field static final Integer IDIOMS_PER_FILE 	= 250
@Field static final Integer IDIOMS_FILES 		= 85
@Field static final String  VERSION 			= "0.0.1"


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
        command "idiomsFileNumber"	, [[name: "Idioms File Number 1-85*", type: "NUMBER", description: "Select the Idioms File Number 1-85", range: "1..85"]]

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
    state.idiomNextKey = 0
	log.info "Initializing Idioms Database..  Please wait"
    refresh()
	log.info "Idioms Database Initialization Completed"
}

def updated() {
    logInfo "updated..."
    checkLogLevel()  // Set Logging Objects    
    logDebug "Debug logging is: ${logEnable == true}"
}

def idiomsFileNumber(num=1) {
    if (num > 1 || num <=85) {
        logInfo "Idioms File Number changed to #${num}"
        state.dataFileSuffix = num
    } else {
         logErr "Invalid Idioms File Number '${num}'.  Must be an Integer between 1 and 85"
    }    
}

def resetKey(num=0) {
    state.remove("idiomKey")
    state.idNextKey = num
    refresh()
}

def on() {
    refresh()
    runIn(1000,'off')
}

def off() {   
	sendEvent(name: 'switch', value: 'off')
}

def refresh() {
    state.idNextKey = (state.idNextKey)?:0
    state.dataFileSuffix = (state.dataFileSuffix)?:1
    if (state.idNextKey > IDIOMS_PER_FILE) {
        state.dataFileSuffix = state.dataFileSuffix + 1
        if (state.dataFileSuffix > IDIOMS_FILES) state.dataFileSuffix = 1
        state.idNextKey = 0
    }
    def uri = "${IDIOMS_GITHIB}${IDIOMS_FNAME}${state.dataFileSuffix}.json"
    logDebug "Sending on GET request to → ${uri}"
    
    try {
        httpGet(['uri': uri, contentType: "application/json"]) { response ->
            logTrace "Json Response = ${response.data}"
            response.headers.each {
                logTrace "${it.name} → ${it.value}"
            }
            logTrace "==> response.success= ${response.success}"
            if (response.success) {
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
        logWarn "Http Call to ${uri} failed: ${e.message}"
    }
    logDebug "Refresh Completed"
}
