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
