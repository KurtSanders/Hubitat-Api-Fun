# Hubitat-Idioms

### Overview

* Provides a new Idiom Phrase and Definition each time refreshed
* Used with voice systems like Alexa and Google Play

### Installation
* Available in Hubitat Package Manager (HPM).
* Search for "Idioms"
* Create a virtual device and select the 'Idioms' Device Driver

### How to Use
* Switch
  * Toggle 'on' to generate a new idiom phrase and definition.  The switch will auto-turn off.
* Commands
  * Refresh: generates a new idiom.
  * ResetKey: allows one to select a different starting idiom key (0-250).
  * Idioms File Number: allows one to select a different idiom file (1-85).  Each idiom file contains 250 idioms.  The idiom file number will be auto-incremented when the idiom-key exceeds 250, and the idiom-key will be reset to 0.

### Idioms Device Attributes and States

<img src="https://raw.githubusercontent.com/KurtSanders/Hubitat-Fun/main/idioms/images/Idioms_Device_Screenshot.jpg" width="400px"> 
