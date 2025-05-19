import json
import re
# from profanity_check import predict, predict_prob
from better_profanity import profanity
profanity.load_censor_words(whitelist_words=['god','stroke',"he'll"])

infilename = "/Users/sandeke/PycharmProjects/Hubitat-Fun/idioms/data/Idioms_phrases_raw.json"
outfilename = "/Users/sandeke/Downloads/Idioms_phrases_map"

max_records_per_file = 250
# Write dictionary to a JSON file
def write_dict_to_json(data, fn):
    with open(fn, 'w') as file:
        json.dump(data, file, indent=1, ensure_ascii=False)  # indent for better readability

# Read dictionary from a JSON file
def read_dict_from_json():
    try:
        with open(infilename, 'r') as file:
            data = json.load(file)
        return data['dictionary']
    except FileNotFoundError:
        print(f"Error: File not found at path: {infilename}")
        return None
    except json.JSONDecodeError:
        print(f"Error: Invalid JSON format in file: {infilename}")
        return None
    except IndexError:
        print(f"Error: JSON data is empty or does not contain a first object: {infilename}")
        return None
    except Exception as e:
        print(f"An unexpected error occurred: {e}")
        return None# Example usage

data_raw = read_dict_from_json()

# recTotal = len(data_raw)
recTotal = 100
pattern = r'^(?:\d*?\.\s|\d*?\.\sLit\.\s|Lit\.\s|Fig\.\s|Rur\.\s|Euph\.\s)(.*?)(?:$|\s2\.\s)'
out_rec_id = 0
data_suffix = 1
data_out = {'dictionary': []}

for i in range(0,recTotal):
    current_record = data_raw[i]
    if i > 0:
        last_record = data_raw[i-1]
        if current_record['phrase'] == last_record['phrase']:
#            print(f"Eliminated duplicate phrase at {current_record['id']}")
            continue
        elif current_record['definition'] == last_record['definition']:
 #           print(f"Eliminated duplicate definition at {current_record['id']}")
            continue
    # Process this record
    phrase = current_record['phrase']
    definition = current_record['definition']

    if profanity.contains_profanity(phrase):
        print (f"Offensive: {phrase}")
        phrase = (profanity.censor(phrase))
        print (f"Corrected: {phrase}")
    if profanity.contains_profanity(definition):
        print (f"Offensive: {definition}")
        definition = (profanity.censor(definition))
        print (f"Corrected: {definition}")

    # Cleanup abbreviations and shorten definiton to first one
    match = re.match(pattern, definition)
    if match:
        definition = match.group(1)
    # write out the processed record
    data_out['dictionary'].append({'phrase': phrase.capitalize(), 'definition': definition.capitalize()})
    out_rec_id += 1
    if out_rec_id % max_records_per_file == 0:
        filename = outfilename + str(data_suffix) + ".json"
        print (f"Writing dictionary to {filename} #{data_suffix}")
        write_dict_to_json(data_out,filename)
        data_suffix += 1
        out_rec_id = 0
        data_out = {'dictionary': []}

filename = outfilename + str(data_suffix) + ".json"
print (f"Writing final dictionary to {filename}")
write_dict_to_json(data_out,filename)
