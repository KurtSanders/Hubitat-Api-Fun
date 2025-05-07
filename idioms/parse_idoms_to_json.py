import json
import re
infilename = "/Users/sandeke/PycharmProjects/Hubitat-Fun/idioms/data/Idioms_phrases_raw.json"
outfilename = "/Users/sandeke/PycharmProjects/Hubitat-Fun/idioms/data/Idioms_phrases_map"
max_records_per_file = 250
# Write dictionary to a JSON file
def write_dict_to_json(data, fn):
    with open(fn, 'w') as file:
        json.dump(data, file, indent=4, ensure_ascii=False)  # indent for better readability

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

recTotal = len(data_raw)
pattern = r'^(?:\d*?\.\sLit\.\s|Lit\.\s|Fig\.\s)(.*?)(?:$|\s2\.\s)'
lastPhrase = ''
out_rec_id = 0
data_suffix = 1
data_out = {'dictionary': []}

for i in range(0,recTotal):
    record = data_raw[i]
    if lastPhrase != record['phrase']:
        rec_id = record['id']
        phrase = record['phrase']
#        print(f"Phrase: {phrase}")
        definition = record['definition']
#        print(f"Definition: {definition}")
        match = re.match(pattern, definition)
        if match:
            definition = match.group(1)

        data_out['dictionary'].append({'id': out_rec_id, 'phrase': phrase, 'definition': definition})
        out_rec_id += 1
        if out_rec_id % max_records_per_file == 0:
            filename = outfilename + str(data_suffix) + ".json"
            print (f"Writing dictionary to {filename}")
            write_dict_to_json(data_out,filename)
            data_suffix += 1
            out_rec_id = 0
            data_out = {'dictionary': []}

        lastPhrase = record['phrase']