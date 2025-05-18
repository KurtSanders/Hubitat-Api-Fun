## Hubitat Fun Api Responses App
 * Idioms, Jokes, Trivia, Poems, Quotes & Riddles
 
 ---
 
This application generates random responses that are suitable for TTS on capability 'SpeechSynthesis' compatible devices (ie. Amazon, Google, Sonos, Ikea, etc).  

A free account on [APILeague.com](https://apileague.com/) is required to obtain an api string for those categories indicated below.  You may wish to purchase their paid plans, which allow more responses (tokens) per day.

The 'Idiom' category does not require an api account or api string. The **Idioms** category is powered by my own JSON file (over 700 entries), and I will add more on an as-needed basis. 

<center>

| Category  | API Required  | Api Website  |  Responses |
|---|:---:|:---:|:---:|
| Jokes | Yes |  [ApiLeague.com](https://apileague.com/) | Joke |
| Trivia | Yes |  [ApiLeague.com](https://apileague.com/) | Trivia |
| Poem | Yes |  [ApiLeague.com](https://apileague.com/) | Title, Author, Poem|
| Quotes | Yes |  [ApiLeague.com](https://apileague.com/) | Author, Quote |
| Riddles | Yes |  [ApiLeague.com](https://apileague.com/) | Question, Answer|
| Idioms | No |  | Phrase, Definition |

</center>

These responses are not only enjoyable to hear but also educational, especially for the younger generation.  A random response can be triggered by each separate device with a push momentary button and/or a Hubitat rule, for example, when you wake up, go to bed, arrive home, have company over, turn a switch on or off, etc.

## Screen Captures

#### Application Interface

<img src="https://raw.githubusercontent.com/KurtSanders/Hubitat-Fun-Api-Responses/refs/heads/main/images/AppScreenCapture.jpg">

#### Devices
* Separate devices with a 'Momentary Push Button' and/or Command 'Refresh' to generate a new response

<img src="https://raw.githubusercontent.com/KurtSanders/Hubitat-Fun-Api-Responses/refs/heads/main/images/devces.jpeg">

## Installation

* Available via Hubitat Package Manager (HPM)
