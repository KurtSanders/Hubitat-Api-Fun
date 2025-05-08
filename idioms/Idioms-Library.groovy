/*******************************************************************
*** SanderSoft - Core App/Device Helpers                        ***
/*******************************************************************/

import groovy.transform.Field

@Field static final String AUTHOR_NAME          	= "Kurt Sanders"
@Field static final String NAMESPACE            	= "kurtsanders"
@Field static final String COMM_LINK            	= "https://community.hubitat.com/"
@Field static final String GITHUB_LINK          	= "https://github.com/KurtSanders/Hubitat-Fun/tree/main"

library (
    base: "app",
    author: "Kurt Sanders",
    category: "Apps",
    description: "Core functions for Idioms Device Driver.",
    name: "Idioms-Library",
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