/*
 *  Jokes and Humor App Manager
 *  -> App
 *
 *  Copyright 2025 Kurt Sanders
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
 */

#include kurtsanders.SanderSoft-Library
#include kurtsanders.Fun-Api-Responses-Library

import groovy.transform.Field

@Field static final String APP_NAME      			= "Fun Api Responses"
@Field static final String PARENT_DEVICE_NAME      	= "Fun Api Responses"
@Field static final String PARENT_DEVICE_TYPE_NAME  = "Fun Api Responses"
@Field static final String VERSION                 	= "0.0.1"


definition(
    name              : APP_NAME,
    namespace         : NAMESPACE,
    author            : AUTHOR_NAME,
    description       : "Installs 'Fun Api Responses' application, creates parent device and sets attributes & preferences.",
    category          : "",
    iconUrl           : "",
    iconX2Url         : "",
    installOnOpen	  : true,
    documentationLink : COMM_LINK,
    singleInstance    : true
) {
}

preferences {
    page(name: "mainPage")
}

def installed() {
    // Set logging to 'Debug for 30 minutes' during initial setup of the app
    log.info "Setting initial '${app.name}' level logging to 'Debug' for 30 Minutes..."
    setLogLevel("Debug", "30 Minutes")
    app.updateSetting("site",[value: getSites()[0], type:"enum"])
    setChildrenDevices()
    state.response = ' '  
}

def updated() {
    setChildrenDevices()
    // Update device attributes for jokes
    updateDeviceQueryKeys()
}

void setChildrenDevices(setStateOnly=false) {
    // Create child devices for each site
    Map childrenDeviceMap = [:]
    getSites().each {
        def devId 	= "${it}-${app.id}-${PARENT_DEVICE_TYPE_NAME}"
        if (!setStateOnly) createDataChildDevice(NAMESPACE, PARENT_DEVICE_TYPE_NAME, devId, it)
        childrenDeviceMap["${it}"] = "${devId}"
        }
    state.siteDevId = childrenDeviceMap
}

def testGroovy() {
    // Testing Area
    return
}

List getSites() {
    List sites = [] 
    SERVICES['sites'].each {
        sites << "${it.key}"
    }
	return sites.sort()
}
    
void updateDeviceQueryKeys() {
    // Update device attributes for sites with paramaters
    getSites().each {
        logTrace "Site = ${it}"
        if (SERVICES['sites'][it].parameters) {
            def d = getChildDevice(state.siteDevId[it])
            logTrace "d = ${it}"
            SERVICES['sites'][it].parameters.each {
		        logTrace "Parameter = ${it}"        
                switch (it) {
                case "include-tags":
	                d.sendEvent(name: "includeTags", value: includeTags?includeTags.toString().replaceAll("[\\[\\](){}]",""):' ')
                    break
                case "exclude-tags":
	                d.sendEvent(name: "excludeTags", value: excludeTags?excludeTags.toString().replaceAll("[\\[\\](){}]",""):' ')
                    break
                case "difficulty":
	                d.sendEvent(name: "includeTags", value: difficultyTags?difficultyTags.toString().replaceAll("[\\[\\](){}]",""):' ')
                    break
                default:
                    logErr "updateDeviceQueryKeys(): Unknown parameter '${it}'"
                break
                }
            }
        }
    }
}

def mainPage() {
    dynamicPage(name: "mainPage", uninstall: true, install: true ) {
        def parameters
        setChildrenDevices(setStateOnly=true)
        //Community Help Link
        section () {
            input name: "helpInfo", type: "hidden", title: fmtHelpInfo("Hubitat Community Support <u>WebLink</u> to ${app.name}")
            paragraph ("")
        }
        section(sectionHeader("${PARENT_DEVICE_TYPE_NAME} Device and Preferences Manager")) {
            def hubIP = location.hub.localIP
            def line = '<span><style>ul {list-style: none;}li {display: inline-block;margin-right: 15px;}</style>'
            line += '<ul>'
            getSites().each {
            	def d = getChildDevice(state.siteDevId[it])
                def deviceLink = "'http://${hubIP}/device/edit/${d.id}'"
                def hoverTitle = "title='View ${it} device in new browser tab'" 
                    def parentDeviceWebLink = "<a target='_blank' ${hoverTitle} rel='noopener noreferrer' href=${deviceLink}><strong>${d.label}</strong></a>"
                def boxGraphic = "<a href=${deviceLink} target='_blank' ${hoverTitle} > ${BOX_ARROW} </a>"
                line += "<li>${parentDeviceWebLink}${boxGraphic}</li>"
            }
            line += '</ul>'
            line += "The following ${getSites().size()} devices <strong>&#8593;</strong> have been automatically created for you that will contain the response. "
            line += "You will need to install the respective device's 'Push' momentary button(s) on your HE dashboard and/or use Rules/WebCore to command refresh the respective device."
            paragraph line
//            input "testGroovy", "button", title: "Test Groovy Code"
        }
        section(sectionHeader("REQUIRED Site Choice and Inputs")) {
	        if (site) {
                state.mysite = SERVICES['sites'][site]
                if (state.mysite.description) paragraph "${state.mysite.description}"
    	        parameters  = state.mysite?.parameters
            }
            input "site", "enum", title: "Select a Category", defaultValue: getSites()[0], width: 2, required: true,
                multiple: false, submitOnChange: true, options: getSites()
            if (state.mysite?.api) {  
                input "apiKey", "text"	, title: "Enter your API key from the ${state.mysite?.website}", 
                    required: true, width: 4, submitOnChange: true
            }
        }                      
        // Only display these optional input options once the getType is selected above
        if ((site && (state.mysite?.api && apiKey)) || (site && (state.mysite?.api==null))) {
            def title = (state.mysite?.parameters)?"OPTIONAL ${site} Filters & Test":"${site} Test"
            section(sectionHeader("${title}")) {
                input "testGet", "button", title: "Get a <strong>'${site}'</strong> Response", submitOnChange: true
                if (state.response) {
                    def responseOutput = "<style>table, th, td {border:1px solid black;}</style><table><tr><th style='text-align:center'>${site} Response</th></tr><tr><td>${getFormat('text-green',state.response)}</td></tr></table>"
                    if (state.mysite?.api && state.quotaLeft) {
    	                paragraph "You have <strong>${state.quotaLeft}</strong> tokens requests left today in your ${state.mysite?.website} account plan."
                    }
                	if (site == state.lastSite) paragraph "${responseOutput}"
                }
                if (state.mysite?.parameters) {
                    if (state.mysite?.parameters.contains("include-tags")) {
                        input "includeTags"		, "enum"	, title: getFormat('text-green', "The list of filters the ${site} should have."), width: 4, submitOnChange: true,
                            multiple: true, options: SERVICES['sites'][site]?.keywords
                    }
                    if (state.mysite?.parameters.contains("exclude-tags")) {
                        input "excludeTags"		, "enum"	, title: getFormat('text-red', "The list of filters the ${site} should <strong>NOT</strong> have."), width: 4,
                            submitOnChange: true, multiple: true, options: SERVICES['sites'][site]?.keywords
                    }
                    if (state.mysite?.parameters.contains("difficulty")) {
                        input "difficultyTags"		, "enum"	, title: getFormat('text-blue', "The difficulty level of the ${site}. Leave unselected to produce a variety."), width: 4,
                            submitOnChange: true, options: SERVICES['sites'][site]?.keywords
                    }
                }
            }
        }
        section(sectionHeader("Text To Speach <strong>(TTS)</strong> Output Options")) {
            input "ttsDevice", "capability.speechSynthesis"	, title: getFormat('text-blue', "Select Speach device to play the TTS response"), width: 4,
            submitOnChange: true, showFilter: false, multiple: false
            if (ttsDevice) {
	            List<String> deviceCommands = ttsDevice.supportedCommands.name
                if ((ttsDevice.typeName == "Echo Speaks Device") && (deviceCommands.contains('playAnnouncementAll'))) {
                    input "playAnnounceAllBool", "bool"	, title: getFormat('text-blue', "${(playAnnounceAllBool)?'Send to <strong>ALL</strong> Echo Speaks devices':'Send only to the <strong>ONE</strong> Echo Speaks device?'}"), 
                        width: 4, submitOnChange: true, multiple: false
        		}
            }
        }
        
        def ll 	= (logLevel)?LOG_LEVELS[getLogLevelInfo()['level']]:'?'
        def ls	= (logLevelTime)?LOG_TIMES[getLogLevelInfo()['time']]:'?'
        
        //Logging Options
        section(sectionHeader("Logging Options: Current: ${ll} for ${ls}"), hideable: true, hidden: true) {
            input name: "logLevel", type: "enum", title: fmtTitle("Logging Level"),
                description: fmtDesc("Logs selected level and above"), defaultValue: 3, submitOnChange: true, options: LOG_LEVELS
            input name: "logLevelTime", type: "enum", title: fmtTitle("Logging Level Time"), submitOnChange: true,
                description: fmtDesc("Time to enable Debug/Trace logging"), defaultValue: 10, options: LOG_TIMES
        }
    }
}

void appButtonHandler(String buttonName) {
    logDebug "==> Button Name = ${buttonName}"
    switch (buttonName) {
        case 'testGroovy':
	        testGroovy()
        break
        case 'testGet':
	        updateDeviceQueryKeys()
	        state.response = 'working...'
           	refresh(site)
        break
        default:
            break
    }
}

void refresh(selectedSite=site) {
    logTrace "==> selectedSite= ${selectedSite}"
    state.mysite = SERVICES['sites'][selectedSite]
    state.lastSite = selectedSite
    logTrace "==> state.mysite= ${state.mysite}"
    // Get site response
    def uri 	= SERVICES['sites'][selectedSite]['url']
    
    //Get uri path per selectedSite
    String path
    Integer rndNumIndex
    switch (selectedSite) {
        case 'Idioms':
		    Integer fileSuffixNum
            Integer rndNum = Math.abs(new Random().nextInt() % IDIOMS_MAX) + 1
        	rndNumIndex = (rndNum<100)?rndNum:rndNum.toString()[-2..-1].toInteger()
        	if (rndNum % 100 != 0) {
                int division = (rndNum / 100) + 1;
                fileSuffixNum = division * 100;
            } else {
                fileSuffixNum = Math.max(100, rndNum);
            }
        	logInfo "==> Idiom Random #: ${rndNum} of ${IDIOMS_MAX}.  This idiom, which is Idiom #${rndNumIndex}, is located in the Idioms-${fileSuffixNum}.json file."
	        path = String.format(state.mysite.path, fileSuffixNum)
        break
        default:
            path = state.mysite.path
        break
    }
    
    logInfo "==> Sending a httpGET request to '${uri}${path}' for '${selectedSite}' response"
    def d = getChildDevice(state.siteDevId[selectedSite])
    d.sendEvent(name: 'error'	, value: ' ')
    state.response = ''
    def params = [
        uri: 	uri,
        path:	path,
        contentType: "application/json",
    ]
    // Place apiKey into either the header or the query into the params
    if (state.mysite?.api) {
        params[state.mysite?.api.where] = [
            "${state.mysite?.api.name}": apiKey
            ]
    }
    // Check for query tags for sites that allow and add to query list
    if (state.mysite?.parameters) {    
        def queryParams = [:]
        if (includeTags) 	{queryParams["include-tags"] 	= includeTags.join(",")}
        if (excludeTags) 	{queryParams["exclude-tags"]	= excludeTags.join(",")}
        if (difficultyTags) {queryParams["difficulty"]		= difficultyTags}
        if (queryParams) params['query'] = queryParams
    }
    params.each {
        logTrace "httpGet params → ${it}"
    }
	//  Break here for review of the params
    //	return
    def httpResponse
    try {
        httpGet(params) { response ->
            httpResponse = response
            logTrace "==> response (${httpResponse?.status}) = ${httpResponse?.data}"
            logTrace "==> response.properties= ${httpResponse?.properties}"
        }
    } catch (Exception ex) {
    	logWarn "Http Call to ${uri} failed: ${ex.message}"
        d.sendEvent(name: "error", value: ex.toString())
        state.response = ''
        return
    }
    // Read Response Headers
    if (httpResponse?.headers) {
        String newName
        Integer newValue
        httpResponse.headers.each {
            logTrace "Header = ${it}"
            if (it.name.startsWith('X-Api')) {
                newValue = Float.valueOf(it.value).toInteger()
                logTrace "==> Working on it.name= ${it.name} = ${newValue}"
                switch ("${it.name}") {
                    case 'X-Api-Quota-Request':
                        newName = 'quotaRequest'
                    break
                    case 'X-Api-Quota-Used':
                        newName = 'quotaUsed'
                    break
                    case 'X-Api-Quota-Left':
                        state.quotaLeft = "${newValue.toString()}"
                        logTrace "==> state.quotaLeft= ${state.quotaLeft}"
                        newName = 'quotaLeft'
                    break
                    default:
                        logWarn "Invald Header = ${it.name}"
                        newName = ''
                        break
                }
                if (newName) {
                    logTrace "(Quota: ${newName} = ${newValue})"
                    d.sendEvent('name': "${newName}", 'value': newValue)
                } else {
                    logWarn  "newName is ${newName}: ${it.name}"  
                }
            }
        }
    } else {
        logWarn "No Response Headers returned from httpGet"
    }
    if (httpResponse.success) {
        // Check Response and create events
        def preResponse 		= "Today's ${selectedSite.replaceFirst('s$','')} is"
        def value 				= (state.mysite.response.value)?httpResponse.data[state.mysite.response.value]:''

        switch(selectedSite) {
            case 'Idioms':
            value 				= httpResponse?.data[selectedSite][rndNumIndex][state.mysite.response.value]                            
            def phrase 			= httpResponse?.data[selectedSite][rndNumIndex][state.mysite.response.phrase]
            response 			= "${preResponse} '${phrase}'.\n${value}"
        break
            case 'Poems':
            def author 			= httpResponse?.data[state.mysite.response.author]
            def title 			= httpResponse?.data[state.mysite.response.title]
            response 			= "${preResponse} '${title}' by '${author}'.\n${value}" 
        break
            case 'Quotes':    
            def author 			= httpResponse?.data[state.mysite.response.author]
            response 			= "${preResponse} is by '${author}'.\n${value}"
        break
            case 'Riddles':
            def riddle 			= httpResponse?.data[state.mysite.response.riddle]
            def difficulty 		= httpResponse?.data[state.mysite.response.difficulty]
            response 			= "Here is a ${difficulty} ${(difficulty?'difficult':'')} riddle for you.\n${riddle}.\n${value}"
        break
            default:
            response 			= "${preResponse} '${value}'."
        }
        logInfo "==> Response= ${response}"
        if (!response?.trim()) {    
            d.sendEvent(name: "error", value: 'No response found')
            response = "Opps, I couldn't find any witty ${selectedSite} for you today."  
            if (state.mysite?.parameters) response += " Maybe try some new or fewer include or exclude tags/filters?"
        }
        d.sendEvent(name: "response", value: response)
        state.response = response
        //Send to TTS Device if selected
        if (ttsDevice) {
            if (playAnnounceAllBool) {
                ttsDevice.playAnnouncementAll(response)
                logTrace "Sending to ${ttsDevice} to play on ALL Amazon Echo devices}"
            } else {
                logTrace "Sending to ${ttsDevice}"
                ttsDevice.play(response)
            }
        }
    } else {
        response = "Opps, I couldn't find any witty ${selectedSite} for you today."  
        d.sendEvent(name: "error", value: 'No response found')
        if (state.mysite?.parameters) response += " Maybe try some new or fewer include or exclude tags/filters?"
        state.response = response
    }
}

Void errorMessage(message) {
    def d = getChildDevice(state.siteDevId[site])
    d.sendEvent(name: "error", value: message)
    logErr message
}