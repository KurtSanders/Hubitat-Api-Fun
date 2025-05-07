/*
 * Http GET Switch
 *
 * Calls URIs with HTTP GET for switch on or off
 * API: https://api.quotable.io/random
 */
metadata {
    definition(name: "JokesAPI", namespace: "kurtsanders", author: "Kurt Sanders") {
        capability "Actuator"
        capability "Sensor"
        capability "Refresh"
        attribute "joke", "string"
    }
}

preferences {
    section("URI") {
        input "URI", "text", title: "The http String from Joke Server (Encode Special Characters)", required: true, defaultValue: "https://api.humorapi.com/jokes/random"
        input "jsonKeyQuestion", "text", title: "JSON Joke Question Field", required: true
        input "jsonKeyAnswer", "text", title: "JSON Joke Answer Field", required: true
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
    }
}

def logsOff() {
    log.warn "debug logging disabled..."
    device.updateSetting("logEnable", [value: "false", type: "bool"])
}

def updated() {
    log.info "updated..."
    log.warn "debug logging is: ${logEnable == true}"
    if (logEnable) runIn(1800, logsOff)
}

def parse(String description) {
    if (logEnable) log.debug(description)
}

def refresh() {
    if (logEnable) log.debug "Sending on GET request to [${settings.URI}]"
    try {
        httpGet(settings.URI) { response ->
            if (logEnable) {
                if (response.data) log.debug "Json Question Key = ${jsonKeyQuestion} ==>" + response.data["${jsonKeyQuestion}"]
                if (response.data["${jsonKeyAnswer}"]) log.debug "Json Answer Key = ${jsonKeyAnswer} ==>" + response.data["${jsonKeyAnswer}"]
                if (response.headers) {
                    response.headers.each {
                        log.debug "${it.name} : ${it.value}"
                    }   
                } else {
                    log.warn "No Response Headers"
                }
            }
            if (response.success) {
                def joke = response.data["${jsonKeyQuestion}"]
                if (response.data["${jsonKeyAnswer}"]) {
                    joke += ' ' + response.data["${jsonKeyAnswer}"] 
                }
                sendEvent(name: "joke", value: joke, isStateChange: true)
            } else {
                sendEvent(name: "joke", value: "Opps, I don't have any witty jokes for you today", isStateChange: true)                
            }
        }
    } catch (Exception e) {
        log.warn "Http Call to ${settings.URI} failed: ${e.message}"
    }
}