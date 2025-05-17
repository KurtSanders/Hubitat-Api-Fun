import json
import csv

infilename = "/Users/sandeke/PycharmProjects/Hubitat-Fun/idioms/data_v3/english_idioms.csv"
outfilename = "/Users/sandeke/Downloads/Idioms.json"
d = []

# Write dictionary to a JSON file
def write_dict_to_json(data, fn):
    with open(fn, 'w') as outfile:
        json.dump(data, outfile, indent=0, ensure_ascii=False)  # indent for better readability

reader = csv.reader(open(infilename, 'r'))
id = 0
with open(infilename) as infile:
    for i in csv.DictReader(infile):
        d.append(dict(i))
print (f"Writing final dictionary to {outfilename}")
write_dict_to_json(d,outfilename)

