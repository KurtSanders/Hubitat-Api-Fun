/**
 * Fun Api Responses Driver Integration by Kurt Sanders 2025
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
#include kurtsanders.Fun-Api-Responses-Library

@Field static final String  VERSION 			= "0.0.1"

metadata {
    definition(name: "Fun Api Responses Driver", namespace: "kurtsanders", author: "Kurt Sanders") {
        capability "Actuator"
        capability "Sensor"
        capability "Refresh"
		capability "Momentary"
        
        attribute "response"			, "string"
        attribute "error"				, "string"
        attribute "quotaRequest"		, "number"
        attribute "quotaUsed"			, "number"
        attribute "quotaLeft"			, "number"
        attribute "includeTags"			, "string"
        attribute "excludeTags"			, "string"
    }
}

preferences {    
        //	Logging Levels & Help
		input name: "logLevel", type: "enum", title: fmtTitle("Logging Level"),
    		description: fmtDesc("Logs selected level and above"), defaultValue: 0, options: LOG_LEVELS
		input name: "logLevelTime", type: "enum", title: fmtTitle("Logging Level Time"),
    		description: fmtDesc("Time to enable Debug/Trace logging"),defaultValue: 0, options: LOG_TIMES
    	//  Display Help Link
		input name: "helpInfo", type: "hidden", title: fmtHelpInfo("Community Link")
}

def push() {
    refresh()
}

def installed() {
    setLogLevel("Debug", "30 Minutes")
    checkLogLevel()  // Set Logging Objects    
	log.info "Setting Inital logging level to 'Debug' for 30 minutes"
}

def updated() {
    logInfo "updated..."
    checkLogLevel()  // Set Logging Objects    
    logDebug "Debug logging is: ${logEnable == true}"    
    sendEvent(name: 'error', value: ' ')
}

def refresh() {
    parent.refresh(device.deviceNetworkId.split('-')[0])   
}