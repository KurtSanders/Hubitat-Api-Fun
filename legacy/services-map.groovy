Map    	SERVICES = [
        "url": "https://api.apileague.com",
        "title": "API League",
    	"status" : ["401": "Unauthorized","402":"Payment Required","403":"Forbidden","404":"Not Found","406":"Not Acceptable","429": "Too Many Requests"],
        "sites" : [            
            "Jokes": [
                path		: "/retrieve-random-joke",
                response	: ["joke"],
                parameters	: ["include-tags","exclude-tags"],
                keywords	: ["analogy","animal","alondes","christmas","chuck norris","clean","dark","deep thougths","food","holiday","insults","jewish","kids","knock knock","law","nerdy","nsfw","one liner","political","racist","relationship","religious","school","sexist","sexual","sport","yo momma"],
                description	: "This category returns a random joke. You can filter the jokes by tags and keywords. To make sure they are safe for work/home, you could use the exclude-tags to exclude jokes with certain tags such as \"sexual\" or \"racist\".",
                ],
            "Trivia": [
                path		: "/retrieve-random-trivia",
                response	: ["trivia"],
                description	: "This category returns a random piece of trivia like \"Rio de Janeiro was once the capital of Portugal, making it the only European capital outside of Europe.\".",
                ],
            "Quotes": [
                path		: "/retrieve-random-quote",
                response	: ["author","quote"],
                description	: "This category returns a random quote from a collection of quotes. The quotes are from famous people and are in English.",
                ],
            "Riddles": [
                path		: "/retrieve-random-riddle",
                response	: ["difficulty","riddle","answer"],
                parameters	: ["difficulty"],
                keywords	: ["easy", "medium", "hard"],
                description	: "This category returns a random riddle or brain-teaser. Riddles are a great way to exercise your brain and keep it sharp. The API supports brain-teasers in three difficulty levels: easy, medium, and hard. You can also get a random riddle without specifying a difficulty level.",
                ],
            "Poems" : [
                path		: "/retrieve-random-poem",
                response	: ["title","author","poem"],
                description	: "Retrieve a random poem by many famous authors.",
                ]
            ]
        ]