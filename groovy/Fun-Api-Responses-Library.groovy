/*******************************************************************
*** SanderSoft - Core App/Device Helpers                        ***
/*******************************************************************/

import groovy.transform.Field

@Field static final String 	AUTHOR_NAME          	= "Kurt Sanders"
@Field static final String 	NAMESPACE            	= "kurtsanders"
@Field static final String  PARENT_DEVICE_NAME      = "Fun Api Responses Driver"
@Field static final String  PARENT_DEVICE_TYPE_NAME = "Fun Api Responses Driver"
@Field static final String 	COMM_LINK            	= "https://community.hubitat.com/t/release-fun-api-response-app-idioms-jokes-trivia-poems-quotes-riddles/153331"
@Field static final String 	GITHUB_LINK          	= "https://github.com/KurtSanders/Hubitat-Fun-Api-Responses"
@Field static final String 	OPENAPI_API_JSON_LEAGUE = "https://raw.githubusercontent.com/ddsky/api-league-clients/master/apileague-openapi-3.json"
@Field static final String  API_LEAGUE_WEBSITE		= "<a target='_blank' rel='noopener noreferrer 'href='https://apileague.com/console/'>API League Website</a>"
@Field static final String  BOX_ARROW				= "<img src='https://raw.githubusercontent.com/KurtSanders/Hubitat-Api-Fun/refs/heads/main/images/box-arrow-up-right.png' />"
@Field static final Integer IDIOMS_MAX	 			= 695
@Field static final Integer IDIOMS_MAX_PER_FILE 	= 100
@Field static final Map    	SERVICES = [
        "sites" : [            
            "Jokes": [
		        url			: "https://api.apileague.com",
                path		: "/retrieve-random-joke",
                website		: "<a target='_blank' rel='noopener noreferrer 'href='https://apileague.com/console/'>API League Website</a>",
                api			: ['where':'headers', 'name': 'x-api-key'],
                response	: ["value": "joke"],
                parameters	: ["include-tags","exclude-tags"],
                keywords	: ["analogy","animal","alondes","christmas","chuck norris","clean","dark","deep thougths","food","holiday","insults","jewish","kids","knock knock","law","nerdy","nsfw","one liner","political","racist","relationship","religious","school","sexist","sexual","sport","yo momma"],
                description	: "This category returns a random joke. You can filter the jokes by tags and keywords. To make sure they are safe for work/home, you could use the exclude-tags to exclude jokes with certain tags such as \"sexual\" or \"racist\".",
                ],
            "Trivia": [
		        url			: "https://api.apileague.com",
                path		: "/retrieve-random-trivia",
                website		: "<a target='_blank' rel='noopener noreferrer 'href='https://apileague.com/console/'>API League Website</a>",
                api			: ['where':'headers', 'name': 'x-api-key'],
                response	: ["value": "trivia"],
                description	: "This category returns a random piece of trivia like \"Rio de Janeiro was once the capital of Portugal, making it the only European capital outside of Europe.\".",
                ],
            "Quotes": [
		        url			: "https://api.apileague.com",
                path		: "/retrieve-random-quote",
                website		: "<a target='_blank' rel='noopener noreferrer 'href='https://apileague.com/console/'>API League Website</a>",
                api			: ['where':'headers', 'name': 'x-api-key'],
                response	: ["author": "author","value": "quote"],
                description	: "This category returns a random quote from a collection of quotes. The quotes are from famous people and are in English.",
                ],
            "Riddles": [
		        url			: "https://api.apileague.com",
                path		: "/retrieve-random-riddle",
                website		: "<a target='_blank' rel='noopener noreferrer 'href='https://apileague.com/console/'>API League Website</a>",
                api			: ['where':'headers', 'name': 'x-api-key'],
                response	: ["difficulty": "difficulty","riddle": "riddle","value": "answer"],
                parameters	: ["difficulty"],
                keywords	: ["easy", "medium", "hard"],
                description	: "This category returns a random riddle or brain-teaser. Riddles are a great way to exercise your brain and keep it sharp. The API supports brain-teasers in three difficulty levels: easy, medium, and hard. You can also get a random riddle without specifying a difficulty level.",
                ],
            "Poems" : [
		        url			: "https://api.apileague.com",
                path		: "/retrieve-random-poem",
                website		: "<a target='_blank' rel='noopener noreferrer 'href='https://apileague.com/console/'>API League Website</a>",
                api			: ['where':'headers', 'name': 'x-api-key'],
                response	: ["author": "author","title": "title", "value": "poem"],
                description	: "This category returns a random poem by many famous authors.",
                ],
            "Idioms" : [
		        url			: "https://raw.githubusercontent.com",
                path		: "/KurtSanders/Hubitat-Fun-Api-Responses/refs/heads/main/data/production/Idioms-%s.json",
                response	: ["phrase":"phrase", "value":"definition"],
                description	: "This category returns a random idiom. No api key is required.",
                ]
            ]
        ]

library (
    base: "app",
    author: AUTHOR_NAME,
    category: "Apps",
    description: "Core functions for Fun Responses Device Driver.",
    name: "Fun-Api-Responses-Library",
    namespace: "kurtsanders",
    documentationLink: "https://github.com/KurtSanders/",
    version: "0.0.1",
    disclaimer: "This library is only for use with SanderSoft Apps and Drivers."
)

def help() {
    section("${getImage('instructions')} <b>${app.name} Online Documentation</b>", hideable: true, hidden: true) {
        paragraph "<a href='${GITHUB_LINK}#readme' target='_blank'><h4 style='color:DodgerBlue;'>Click this link to view Online Documentation for ${app.name}</h4></a>"
    }
}

String fmtHelpInfo(String str) {
    String info = "${PARENT_DEVICE_NAME} v${VERSION}"
    String prefLink = "<a href='${COMM_LINK}' target='_blank'>${str}<br><div style='font-size: 70%;'>${info}</div></a>"
    String topStyle = "style='font-size: 18px; padding: 1px 12px; border: 2px solid Crimson; border-radius: 6px;'" //SlateGray
    String topLink = "<a ${topStyle} href='${COMM_LINK}' target='_blank'>${str}<br><div style='font-size: 14px;'>${info}</div></a>"
    if (device) {   
        return "<div style='font-size: 160%; font-style: bold; padding: 2px 0px; text-align: center;'>${prefLink}</div>" +
            "<div style='text-align: center; position: absolute; top: 30px; right: 60px; padding: 0px;'><ul class='nav'><li>${topLink}</ul></li></div>"
    } else {
        return "<div style='text-align: center; position: absolue; top: 0px; right: 80px; padding: 0px;'><ul class='nav'><li>${topLink}</ul></li></div>"

    }
}

def createDataChildDevice(namespace, typeName, deviceNetworkId, label) {    
    logDebug "In createDataChildDevice()"
    def statusMessageD = ""
    def rc
    if(!getChildDevice(deviceNetworkId)) {
        logInfo "In createDataChildDevice - Child device not found - Creating device: ${typeName}"
        try {
            rc = addChildDevice(namespace, typeName, deviceNetworkId, ["name": "${typeName}", "label": "${label}", isComponent: false])
            statusMessageD = "<b>A device with the name '${label}' has been been created. (${typeName})</b>"
        } catch (e) { logErr "Unable to create device - ${e}" }
    } else {
        statusMessageD = "<b>The device '${label}' (${typeName}) already exists.</b>"
    }
    logInfo "${statusMessageD}"
    return rc
}


//Logging Functions
def logMessage(String msg) {
    if (app) {
        return "<span style='color: blue'>${app.name}</span>: ${msg}"   
    } else {
        return "<span style='color: green'>${device.name}</span>: ${msg}"           
    }
}

void logErr(String msg) {
    if (logLevelInfo.level>=1) log.error "${logMessage(msg)}"
}
void logWarn(String msg) {
    if (logLevelInfo.level>=2) log.warn "${logMessage(msg)}"
}
void logInfo(String msg) {
    if (logLevelInfo.level>=3) log.info "${logMessage(msg)}"
}

void logDebug(String msg) {
        if (logLevelInfo.level>=4) log.debug "${logMessage(msg)}"
}

void logTrace(String msg) {
        if (logLevelInfo.level>=5) log.trace "${logMessage(msg)}"
}

// Thanks to author: "Jean P. May Jr." for these following Hubitat local file access methods
// importUrl: "https://raw.githubusercontent.com/thebearmay/hubitat/main/libraries/localFileMethods.groovy",


HashMap securityLogin(){
    def result = false
    try{
        httpPost(
				[
					uri: "http://127.0.0.1:8080",
					path: "/login",
					query: 
					[
						loginRedirect: "/"
					],
					body:
					[
						username: username,
						password: password,
						submit: "Login"
					],
					textParser: true,
					ignoreSSLIssues: true
				]
		)
		{ resp ->
//			log.debug resp.data?.text
				if (resp.data?.text?.contains("The login information you supplied was incorrect."))
					result = false
				else {
					cookie = resp?.headers?.'Set-Cookie'?.split(';')?.getAt(0)
					result = true
		    	}
		}
    }catch (e){
			log.error "Error logging in: ${e}"
			result = false
            cookie = null
    }
	return [result: result, cookie: cookie]
}


Boolean fileExists(fName){

    uri = "http://${location.hub.localIP}:8080/local/${fName}";

     def params = [
        uri: uri
    ]

    try {
        httpGet(params) { resp ->
            if (resp != null){
                return true;
            } else {
                return false;
            }
        }
    } catch (exception){
        if (exception.message == "Not Found"){
            log.debug("File DOES NOT Exists for $fName)");
        } else {
            log.error("Find file $fName) :: Connection Exception: ${exception.message}");
        }
        return false;
    }

}

String readFile(fName){
    if(security) cookie = securityLogin().cookie
    uri = "http://${location.hub.localIP}:8080/local/${fName}"


    def params = [
        uri: uri,
        contentType: "text/html",
        textParser: true,
        headers: [
				"Cookie": cookie,
                "Accept": "application/octet-stream"
            ]
    ]

    try {
        httpGet(params) { resp ->
            if(resp!= null) {       
              // return resp.data
               int i = 0
               String delim = ""
               i = resp.data.read() 
               while (i != -1){
                   char c = (char) i
                   delim+=c
                   i = resp.data.read() 
               } 
               return delim
            }
            else {
                log.error "Null Response"
            }
        }
    } catch (exception) {
        log.error "Read Error: ${exception.message}"
        return null;
    }
}

Boolean appendFile(fName,newData){
    try {
        fileData = (String) readFile(fName)
        fileData = fileData.substring(0,fileData.length()-1)
        return writeFile(fName,fileData+newData)
    } catch (exception){
        if (exception.message == "Not Found"){
            return writeFile(fName, newData)      
        } else {
            log.error("Append $fName Exception: ${exception}")
            return false
        }
    }
}

Boolean xferFile(fileIn, fileOut) {
    fileBuffer = (String) readExtFile(fileIn)
    retStat = writeFile(fileOut, fileBuffer)
    return retStat
}

String readExtFile(fName){
    def params = [
        uri: fName,
        contentType: "text/html",
        textParser: true
    ]

    try {
        httpGet(params) { resp ->
            if(resp!= null) {
               int i = 0
               String delim = ""
               i = resp.data.read() 
               while (i != -1){
                   char c = (char) i
                   delim+=c
                   i = resp.data.read() 
               } 
               return delim
            }
            else {
                log.error "Null Response"
            }
        }
    } catch (exception) {
        log.error "Read Ext Error: ${exception.message}"
        return null;
    }
}


