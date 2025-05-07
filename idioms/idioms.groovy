/**
 * Hubitat Balboa Hot Tub Driver Integration by Kurt Sanders 2025
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

@Field static final String  IDIOMS_FNAME = "Idioms_phrases_map"

metadata {
    definition(name: "Idioms", namespace: NAMESPACE, author: AUTHOR_NAME) {
        capability "Actuator"
        capability "Sensor"
        capability "Refresh"
        
        attribute "idiomKey"	, "number"
        attribute "phrase"		, "string"
        attribute "definition"	, "string"
        attribute "idiom"		, "string"
        attribute "error"		, "string"
        
        command "resetKey", [[name: "Idiom Key Number*", type: "NUMBER", description: "Resets the idiom key"]]
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
    logInfo "Setting Inital logging level to 'Debug' for 30 minutes"
    state.idiomNextKey = 0
}

def updated() {
    logInfo "updated..."
    logDebug "Debug logging is: ${logEnable == true}"
    checkLogLevel()  // Set Logging Objects
    state.remove('url')
    
}

def resetKey(num=1) {
    state.remove("idiomKey")
    state.idiomNextKey = num
    refresh()
}

def refresh() {
    Integer idiomKey = (state.idiomNextKey)?:1
    logDebug "idiomKey= ${idiomKey}"
    def idioms_fn_index = 1
    def uri = "http://${location.hub.localIP}:8080/local/${IDIOMS_FNAME}${idioms_fn_index}.json"
    logDebug "Sending on GET request to → ${uri}"
    
    try {
        httpGet(['uri': uri, contentType: "application/json"]) { response ->
            logDebug "Json Response = ${response.data}"
            response.headers.each {
                logDebug "${it.name} : ${it.value}"
            }
            logDebug "==> response.success= ${response.success}"
            if (response.success) {
                Map mapIdiomData = response.data["dictionary"][idiomKey]
                def id = mapIdiomData["id"]
                sendEvent(name: "id", value: id, isStateChange: true)
                def phrase = mapIdiomData["phrase"]
                sendEvent(name: "phrase", value: phrase, isStateChange: true)
                def definition = mapIdiomData["definition"]
                sendEvent(name: "definition", value: definition, isStateChange: true)

                def idiom = "Your idiom for today is '${phrase}', which means '${definition}'."
                sendEvent(name: "idiom", value: idiom, isStateChange: true)
                
                sendEvent(name: "idiomKey", value: idiomKey)
                state.idiomNextKey = ++idiomKey
            }
            return
            if (response.success) {
                def jsonResponse = jsonSlurper.parseText(response.data)
                logDebug "jsonResponse = ${jsonResponse}"
            } else {
                logDebug "jsonResponse = Opps, I don't have any idioms for you today"                
            }
        }
    } catch (Exception e) {
        logWarn "Http Call to ${uri} failed: ${e.message}"
    }
    logDebug "Refresh Completed"
}
