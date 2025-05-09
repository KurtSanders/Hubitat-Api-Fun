import json
import csv

infilename = "/Users/sandeke/PycharmProjects/Hubitat-Fun/idioms/test/english_idioms.csv"
outfilename = "/Users/sandeke/Downloads/Idioms_phrases_map"
max_records_per_file = 250
data_suffix = 1
d = {'dictionary': []}

# Write dictionary to a JSON file
def write_dict_to_json(data, fn):
    with open(fn, 'w') as outfile:
        json.dump(data, outfile, indent=1, ensure_ascii=False)  # indent for better readability

reader = csv.reader(open(infilename, 'r'))
x = 0
with open(infilename) as infile:
    for i in csv.DictReader(infile):
        x += 1
        d['dictionary'].append(dict(i))
        if x % max_records_per_file == 0:
            filename = outfilename + str(data_suffix) + ".json"
            print (f"Writing dictionary to {filename} #{data_suffix}")
            write_dict_to_json(d,filename)
            data_suffix += 1
            d = {'dictionary': []}

filename = outfilename + str(data_suffix) + ".json"
print (f"Writing final dictionary to {filename}")
write_dict_to_json(d,filename)

